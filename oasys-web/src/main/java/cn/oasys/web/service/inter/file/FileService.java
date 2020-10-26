package cn.oasys.web.service.inter.file;

import cn.oasys.web.model.pojo.file.AoaFileList;
import cn.oasys.web.model.pojo.file.AoaFilePath;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {

    AoaFilePath findByPathName(String userName);

    AoaFilePath save(AoaFilePath filepath1);

    List<AoaFilePath> findpathByparent(Long parentId);

    AoaFilePath findOne(Long pathid);

    void findAllParent(AoaFilePath filepath, List<AoaFilePath> allparentpaths);

    Object savefile(MultipartFile file, AoaUser user, AoaFilePath nowpath, boolean isfile) throws IOException;

    AoaFileList findOneFile(Long fileid);

    File getFile(String filePath);

    String onlyname(String name,AoaFilePath filepath,String shuffix,int num,boolean isfile);

    void trashfile(List<Long> checkfileids, long istrash, Long userid);

    void trashpath(List<Long> checkpathids, long istrash, boolean isfirst);

    void loadFile(AoaUser user, String type, Model model);

    void doshare(List<Long> checkfileids);

    void rename(String creatpathinput, Long renamefp, Long pathid, boolean isfile);

    void filereturnback(List<Long> checkfileids, Long userid);

    void pathreturnback(List<Long> checkpathids, Long userid);

    List<AoaFilePath> mcpathload(Long mctoid, List<Long> mcpathids);

    void moveAndcopy(List<Long> mcfileids, List<Long> mcpathids, Long mctoid, boolean fromwhere, Long userid);

    void deleteFile(List<Long> checkfileids);

    void deletePath(List<Long> checkpathids);

    void findFile(AoaUser user, String findlike, String type,Model model);

    Integer updateatt(MultipartFile file, AoaUser user, Object o, Long attachId) throws IOException;

    AoaAttachmentList findByAttachmentId(Long paid);

    File get(AoaAttachmentList aoaAttachmentList);

    Long count();

    String upload(MultipartFile filePath) throws IOException;
}
