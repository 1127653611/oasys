package cn.oasys.web.controller.file;

import cn.oasys.web.model.pojo.file.AoaFileList;
import cn.oasys.web.model.pojo.file.AoaFilePath;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.user.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class FileController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @RequestMapping("filemanage")
    public String usermanage(@SessionAttribute("userId") Long userid, Model model) {
        AoaUser user = userService.findOne(userid);
        AoaFilePath filepath = fileService.findByPathName(user.getUserName());
        if (filepath == null) {
            AoaFilePath filepath1 = new AoaFilePath();
            filepath1.setParentId(1L);
            filepath1.setPathName(user.getUserName());
            filepath1.setPathUserId(user.getUserId());
            filepath = fileService.save(filepath1);
        }

        model.addAttribute("nowpath", filepath);
        model.addAttribute("paths", fileService.findpathByparent(filepath.getPathId()));
        model.addAttribute("files", filepath.getFileLists());

        model.addAttribute("userrootpath", filepath);
        model.addAttribute("mcpaths", fileService.findpathByparent(filepath.getPathId()));
        return "file/filemanage";
    }

    @RequestMapping("filetest")
    public String text(@SessionAttribute("userId") Long userid, @RequestParam("pathid") Long pathid, Model model) {
        AoaUser user = userService.findOne(userid);
        AoaFilePath userrootpath = fileService.findByPathName(user.getUserName());

        // 查询当前目录
        AoaFilePath filepath = fileService.findOne(pathid);

        // 查询当前目录的所有父级目录
        List<AoaFilePath> allparentpaths = new ArrayList<>();
        fileService.findAllParent(filepath, allparentpaths);
        Collections.reverse(allparentpaths);

        model.addAttribute("allparentpaths", allparentpaths);
        model.addAttribute("nowpath", filepath);
        model.addAttribute("paths", fileService.findpathByparent(filepath.getPathId()));
        model.addAttribute("files", filepath.getFileLists());
        //复制移动显示 目录
        model.addAttribute("userrootpath", userrootpath);
        model.addAttribute("mcpaths", fileService.findpathByparent(userrootpath.getPathId()));
        return "file/filemanage";
    }

    @RequestMapping("fileupload")
    public String uploadfile(@RequestParam("file") MultipartFile file, @RequestParam("pathid") Long pathid,
                             HttpSession session, Model model) throws IllegalStateException, IOException {
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user = userService.findOne(userid);
        AoaFilePath nowpath = fileService.findOne(pathid);
        // true 表示从文件使用上传
        fileService.savefile(file, user, nowpath, true);
        model.addAttribute("pathid", pathid);
        return "forward:/filetest";
    }
    @RequestMapping("findfileandpath")
    public String findfileandpath(@SessionAttribute("userId") Long userid,
                                  @RequestParam(value = "findfileandpath",required=false) String findfileandpath,
                                  @RequestParam(value = "type",defaultValue="all") String type,
                                  Model model){
        String findlike = "%" +findfileandpath+ "%";
        AoaUser user = userService.findOne(userid);
        fileService.findFile(user,findlike,type,model);

        model.addAttribute("type", type);
        return "file/filetypeload";

    }
    @RequestMapping("imgshow")
    public void imgshow(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
        AoaFileList filelist = fileService.findOneFile(fileid);
        File file = fileService.getFile(filelist.getFilePath());
        writefile(response, file);
    }

    public void writefile(HttpServletResponse response, File file) {
        ServletOutputStream sos = null;
        FileInputStream aa = null;
        try {
            aa = new FileInputStream(file);
            sos = response.getOutputStream();
            // 读取文件问字节码
            byte[] data = new byte[(int) file.length()];
            IOUtils.readFully(aa, data);
            // 将文件流输出到浏览器
            IOUtils.write(data, sos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                sos.close();
                aa.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("createpath")
    public String createpath(@SessionAttribute("userId") Long userid, @RequestParam("pathid") Long pathid, @RequestParam("pathname") String pathname,
                             Model model) {
        AoaFilePath filepath = fileService.findOne(pathid);
        String newname = fileService.onlyname(pathname, filepath, null, 1, false);

        AoaFilePath newfilepath = new AoaFilePath();
        newfilepath.setParentId(pathid);
        newfilepath.setPathUserId(userid);
        newfilepath.setPathName(newname);
        newfilepath.setPathIstrash(0L);

        fileService.save(newfilepath);

        model.addAttribute("pathid", pathid);
        return "forward:/filetest";
    }

    @RequestMapping("downfile")
    public void downFile(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
        try {
            AoaFileList filelist = fileService.findOneFile(fileid);
            File file = fileService.getFile(filelist.getFilePath());
            response.setContentLength(filelist.getSize().intValue());
            response.setContentType(filelist.getContentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filelist.getFileName().getBytes("UTF-8"), "ISO8859-1"));
            writefile(response, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("deletefile")
    public String deletefile(@SessionAttribute("userId") Long userid,
                             @RequestParam("pathid") Long pathid,
                             @RequestParam("checkpathids") List<Long> checkpathids,
                             @RequestParam("checkfileids") List<Long> checkfileids, Model model) {


        if (!checkfileids.isEmpty()) {

            fileService.trashfile(checkfileids, 1L, userid);
        }
        if (!checkpathids.isEmpty()) {

            fileService.trashpath(checkpathids, 1L, true);
        }

        model.addAttribute("pathid", pathid);
        return "forward:/filetest";
    }

    @RequestMapping("filetypeload")
    public String filetypeload(@SessionAttribute("userId") Long userid,
                               @RequestParam("type") String type,
                               Model model) {
        AoaUser user = userService.findOne(userid);
        fileService.loadFile(user, type, model);
        model.addAttribute("type", type);
        return "file/filetypeload";

    }

    @RequestMapping("doshare")
    public String doshare(@RequestParam("pathid") Long pathid,
                          @RequestParam("checkfileids") List<Long> checkfileids,
                          Model model
    ) {
        if (!checkfileids.isEmpty()) {
            fileService.doshare(checkfileids);
        }
        model.addAttribute("pathid", pathid);
        model.addAttribute("message", "分享成功");
        return "forward:/filetest";
    }

    @RequestMapping("fileloadshare")
    public String fileloadshare(@RequestParam("type") String type,
                                @RequestParam(value = "checkfileids[]", required = false) List<Long> checkfileids,
                                Model model
    ) {
        if (checkfileids != null) {

            fileService.doshare(checkfileids);
        }

        model.addAttribute("message", "分享成功");
        return "forward:/filetypeload";
    }

    @RequestMapping("fileloadtrashfile")
    public String fileloadtrashfile(@SessionAttribute("userId") Long userid,
                                    @RequestParam("type") String type,
                                    @RequestParam(value = "checkpathids[]", required = false) List<Long> checkpathids,
                                    @RequestParam(value = "checkfileids[]", required = false) List<Long> checkfileids,
                                    Model model) {

        System.out.println(type + checkpathids + checkfileids);
        if (checkfileids != null) {
            // 文件放入回收站
            fileService.trashfile(checkfileids, 1L, userid);
        }
        if (checkpathids != null) {
            // 删除文件夹
            fileService.trashpath(checkpathids, 1L, true);
            //fs.trashPath(checkpathids);
        }

        model.addAttribute("type", type);
        return "forward:/filetypeload";
    }

    @RequestMapping("fileloadrename")
    public String fileloadrename(@RequestParam("type") String type,
                                 @RequestParam("renamefp") Long renamefp,
                                 @RequestParam("creatpathinput") String creatpathinput,
                                 @RequestParam("isfile") boolean isfile,
                                 @RequestParam(value = "pathid", required = false) Long pathid,
                                 Model model) {
        fileService.rename(creatpathinput, renamefp, pathid, isfile);

        model.addAttribute("type", type);
        return "forward:/filetypeload";
    }

    @RequestMapping("rename")
    public String rename(@RequestParam("name") String name,
                         @RequestParam("renamefp") Long renamefp,
                         @RequestParam("pathid") Long pathid,
                         @RequestParam("isfile") boolean isfile,
                         Model model) {

        //这里调用重命名方法
        fileService.rename(name, renamefp, pathid, isfile);

        model.addAttribute("pathid", pathid);
        return "forward:/filetest";

    }

    @RequestMapping("filereturnback")
    public String filereturnback(@SessionAttribute("userId") Long userid,
                                 @RequestParam("type") String type,
                                 @RequestParam(value = "checkpathids[]", required = false) List<Long> checkpathids,
                                 @RequestParam(value = "checkfileids[]", required = false) List<Long> checkfileids,
                                 Model model) {
        if (checkfileids != null) {
            fileService.filereturnback(checkfileids, userid);
        }
        if (checkpathids != null) {
            fileService.pathreturnback(checkpathids, userid);
        }

        model.addAttribute("type", type);
        return "forward:/filetypeload";

    }

    @RequestMapping("mcloadpath")
    public String mcloadpath(@RequestParam("mctoid") Long mctoid, @RequestParam("mcpathids") List<Long> mcpathids, Model model) {
        System.out.println(mctoid);
        System.out.println(mcpathids);

        List<AoaFilePath> showsonpaths = fileService.mcpathload(mctoid, mcpathids);
        model.addAttribute("mcpaths", showsonpaths);
        return "file/mcpathload";
    }

    @RequestMapping("mcto")
    public String mcto(@SessionAttribute("userId") Long userid,
                       @RequestParam("morc") boolean morc,
                       @RequestParam("mctoid") Long mctoid,
                       @RequestParam("pathid") Long pathid,
                       @RequestParam("mcfileids") List<Long> mcfileids,
                       @RequestParam("mcpathids") List<Long> mcpathids,
                       Model model) {
        System.out.println(morc);
        System.out.println(mctoid);
        System.out.println(mcfileids);
        System.out.println(mcpathids);

        if (morc) {

            fileService.moveAndcopy(mcfileids, mcpathids, mctoid, true, userid);
        } else {

            fileService.moveAndcopy(mcfileids, mcpathids, mctoid, false, userid);
        }

        model.addAttribute("pathid", pathid);
        return "forward:/filetest";
    }

    @RequestMapping("fileloaddeletefile")
    public String fileloaddeletefile(@RequestParam("type") String type,
                                     @RequestParam(value = "checkpathids[]", required = false) List<Long> checkpathids,
                                     @RequestParam(value = "checkfileids[]", required = false) List<Long> checkfileids,
                                     Model model) {

        System.out.println(type + checkpathids + checkfileids);
        if (checkfileids != null) {
            // 删除文件
            fileService.deleteFile(checkfileids);
        }
        if (checkpathids != null) {
            // 删除文件夹
            fileService.deletePath(checkpathids);
        }

        model.addAttribute("type", type);
        return "forward:/filetypeload";

    }
}
