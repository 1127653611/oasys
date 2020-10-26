package cn.oasys.web.controller.note;

import cn.oasys.web.common.Utils.User;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.note.AoaReceiverNote;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/")
@Controller
public class NoteController {
    @Autowired
    private User u;
    @Autowired
    private ProcessService processService;
    @Autowired
    private FileService fileService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "noteview", method = RequestMethod.GET)
    public String noteview(Model model, HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "baseKey", required = false) String baseKey,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "time", required = false) String time,
                           @RequestParam(value = "icon", required = false) String icon) {
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        List<AoaCatalog> cataloglist = noteService.findcatauser(userid);
        setSomething(baseKey, type, status, time, icon, model, null, null);
        PageHelper.startPage(page, 10);
        List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, null, null, null, type, status, time);
        PageInfo<AoaNoteList> pages = new PageInfo<>(upage);
        model.addAttribute("nlist", upage);
        model.addAttribute("page", pages);
        typestatus(request);
        model.addAttribute("url", "notewrite");
        model.addAttribute("calist", cataloglist);
        return "note/noteview";
    }
    @RequestMapping(value = "noteview", method = RequestMethod.POST)
    public String test3332(HttpServletRequest request, @Param("title") String title, HttpSession session) {
        int flag = 0;
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user = userService.findOne(userid);
        String catalogName = request.getParameter("name");
        if (catalogName != null) {
            List<String> catanamelist = noteService.findcataname(userid);
            for (String caname : catanamelist) {
                if (caname.contains("(") && caname.contains(")"))
                    caname = caname.substring(0, caname.indexOf("("));
                if (caname.equals(catalogName)) {
                    flag++;
                }
            }
            if (flag == 0)
                noteService.savecata(new AoaCatalog(catalogName, 1,user.getUserId()));
            if (flag > 0)
                noteService.savecata(new AoaCatalog(catalogName + "(" + flag + ")",1, user.getUserId()));
        }
        return "redirect:/noteview";
    }

    @RequestMapping("catadelete")
    public String testrwd(Model model, HttpServletRequest request, HttpSession session) {
        long realuserId = Long.valueOf(session.getAttribute("userId") + "");
        String cid = request.getParameter("cid");
        long catalogid = Long.valueOf(cid);
        List<AoaNoteList>  noteList=noteService.findByCatalogId(catalogid);
        // 没有做级联删除 先删除目录下的笔记 再删除目录
        for (AoaNoteList note : noteList) {
            noteService.delete(note.getNoteId());
        }
        noteService.deletecata(catalogid);

        return "redirect:/noteview";
    }

    public void setSomething(String baseKey, String type, String status, String time, String icon, Model model, Long cataid, Long typeid) {

        if (!StringUtils.isEmpty(icon)) {
            model.addAttribute("icon", icon);
            if (!StringUtils.isEmpty(type)) {
                model.addAttribute("type", type);
                setthree("type", type, icon, model, cataid, typeid);
            }
            if (!StringUtils.isEmpty(status)) {
                model.addAttribute("status", status);
                setthree("status", status, icon, model, cataid, typeid);
            }
            if (!StringUtils.isEmpty(time)) {
                model.addAttribute("time", time);
                setthree("time", time, icon, model, cataid, typeid);
            }

        }
        if (StringUtils.isEmpty(icon)) {
            //目录类型查找
            if (!StringUtils.isEmpty(cataid) && !StringUtils.isEmpty(typeid))
                model.addAttribute("sort", "&id=" + cataid + "&typeid=" + typeid);
            //目录单纯查找
            if (!StringUtils.isEmpty(cataid) && StringUtils.isEmpty(typeid))
                model.addAttribute("sort", "&id=" + cataid);
            //单纯类型查找
            if (StringUtils.isEmpty(cataid) && !StringUtils.isEmpty(typeid))
                model.addAttribute("sort", "&typeid=" + typeid);
            else
                model.addAttribute("sort", "&id=" + cataid);
        }

    }

    private void setthree(String x, String name, String icon, Model model, Long cataid, Long typeid) {
        //单纯根据目录
        if (!StringUtils.isEmpty(cataid) && StringUtils.isEmpty(typeid))
            model.addAttribute("sort", "&" + x + "=" + name + "&icon=" + icon + "&id=" + cataid);
        //单纯的根据类型
        if (StringUtils.isEmpty(cataid) && !StringUtils.isEmpty(typeid))
            model.addAttribute("sort", "&" + x + "=" + name + "&icon=" + icon + "&typeid=" + typeid);
        //根据目录和类型
        if (!StringUtils.isEmpty(cataid) && !StringUtils.isEmpty(typeid))
            model.addAttribute("sort", "&" + x + "=" + name + "&icon=" + icon + "&id=" + cataid + "&typeid=" + typeid);
        else if (StringUtils.isEmpty(cataid) && StringUtils.isEmpty(typeid))
            model.addAttribute("sort", "&" + x + "=" + name + "&icon=" + icon);
    }

    private void typestatus(HttpServletRequest request) {
        List<AoaTypeList> type = typeService.findByTypeModel("aoa_note_list");
        List<AoaStatusList> status = statusService.findByStatusModel("aoa_note_list");
        request.setAttribute("typelist", type);
        request.setAttribute("statuslist", status);
    }

    @RequestMapping(value = "notewrite", method = RequestMethod.GET)
    public String test33(Model model, HttpServletRequest request, HttpSession session,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "baseKey", required = false) String baseKey,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "icon", required = false) String icon) {
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        setSomething(baseKey, type, status, time, icon, model, null, null);
        PageHelper.startPage(page, 10);
        List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, null, null, null, type, status, time);
        PageInfo<AoaNoteList> pages = new PageInfo<>(upage);
        typestatus(request);
        if (baseKey != null) {
            //如果有搜索关键字那么就记住它
            request.setAttribute("baseKey", baseKey);
        }
        model.addAttribute("nlist", upage);
        model.addAttribute("page", pages);
        model.addAttribute("url", "notewrite");
        return "note/notewrite";
    }

    @RequestMapping("notecata")
    public String sadf(Model model, HttpServletRequest request, HttpSession session,
                       @RequestParam("id") String cid,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "baseKey", required = false) String baseKey
            , @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "time", required = false) String time,
                       @RequestParam(value = "icon", required = false) String icon
    ) {
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        //不为-2就是按照目录查找
        if (!request.getParameter("id").equals("-2")) {
            Long id = Long.valueOf(cid);
            setSomething(baseKey, type, status, time, icon, model, id, null);
            PageHelper.startPage(page, 10);
            List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, null, id, null, type, status, time);
            PageInfo<AoaNoteList> pageInfo = new PageInfo<>(upage);
            model.addAttribute("nlist", upage);
            model.addAttribute("page", pageInfo);
            model.addAttribute("url", "notecata");
            ////为-2就是按照最近查找
        }
        if (baseKey != null) {
            //如果有搜索关键字那么就记住它
            request.setAttribute("baseKey", baseKey);
        }
        model.addAttribute("catalog", "&id=" + cid);
        request.setAttribute("sort2", "&id=" + cid);
        typestatus(request);
        return "note/notewrite";
    }

    @RequestMapping("notetype")
    public String test43(Model model, HttpServletRequest request, @RequestParam("typeid") Long tid, @RequestParam("id") Long cid, HttpSession session, @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "baseKey", required = false) String baseKey, @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "icon", required = false) String icon
    ) {

        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        if (cid == -2)
            cid = null;
        setSomething(baseKey, type, status, time, icon, model, cid, tid);
        PageHelper.startPage(page, 10);
        List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, null, cid, tid, type, status, time);
        PageInfo<AoaNoteList> pageInfo = new PageInfo<>(upage);
        //获得数据之后就将cid重新设置
        if (cid == null)
            cid = -2l;
        request.setAttribute("sort2", "&id=" + cid + "&typeid=" + tid);
        if (baseKey != null) {
            //如果有搜索关键字那么就记住它
            request.setAttribute("baseKey", baseKey);
        }
        model.addAttribute("nlist", upage);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "notetype");
        model.addAttribute("catalog", "&id=" + cid + "&typeid=" + tid);
        typestatus(request);
        return "note/notewrite";
    }

    @RequestMapping(value = "noteedit")
    public String test4(HttpServletRequest Request, HttpSession session, Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        // 目录
        long userid = Long.valueOf(session.getAttribute("userId") + "");

        List<AoaCatalog> cataloglist = noteService.findcatauser(userid);
        //将根目录放在第一
        if (cataloglist.size() == 0)
            cataloglist.add(noteService.findOneCatelog(33));
        else
            cataloglist.add(0, noteService.findOneCatelog(33));

        String nId = Request.getParameter("id");
        if (nId.contains("cata")) {
            //从目录编辑那里进来的
            String newnid = nId.substring(4, nId.length());
            long ca = Long.valueOf(newnid);
            AoaCatalog cate = noteService.findOneCatelog(ca);
            Request.setAttribute("cata", cate);
            Request.setAttribute("id", -3);
        } else {
            Long nid = Long.valueOf(nId);
            // 新建
            if (nid == -1) {
                // 新建id
                Request.setAttribute("id", nid);
            }

            // 修改
            else if (nid > 0) {
                AoaNoteList note = noteService.findOne(nid);
                long ca = Long.valueOf(note.getCatalogId());
                AoaCatalog cate = noteService.findOneCatelog(ca);
                Request.setAttribute("cata", cate);
                Request.setAttribute("note", note);
                // 修改id
                Request.setAttribute("id", nid);
            }
            // Request.setAttribute("id", nid);
        }
        userget(page, size, model);
        Request.setAttribute("calist", cataloglist);
        typestatus(Request);
        return "note/noteedit";
    }

    public void userget(int page, int size, Model model) {
        PageHelper.startPage(page, size);
        List<AoaUser> pageuser = userService.findAll();
        PageInfo<AoaUser> userPageInfo = new PageInfo<>(pageuser);
        model.addAttribute("page", userPageInfo);
        model.addAttribute("emplist", pageuser);
        model.addAttribute("url", "namereceive");
    }

    @RequestMapping("noteinfo")
    public String test3(@Param("id") String id, HttpServletRequest Request, HttpServletResponse response,
                        HttpSession session) throws IOException {
        AoaAttachmentList attachment = null;
        FileInputStream fis = null;
        OutputStream os = null;
        Long nid = Long.valueOf(id);
        AoaNoteList note = noteService.findOne(nid);
        AoaUser user = userService.findOne(note.getCreatemanId());
        if (note.getAttachId() != null) {
            attachment = fileService.findByAttachmentId(note.getAttachId());
            Request.setAttribute("att", attachment);
        }
        Request.setAttribute("note", note);
        Request.setAttribute("user", user);
        return "note/noteinfo";
    }

    @RequestMapping(value = "notesave", method = RequestMethod.POST)
    public String testdfddf(@RequestParam("file") MultipartFile file, AoaNoteList note2, @SessionAttribute("userId") Long userid,
                            HttpServletRequest request, HttpSession session) throws IllegalStateException, IOException {
        AoaUser user = userService.findOne(userid);
        Set<Long> userss = new HashSet<>();
        userss.add(user.getUserId());
        if (StringUtils.isEmpty(note2.getTitle())) {
            request.setAttribute("errormess", "标题不能为空");
            return "forward:/noteedit";
        }
        if (StringUtils.isEmpty(note2.getNoteId())) {
            if (!file.isEmpty()) {
                AoaAttachmentList att = (AoaAttachmentList) fileService.savefile(file, user, null, false);
                note2.setAttachId(att.getAttachmentId());
            }
            note2.setCreateTime(new Date());
            if (note2.getReceiver().length() > 0) {
                String[] receiver = note2.getReceiver().split(";");
                for (String re : receiver) {
                    AoaUser user2 = userService.findByname(re);
                    userss.add(user2.getUserId());
                }
            }
            note2.setCreatemanId(userid);
        } else {
            if (note2.getTypeId() != 7) {
                note2.setReceiver("");
            }
            if (note2.getAttachId() == null) {
                if (!file.isEmpty()) {
                    AoaAttachmentList att = (AoaAttachmentList) fileService.savefile(file, user, null, false);
                    note2.setAttachId(att.getAttachmentId());
                }
            } else {
                fileService.updateatt(file, user, null, note2.getAttachId());
            }
            if (note2.getReceiver().length() > 0) {
                String[] receiver = note2.getReceiver().split(";");
                for (String re : receiver) {
                    AoaUser user2 = userService.findByname(re);
                    userss.add(user2.getUserId());
                }
            }

        }
        noteService.save(note2, userss);
        request.setAttribute("note2", note2);
        request.setAttribute("success", "后台验证成功");
        return "forward:/noteedit";
    }

    @RequestMapping("collect")
    @ResponseBody
    public Long dsaf(Model model, HttpServletRequest request,
                     HttpSession session, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "baseKey", required = false) String baseKey
            , @RequestParam(value = "type", required = false) String type,
                     @RequestParam(value = "status", required = false) String status,
                     @RequestParam(value = "time", required = false) String time,
                     @RequestParam(value = "icon", required = false) String icon
    ) {
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        String id = request.getParameter("id");
        String iscollected = request.getParameter("iscollected");
        noteService.updatecollect(Long.valueOf(iscollected), Long.valueOf(id));
        return Long.valueOf(iscollected);
    }

    @RequestMapping("collectfind")
    public String dsafdsf(Model model, HttpServletRequest request, @RequestParam("iscollect") String iscollected, @RequestParam("cata") Long cid,
                          HttpSession session, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "baseKey", required = false) String baseKey
            , @RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "time", required = false) String time,
                          @RequestParam(value = "icon", required = false) String icon
    ) {
        if (baseKey != null) {
            //如果有搜索关键字那么就记住它
            request.setAttribute("baseKey", baseKey);
        }
        if (cid == -2)
            cid = null;
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        long collect = Long.valueOf(iscollected);
        if (collect == 1) {
            setSomething(baseKey, type, status, time, icon, model, cid, null);
            PageHelper.startPage(page, 10);
            List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, collect, cid, null, type, status, time);
            PageInfo<AoaNoteList> pages = new PageInfo<>(upage);
            model.addAttribute("url", "collectfind");
            model.addAttribute("nlist", upage);
            model.addAttribute("page", pages);
            //获得数据之后就将cid重新设置
            if (cid == null)
                cid = -2l;
            model.addAttribute("sort", "&iscollect=" + collect + "&cata=" + cid);
            model.addAttribute("sort2", "&iscollect=" + collect + "&cata=" + cid);
            model.addAttribute("collect", 0);
        } else if (collect == 0) {
            setSomething(baseKey, type, status, time, icon, model, cid, null);
            PageHelper.startPage(page, 10);
            List<AoaNoteList> upage = noteService.sortpage(baseKey, userid, null, cid, null, type, status, time);
            PageInfo<AoaNoteList> pages = new PageInfo<>(upage);
            model.addAttribute("url", "notewrite");
            model.addAttribute("nlist", upage);
            model.addAttribute("page", pages);
            model.addAttribute("sort", "&userid=" + userid);
            model.addAttribute("sort2", "&userid=" + userid);
            model.addAttribute("collect", 1);
        }

        typestatus(request);
        return "note/notewrite";
    }

    @RequestMapping("notedelete")
    public String testrw(Model model, HttpServletRequest request, HttpSession session) {
        long realuserId = Long.valueOf(session.getAttribute("userId") + "");
        String nid = request.getParameter("nid");
        long noteid = Long.valueOf(nid);
        AoaReceiverNote u = noteService.finduserid(noteid, realuserId);
        if (u != null) {
            AoaNoteList note = noteService.findOne(noteid);
            if (note.getTypeId() == 7) {
                noteService.deleteRe(u, note);
            }
            //如果笔记的类型不是共享类型的就直接删除
            else
                noteService.delete(noteid);
            return "redirect:/noteview";
        } else {
            return "redirect:/notlimit";

        }

    }

    @RequestMapping("notesomedelete")
    public String dsafds(HttpServletRequest request, HttpSession session) {
        long realuserId = Long.valueOf(session.getAttribute("userId") + "");
        String sum = request.getParameter("sum");
        String[] strings = sum.split(";");
        for (String s : strings) {
            long noteids = Long.valueOf(s);
            AoaReceiverNote u = noteService.finduserid(noteids, realuserId);
            AoaNoteList note = noteService.findOne(noteids);
            if (note.getTypeId() == 7) {
                noteService.deleteRe(u, note);
            }
            //如果笔记的类型不是共享类型的就直接删除
            else
                noteService.delete(noteids);
        }
        return "redirect:/noteview";
    }
}
