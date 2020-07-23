package cn.oasys.web.controller.note;

import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/")
@Controller
public class NoteController {
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
        List<AoaNoteList> upage = noteService.sortpage(page, baseKey, userid, null, null, null, type, status, time);
        PageInfo<AoaNoteList> pages = new PageInfo<>(upage);
        model.addAttribute("nlist", upage);
        model.addAttribute("page", pages);
        typestatus(request);
        model.addAttribute("url", "notewrite");
        model.addAttribute("calist", cataloglist);
        return "note/noteview";
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
        List<AoaNoteList> upage = noteService.sortpage(page, baseKey, userid, null, null, null, type, status, time);
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
            List<AoaNoteList> upage = noteService.sortpage(page, baseKey, userid, null, id, null, type, status, time);
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
        System.out.println(cid);
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        if (cid == -2)
            cid = null;
        setSomething(baseKey, type, status, time, icon, model, cid, tid);
        PageHelper.startPage(page, 10);
        List<AoaNoteList> upage = noteService.sortpage(page, baseKey, userid, null, cid, tid, type, status, time);
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
        //查看用户并分页
        List<AoaUser> userlist = userService.findAll();
        PageInfo<AoaUser> pageuser = new PageInfo<>(userlist);
        // 查询部门表

        model.addAttribute("page", pageuser);
        model.addAttribute("emplist", userlist);
        model.addAttribute("url", "namereceive");
    }

    @RequestMapping("namereceive")
    public String serch(Model model, HttpServletRequest req, @SessionAttribute("userId") Long userId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        String baseKey = null;
        if (!StringUtil.isEmpty(req.getParameter("baseKey"))) {
            baseKey = req.getParameter("baseKey").trim();
        }
        PageHelper.startPage(page, size);
        List<AoaUser> userlist = null;
        if (StringUtil.isEmpty(baseKey)) {
            //查看用户并分页
            userlist = userService.findAll();
        } else {
            userlist = userService.findAllByLike(baseKey);
        }
        PageInfo<AoaUser> pageuser = new PageInfo<>(userlist);
        model.addAttribute("emplist", userlist);
        model.addAttribute("page", pageuser);
        model.addAttribute("url", "namereceive");

        return "common/noterecivers";

    }
}
