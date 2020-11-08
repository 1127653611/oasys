package cn.oasys.web.service.impl.file;

import cn.oasys.web.model.dao.file.AoaFileListMapper;
import cn.oasys.web.model.dao.file.AoaFilePathMapper;
import cn.oasys.web.model.dao.note.AoaAttachmentListMapper;
import cn.oasys.web.model.pojo.file.AoaFileList;
import cn.oasys.web.model.pojo.file.AoaFilePath;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class FileServiceImpl implements FileService {
    @Autowired
    private AoaFileListMapper aoaFileListMapper;
    @Autowired
    private AoaAttachmentListMapper aoaAttachmentListMapper;
    @Value("${file.rootpath}")
    private String filerootPath;
    @Value("${attachment.roopath}")
    private String atachmentpath;
    @Value("${user.avatar.rootpath}")
    private String userAvatarRootpath;
    @Value("${img.rootpath}")
    private String imgpath;
    @Value("${contact_person.avatar.rootpath}")
    private String contactpersonavatarrootpath;
    @Autowired
    private AoaFilePathMapper aoaFilePathMapper;

    @Override
    public AoaAttachmentList upload(MultipartFile file, Long userId, String model, String type) throws IOException {
        String path = null;
        if (type.equals("contact")) {
            path = this.contactpersonavatarrootpath;
            System.out.println(path);
        } else if (type.equals("img")) {
            path = this.imgpath;
        } else if (type.equals("atta")) {
            path = this.atachmentpath;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        File savepath = new File(path, simpleDateFormat.format(new Date()));
        if (!savepath.exists()) {
            savepath.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        if (!StringUtil.isEmpty(fileName)) {
            String suffix = FilenameUtils.getExtension(fileName);
            String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + suffix;
            File targetFile = new File(savepath, newFileName);
            file.transferTo(targetFile);

            AoaAttachmentList attachment = new AoaAttachmentList();
            attachment.setAttachmentName(file.getOriginalFilename());
            attachment.setAttachmentPath(targetFile.getAbsolutePath().replace("\\", "/").replace(path, ""));
            attachment.setAttachmentShuffix(suffix);
            attachment.setAttachmentSize(String.valueOf(file.getSize()));
            attachment.setAttachmentType(file.getContentType());
            attachment.setUploadTime(new Date());
            attachment.setUserId(userId + "");
            attachment.setModel(model);
            aoaAttachmentListMapper.insertSelective(attachment);
            return attachment;
        }
        return null;
    }

    @Override
    public AoaFilePath findByPathName(String userName) {
        return aoaFilePathMapper.findByPathName(userName);
    }

    @Override
    public AoaFilePath save(AoaFilePath filepath1) {
        aoaFilePathMapper.insertSelective(filepath1);
        return aoaFilePathMapper.findOne(filepath1.getPathId());
    }

    @Override
    public List<AoaFilePath> findpathByparent(Long parentId) {
        return aoaFilePathMapper.findpathByparent(parentId, 0L);
    }

    @Override
    public AoaFilePath findOne(Long pathid) {
        return aoaFilePathMapper.findOne(pathid);
    }

    @Override
    public void findAllParent(AoaFilePath filepath, List<AoaFilePath> allparentpaths) {
        if (filepath.getParentId() != 1L) {
            AoaFilePath filepath1 = aoaFilePathMapper.findOne(filepath.getParentId());
            allparentpaths.add(filepath1);
            findAllParent(filepath1, allparentpaths);
        }
    }

    @Override
    public Object savefile(MultipartFile file, AoaUser user, AoaFilePath nowpath, boolean isfile) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        File root;
        if (isfile) {
            root = new File(this.filerootPath, user.getUserName());
        } else {
            root = new File(this.atachmentpath, user.getUserName());
        }

        File savepath = new File(root, simpleDateFormat.format(new Date()));

        if (!savepath.exists()) {
            savepath.mkdirs();
        }

        String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
        File targetFile = new File(savepath, newFileName);
        file.transferTo(targetFile);

        if (isfile) {
            AoaFileList filelist = new AoaFileList();
            String filename = file.getOriginalFilename();
            filename = onlyname(filename, nowpath, shuffix, 1, true);
            filelist.setFileName(filename);
            filelist.setFilePath(targetFile.getAbsolutePath().replace("\\", "/").replace(this.filerootPath, ""));
            filelist.setFileShuffix(shuffix);
            filelist.setSize(file.getSize());
            filelist.setUploadTime(new Date());
            filelist.setPathId(nowpath.getPathId());
            filelist.setContentType(file.getContentType());
            filelist.setFileUserId(user.getUserId());
            filelist.setFileIsshare(0L);
            aoaFileListMapper.insertSelective(filelist);
            return filelist;
        } else {
            AoaAttachmentList attachment = new AoaAttachmentList();
            attachment.setAttachmentName(file.getOriginalFilename());
            attachment.setAttachmentPath(targetFile.getAbsolutePath().replace("\\", "/").replace(this.atachmentpath, ""));
            attachment.setAttachmentShuffix(shuffix);
            attachment.setAttachmentSize(String.valueOf(file.getSize()));
            attachment.setAttachmentType(file.getContentType());
            attachment.setUploadTime(new Date());
            attachment.setUserId(user.getUserId() + "");
            attachment.setModel("note");
            aoaAttachmentListMapper.insertSelective(attachment);
            return attachment;
        }
    }

    @Override
    public AoaFileList findOneFile(Long fileid) {
        return aoaFileListMapper.finOne(fileid);
    }

    @Override
    public File getFile(String filePath) {
        return new File(this.filerootPath, filePath);
    }

    @Override
    public String onlyname(String name, AoaFilePath filepath, String shuffix, int num, boolean isfile) {
        boolean ispass = false;
        if (isfile) {
            List<AoaFileList> aoaFileLists = aoaFileListMapper.findByFileNameAndFpath(name, filepath.getPathId());
            if (aoaFileLists.isEmpty() || aoaFileLists == null) {
                ispass = true;
            }
        } else {
            List<AoaFilePath> aoaFilePaths = aoaFilePathMapper.findByPathNameAndParentId(name, filepath.getPathId());
            if (aoaFilePaths.isEmpty() || aoaFilePaths == null) {
                ispass = true;
            }
        }
        if (!ispass) {
            int num2 = num - 1;
            if (shuffix == null) {
                name = name.replace("(" + num2 + ")", "") + "(" + num + ")";
            } else {
                name = name.replace("." + shuffix, "").replace("(" + num2 + ")", "") + "(" + num + ")" + "." + shuffix;
            }
            num += 1;
            return onlyname(name, filepath, shuffix, num, isfile);
        }
        return name;
    }

    @Override
    public void trashfile(List<Long> checkfileids, long istrash, Long userid) {
        for (Long fileid : checkfileids) {
            AoaFileList fileList = aoaFileListMapper.finOne(fileid);
            fileList.setFileIstrash(istrash);
            if (userid != null) {
                fileList.setPathId(null);
            }

            aoaFileListMapper.updateByPrimaryKey(fileList);
        }
    }

    @Override
    public void trashpath(List<Long> checkpathids, long istrash, boolean isfirst) {
        for (Long pathid : checkpathids) {
            AoaFilePath filepath = aoaFilePathMapper.findOne(pathid);


            //首先将此文件夹下的文件放入回收战
            List<AoaFileList> files = aoaFileListMapper.findByFilePath(filepath.getPathId());
            if (!files.isEmpty()) {

                List<Long> fileids = new ArrayList<>();
                for (AoaFileList filelist : files) {
                    fileids.add(filelist.getFileId());
                }
                trashfile(fileids, 2L, null);
            }

            List<AoaFilePath> filepaths = aoaFilePathMapper.findByParentId(pathid);
            if (!filepaths.isEmpty()) {

                List<Long> pathids2 = new ArrayList<>();
                for (AoaFilePath filePath : filepaths) {
                    pathids2.add(filePath.getPathId());
                }

                trashpath(pathids2, 2L, false);
            }
            if (isfirst) {
                filepath.setParentId(0L);
            }
            filepath.setPathIstrash(istrash);
            aoaFilePathMapper.updateByPrimaryKey(filepath);
        }
    }

    @Override
    public void loadFile(AoaUser user, String type, Model model) {
        String contenttype;
        List<AoaFileList> fileLists = null;
        List<AoaFilePath> filePaths = null;
        switch (type) {

            case "document":
                fileLists = aoaFileListMapper.finddocument(user.getUserId());
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "picture":
                contenttype = "image/%";
                fileLists = aoaFileListMapper.findByUserAndContentTypeLikeAndFileIstrash(user.getUserId(), contenttype, 0L);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "music":
                contenttype = "audio/%";
                fileLists = aoaFileListMapper.findByUserAndContentTypeLikeAndFileIstrash(user.getUserId(), contenttype, 0L);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "video":
                contenttype = "video/%";
                fileLists = aoaFileListMapper.findByUserAndContentTypeLikeAndFileIstrash(user.getUserId(), contenttype, 0L);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;
            case "yasuo":
                contenttype = "application/o%";
                fileLists = aoaFileListMapper.findByUserAndContentTypeLikeAndFileIstrash(user.getUserId(), contenttype, 0L);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "trash":
                filePaths = aoaFilePathMapper.findByPathUserIdAndPathIstrash(user.getUserId(), 1L);
                fileLists = aoaFileListMapper.findByUserAndFileIstrash(user.getUserId(), 1L);

                model.addAttribute("paths", filePaths);
                model.addAttribute("files", fileLists);
                model.addAttribute("istrash", 1);
                model.addAttribute("isload", 1);
                break;

            case "share":
                fileLists = aoaFileListMapper.findByFileIsshareAndFileIstrash(1L, 0L);
                model.addAttribute("files", fileLists);
                model.addAttribute("isshare", 1);
                model.addAttribute("isload", 1);
                model.addAttribute("userid", user.getUserId());
                break;


            default:
                break;
        }
    }

    @Override
    public void doshare(List<Long> checkfileids) {
        for (Long fileid : checkfileids) {

            AoaFileList filelist = aoaFileListMapper.finOne(fileid);

            filelist.setFileIsshare(1L);
            aoaFileListMapper.updateByPrimaryKey(filelist);
        }
    }

    @Override
    public void rename(String creatpathinput, Long renamefp, Long pathid, boolean isfile) {
        if (isfile) {
            //文件名修改
            AoaFileList fl = aoaFileListMapper.finOne(renamefp);
            AoaFilePath aoaFilePath = aoaFilePathMapper.findOne(fl.getPathId());
            String newname;
            if (!fl.getFileName().equals(creatpathinput)) {
                newname = onlyname(creatpathinput, aoaFilePath, fl.getFileShuffix(), 1, isfile);
            } else {
                newname = creatpathinput;
            }
            fl.setFileName(newname);
            aoaFileListMapper.updateByPrimaryKey(fl);
        } else {
            //文件夹名修改
            AoaFilePath fp = aoaFilePathMapper.findOne(renamefp);
            AoaFilePath filepath = aoaFilePathMapper.findOne(pathid);
            String newname;
            if (!fp.getPathName().equals(creatpathinput)) {
                newname = onlyname(creatpathinput, filepath, null, 1, false);
            } else {
                newname = creatpathinput;
            }
            fp.setPathName(newname);
            aoaFilePathMapper.updateByPrimaryKey(fp);
        }
    }

    @Override
    public void filereturnback(List<Long> checkfileids, Long userid) {
        AoaFilePath fpath = aoaFilePathMapper.findByParentIdAndPathUserId(1L, userid);
        for (Long checkfileid : checkfileids) {
            AoaFileList fileList = aoaFileListMapper.finOne(checkfileid);

            if (userid != null) {
                String name = onlyname(fileList.getFileName(), fpath, fileList.getFileShuffix(), 1, true);
                fileList.setPathId(fpath.getPathId());
                fileList.setFileName(name);
            }
            fileList.setFileIstrash(0L);
            aoaFileListMapper.updateByPrimaryKey(fileList);
        }

    }

    @Override
    public void pathreturnback(List<Long> checkpathids, Long userid) {
        for (Long pathid : checkpathids) {
            AoaFilePath filepath = aoaFilePathMapper.findOne(pathid);
            //首先将此文件夹下的文件还原
            List<AoaFileList> files = aoaFileListMapper.findByPath(filepath.getPathId());
            if (!files.isEmpty()) {
                List<Long> fileids = new ArrayList<>();
                for (AoaFileList filelist : files) {
                    fileids.add(filelist.getFileId());
                }
                filereturnback(fileids, null);
            }
            //然后将此文件夹下的文件夹还原
            List<AoaFilePath> filepaths = aoaFilePathMapper.findByParentId(pathid);
            if (!filepaths.isEmpty()) {
                List<Long> pathids2 = new ArrayList<>();
                for (AoaFilePath filePath : filepaths) {
                    pathids2.add(filePath.getPathId());
                }
                pathreturnback(pathids2, null);
            }
            if (userid != null) {
                AoaFilePath fpath = aoaFilePathMapper.findByParentIdAndPathUserId(1L, userid);
                String name = onlyname(filepath.getPathName(), fpath, null, 1, false);
                filepath.setPathName(name);
                filepath.setParentId(fpath.getPathId());
            }

            filepath.setPathIstrash(0L);
            aoaFilePathMapper.updateByPrimaryKey(filepath);
        }
    }

    @Override
    public List<AoaFilePath> mcpathload(Long mctoid, List<Long> mcpathids) {
        List<AoaFilePath> showsonpath = new ArrayList<>();
        List<AoaFilePath> sonpaths = aoaFilePathMapper.findByParentIdAndPathIstrash(mctoid, 0L);

        for (AoaFilePath sonpath : sonpaths) {
            boolean nosame = true;
            for (Long mcpathid : mcpathids) {
                if (sonpath.getPathId().equals(mcpathid)) {
                    nosame = false;
                    break;
                }
            }
            if (nosame) {
                showsonpath.add(sonpath);
            }
        }
        return showsonpath;
    }

    @Override
    public void moveAndcopy(List<Long> mcfileids, List<Long> mcpathids, Long topathid, boolean fromwhere, Long userid) {
        AoaFilePath topath = aoaFilePathMapper.findOne(topathid);
        if (fromwhere) {
            if (!mcfileids.isEmpty()) {
                for (Long mcfileid : mcfileids) {
                    AoaFileList filelist = aoaFileListMapper.finOne(mcfileid);
                    String filename = onlyname(filelist.getFileName(), topath, filelist.getFileShuffix(), 1, true);
                    filelist.setPathId(topath.getPathId());
                    filelist.setFileName(filename);
                    aoaFileListMapper.updateByPrimaryKey(filelist);
                }
            }
            if (!mcpathids.isEmpty()) {
                for (Long mcpathid : mcpathids) {
                    AoaFilePath filepath = aoaFilePathMapper.findOne(mcpathid);
                    String name = onlyname(filepath.getPathName(), topath, null, 1, false);
                    filepath.setParentId(topathid);
                    filepath.setPathName(name);
                    aoaFilePathMapper.updateByPrimaryKey(filepath);
                }
            }
        } else {
            if (!mcfileids.isEmpty()) {
                for (Long mcfileid : mcfileids) {
                    AoaFileList filelist = aoaFileListMapper.finOne(mcfileid);
                    copyfile(filelist, topath, true);
                }
            }
            if (!mcpathids.isEmpty()) {
                for (Long mcpathid : mcpathids) {
                    copypath(mcpathid, topathid, true, userid);
                }
            }
        }
    }

    @Override
    public void deleteFile(List<Long> checkfileids) {
        for (Long fileid : checkfileids) {
            AoaFileList filelist = aoaFileListMapper.finOne(fileid);

            File file = new File(this.filerootPath, filelist.getFilePath());
            aoaFileListMapper.delete(fileid);

            file.delete();

        }
    }

    @Override
    public void deletePath(List<Long> checkpathids) {
        for (Long pathid : checkpathids) {
            AoaFilePath filepath = aoaFilePathMapper.findOne(pathid);

            //首先删除此文件夹下的文件
            List<AoaFileList> files = aoaFileListMapper.findByPath(filepath.getPathId());

            if (!files.isEmpty()) {
                List<Long> fileids = new ArrayList<>();
                for (AoaFileList filelist : files) {
                    fileids.add(filelist.getFileId());
                }
                deleteFile(fileids);
            }

            //然后删除此文件夹下的文件夹
            List<AoaFilePath> filepaths = aoaFilePathMapper.findByParentId(pathid);
            if (!filepaths.isEmpty()) {
                List<Long> pathids2 = new ArrayList<>();
                for (AoaFilePath filePath : filepaths) {
                    pathids2.add(filePath.getPathId());
                }
                deletePath(pathids2);
            }

            aoaFilePathMapper.delete(filepath.getPathId());
        }
    }

    @Override
    public void findFile(AoaUser user, String findlike, String type, Model model) {
        AoaFilePath fpath = aoaFilePathMapper.findByParentIdAndPathUserId(1L, user.getUserId());
        String contenttype;
        List<AoaFileList> fileLists = null;
        List<AoaFilePath> filePaths = null;
        switch (type) {

            case "document":
                fileLists = aoaFileListMapper.finddocumentlike(user.getUserId(), findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "picture":
                contenttype = "image/%";
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(user.getUserId(), 0L, contenttype, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "music":
                contenttype = "audio/%";
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(user.getUserId(), 0L, contenttype, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "video":
                contenttype = "video/%";
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(user.getUserId(), 0L, contenttype, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "yasuo":
                contenttype = "application/o%";
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(user.getUserId(), 0L, contenttype, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isload", 1);
                break;

            case "trash":
                filePaths = aoaFilePathMapper.findByPathUserIdAndPathIstrashAndPathNameLikeAndParentIdNot(user.getUserId(), 1L, findlike, 1L);
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(user.getUserId(), 1L, "%%", findlike);
                model.addAttribute("istrash", 1);
                model.addAttribute("isload", 1);
                model.addAttribute("paths", filePaths);
                model.addAttribute("files", fileLists);
                break;

            case "share":
                fileLists = aoaFileListMapper.findByFileIsshareAndFileNameLike(1L, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("isshare", 1);
                model.addAttribute("isload", 1);
                break;

            default:
                filePaths = aoaFilePathMapper.findByPathUserIdAndPathIstrashAndPathNameLikeAndParentIdNot(user.getUserId(), 0L, findlike, 1L);
                fileLists = aoaFileListMapper.findByUserAndFileIstrashAndFileNameLike(user.getUserId(), 0L, findlike);
                model.addAttribute("files", fileLists);
                model.addAttribute("paths", filePaths);
                model.addAttribute("isload", 1);
                break;
        }

    }

    @Override
    public Integer updateatt(MultipartFile file, AoaUser user, Object o, Long attachId) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        File root = new File(this.atachmentpath, user.getUserName());

        File savepath = new File(root, simpleDateFormat.format(new Date()));

        if (!savepath.exists()) {
            savepath.mkdirs();
        }
        if (!file.isEmpty()) {
            String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
            File targetFile = new File(savepath, newFileName);
            file.transferTo(targetFile);
            AoaAttachmentList aoaAttachmentList = aoaAttachmentListMapper.findOne(attachId);
            aoaAttachmentList.setAttachmentName(file.getOriginalFilename());
            aoaAttachmentList.setAttachmentPath(targetFile.getAbsolutePath().replace("\\", "/").replace(this.atachmentpath, ""));
            aoaAttachmentList.setAttachmentShuffix(shuffix);
            aoaAttachmentList.setAttachmentSize(file.getSize() + "");
            aoaAttachmentList.setAttachmentType(file.getContentType());
            aoaAttachmentList.setUploadTime(new Date());
            return aoaAttachmentListMapper.updateByPrimaryKeySelective(aoaAttachmentList);

        }
        return 0;
    }

    @Override
    public AoaAttachmentList findByAttachmentId(Long paid) {
        return aoaAttachmentListMapper.findOne(paid);
    }

    @Override
    public File get(AoaAttachmentList aoaAttachmentList) {
        return new File(this.atachmentpath ,aoaAttachmentList.getAttachmentPath());
    }

    @Override
    public Long count() {
        return aoaFileListMapper.count();
    }

    @Override
    public String upload(MultipartFile filePath) throws IOException {
        File dir = new File(userAvatarRootpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = filePath.getOriginalFilename();
        if (!StringUtil.isEmpty(fileName)) {

            String suffix = FilenameUtils.getExtension(fileName);

            String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + suffix;
            File targetFile = new File(dir, newFileName);
            filePath.transferTo(targetFile);
            String imgpath = targetFile.getPath().replace("\\", "/").replace(userAvatarRootpath, "");
            return imgpath;
        } else {
            return null;
        }
    }

    private void copyfile(AoaFileList filelist, AoaFilePath topath, boolean isfilein) {
        File s = getFile(filelist.getFilePath());
        AoaUser user = filelist.getUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        File root = new File(this.filerootPath, user.getUserName());
        File savepath = new File(root, simpleDateFormat.format(new Date()));

        if (!savepath.exists()) {
            savepath.mkdirs();
        }

        String shuffix = filelist.getFileShuffix();
        String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
        File t = new File(savepath, newFileName);

        copyfileio(s, t);

        AoaFileList filelist1 = new AoaFileList();
        String filename = "";
        if (isfilein) {
            filename = "拷贝 " + filelist.getFileName().replace("拷贝 ", "");
        } else {
            filename = filelist.getFileName();
        }
        filename = onlyname(filename, topath, shuffix, 1, true);
        filelist1.setFileName(filename);
        filelist1.setFilePath(t.getAbsolutePath().replace("\\", "/").replace(this.filerootPath, ""));
        filelist1.setFileShuffix(shuffix);
        filelist1.setSize(filelist.getSize());
        filelist1.setUploadTime(new Date());
        filelist1.setPathId(topath.getPathId());
        filelist1.setContentType(filelist.getContentType());
        filelist1.setFileUserId(user.getUserId());
        filelist1.setFileIsshare(0L);
        aoaFileListMapper.insertSelective(filelist1);
    }

    private void copypath(Long mcpathid, Long topathid, boolean isfirst, Long userid) {
        AoaFilePath filepath = aoaFilePathMapper.findOne(mcpathid);

        //第一个文件夹的复制
        AoaFilePath copypath = new AoaFilePath();
        copypath.setParentId(topathid);
        String copypathname = filepath.getPathName();
        if (isfirst) {
            copypathname = "拷贝 " + filepath.getPathName().replace("拷贝 ", "");
        }
        copypath.setPathName(copypathname);
        copypath.setPathUserId(userid);
        copypath = save(copypath);

        //这一个文件夹下的文件的复制
        List<AoaFileList> filelists = aoaFileListMapper.findByFilePath(filepath.getPathId());
        for (AoaFileList fileList : filelists) {
            copyfile(fileList, copypath, false);
        }

        List<AoaFilePath> filepathsons = aoaFilePathMapper.findByParentIdAndPathIstrash(filepath.getPathId(), 0L);

        if (!filepathsons.isEmpty()) {
            for (AoaFilePath filepathson : filepathsons) {
                copypath(filepathson.getPathId(), copypath.getPathId(), false, userid);
            }
        }
    }

    public void copyfileio(File s, File t) {
        InputStream fis = null;
        OutputStream fos = null;

        try {
            fis = new BufferedInputStream(new FileInputStream(s));
            fos = new BufferedOutputStream(new FileOutputStream(t));
            byte[] buf = new byte[2048];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}


