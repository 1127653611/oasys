package cn.oasys.web.controller.common;

import cn.oasys.web.common.Common;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @ Description   :  TODO
 * @ Author        :  wang
 * @ CreateDate    :  2020/10/23 9:04
 */
@Controller
@RequestMapping("/")
public class ConmmonController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private FileService fileService;
    @Value("${img.rootpath}")
    private String imgrootpath;
    @Value("${attachment.roopath}")
    private String atachrootpath;
    @Autowired
    private UserService userService;
    @RequestMapping("names")
    public String serch(Model model, HttpServletRequest req, @SessionAttribute("userId") Long userId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "title", required = false) String title) {
        PageHelper.startPage(page, size);
        List<AoaUser> userlist;
        if (StringUtils.isEmpty(title)) {

            userlist = userService.findAll();
        } else {

            String key = "%" + title + "%";
            userlist = userService.findAllByLike(key);
        }
        PageInfo<AoaUser> pageuser = new PageInfo<>(userlist);
        model.addAttribute("emplist", userlist);
        model.addAttribute("page", pageuser);
        model.addAttribute("url", "names");
        return "common/recivers";

    }
    @RequestMapping("namereceive")
    public String serch1(Model model, HttpServletRequest req, @SessionAttribute("userId") Long userId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "title", required = false) String title) {
        PageHelper.startPage(page, size);
        List<AoaUser> userlist;
        if (StringUtils.isEmpty(title)) {

            userlist = userService.findAll();
        } else {

            String key = "%" + title + "%";
            userlist = userService.findAllByLike(key);
        }
        PageInfo<AoaUser> pageuser = new PageInfo<>(userlist);
        model.addAttribute("emplist", userlist);
        model.addAttribute("page", pageuser);
        model.addAttribute("url", "namereceive");
        return "common/noterecivers";

    }

    @RequestMapping("file")
    public void downFile(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
        try {
            AoaAttachmentList attd = fileService.findByAttachmentId(fileid);
            File file = new File(imgrootpath, attd.getAttachmentPath());
            response.setContentLength(Integer.parseInt(attd.getAttachmentSize()));
            response.setContentType(attd.getAttachmentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(attd.getAttachmentName().getBytes("UTF-8"), "ISO8859-1"));
            Common.writefile(response, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("fileata")
    public void downFiles(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
        try {
            AoaAttachmentList attd = fileService.findByAttachmentId(fileid);
            File file = new File(atachrootpath, attd.getAttachmentPath());
            response.setContentLength(Integer.parseInt(attd.getAttachmentSize()));
            response.setContentType(attd.getAttachmentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(attd.getAttachmentName().getBytes("UTF-8"), "ISO8859-1"));
            Common.writefile(response, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("show/**")
    /**
     * @ Description   :  图片预览
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/27 14:39
     * @ Param         :  model
     * @ Param         :  response
     * @ Param         :  userId
     * @ Param         :  request 
     * @ return        :  void
     */
    public void image(Model model, HttpServletResponse response, @SessionAttribute("userId") Long userId, HttpServletRequest request)
            throws IOException {

        String startpath = new String(URLDecoder.decode(request.getRequestURI(), "utf-8"));

        String path = startpath.replace("/show", "");

        File f = new File(imgrootpath, path);
        ServletOutputStream sos = response.getOutputStream();
        FileInputStream input = new FileInputStream(f.getPath());
        byte[] data = new byte[(int) f.length()];
        IOUtils.readFully(input, data);
        // 将文件流输出到浏览器
        IOUtils.write(data, sos);
        input.close();
        sos.close();
    }
    @RequestMapping("showata/**")
    /**
     * @ Description   :  图片预览
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/27 14:39
     * @ Param         :  model
     * @ Param         :  response
     * @ Param         :  userId
     * @ Param         :  request
     * @ return        :  void
     */
    public void images(Model model, HttpServletResponse response, @SessionAttribute("userId") Long userId, HttpServletRequest request)
            throws IOException {

        String startpath = new String(URLDecoder.decode(request.getRequestURI(), "utf-8"));

        String path = startpath.replace("/showata", "");

        File f = new File(atachrootpath, path);
        ServletOutputStream sos = response.getOutputStream();
        FileInputStream input = new FileInputStream(f.getPath());
        byte[] data = new byte[(int) f.length()];
        IOUtils.readFully(input, data);
        // 将文件流输出到浏览器
        IOUtils.write(data, sos);
        input.close();
        sos.close();
    }
    @RequestMapping("down")
    public void dsaf(HttpServletResponse response, HttpServletRequest request) {
        AoaAttachmentList att = null;
        if (StringUtils.isEmpty(request.getParameter("paid")) || request.getParameter("paid") == null
                || request.getParameter("paid").length() == 0) {
        } else {
            Long paid = Long.valueOf(request.getParameter("paid"));
            att = fileService.findByAttachmentId(paid);
        }
        if (StringUtils.isEmpty(request.getParameter("nid")) || request.getParameter("nid") == null
                || request.getParameter("nid").length() == 0) {
        } else {
            Long nid = Long.valueOf(request.getParameter("nid"));
            AoaNoteList note = noteService.findOne(nid);
            att = fileService.findByAttachmentId(note.getAttachId());
        }
        File file = fileService.get(att);
        try {
            // 在浏览器里面显示
            response.setContentLength(Integer.parseInt(att.getAttachmentSize()));
            response.setContentType(att.getAttachmentType());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(att.getAttachmentName().getBytes("UTF-8"), "ISO8859-1"));
            ServletOutputStream sos = response.getOutputStream();
            byte[] data = new byte[Integer.parseInt(att.getAttachmentSize())];
            IOUtils.readFully(new FileInputStream(file), data);
            IOUtils.write(data, sos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
