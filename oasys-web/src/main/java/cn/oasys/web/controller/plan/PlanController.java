package cn.oasys.web.controller.plan;

import cn.oasys.web.common.Utils.Sort;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.plan.AoaPlanList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.plan.PlanService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RequestMapping("/")
@Controller
public class PlanController {
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private PlanService planService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "planview", method = RequestMethod.GET)
    public String test(Model model, HttpSession session,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "baseKey", required = false) String baseKey,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "time", required = false) String time,
                       @RequestParam(value = "icon", required = false) String icon) {
        sortpaging(model, session, page, baseKey, type, status, time, icon);
        return "plan/planview";
    }

    @RequestMapping(value = "planviewtable", method = RequestMethod.GET)
    public String testdd(Model model, HttpSession session,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "baseKey", required = false) String baseKey,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "icon", required = false) String icon) {
        sortpaging(model, session, page, baseKey, type, status, time, icon);
        return "plan/planviewtable";
    }

    private void sortpaging(Model model, HttpSession session, int page, String baseKey, String type, String status,
                            String time, String icon) {
        Sort.setSomething(baseKey, type, status, time, icon, model);
        if (!StringUtils.isEmpty(baseKey)) model.addAttribute("baseKey", baseKey);
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        PageHelper.startPage(page, 10);
        List<AoaPlanList> planLists = planService.paging(baseKey, userid, type, status, time);
        PageInfo<AoaPlanList> page2 = new PageInfo<>(planLists);
        model.addAttribute("plist", planLists);
        model.addAttribute("page", page2);
        model.addAttribute("url", "planviewtable");
    }

    @RequestMapping("planedit")
    public String test3(HttpServletRequest request, Model model) {
        long pid = Long.valueOf(request.getParameter("pid"));

        // 新建
        if (pid == -1) {
            model.addAttribute("pid", pid);
        }
        if (pid > 0) {
            AoaPlanList plan = planService.findOne(pid);
            model.addAttribute("plan", plan);
            model.addAttribute("pid", pid);
        }
        typestatus(model);
        return "plan/planedit";
    }

    @RequestMapping(value = "plansave", method = RequestMethod.POST)
    public String testMess(@RequestParam("file") MultipartFile file, HttpServletRequest req, AoaPlanList plan2) throws IllegalStateException, IOException {
        if (StringUtils.isEmpty(plan2.getTitle())) {
            req.setAttribute("errormess", "标题不能为空");
            return "forward:/planedit";
        }
        if (StringUtils.isEmpty(plan2.getPlanContent())) {
            req.setAttribute("errormess", "计划不能为空");
            return "forward:/planedit";
        }
        AoaAttachmentList att = null;
        Long attid = null;
        long pid = Long.valueOf(req.getParameter("pid"));
        HttpSession session = req.getSession();
        long userid = Long.valueOf(session.getAttribute("userId") + "");
        AoaUser user = userService.findOne(userid);
        if (pid == -1) {
            if (!file.isEmpty()) {
                att = (AoaAttachmentList) fileService.savefile(file, user, null, false);
                attid = att.getAttachmentId();
            } else if (file.isEmpty())
                attid = null;
            plan2.setAttachId(attid);
            plan2.setCreateTime(new Date());
            plan2.setPlanUserId(userid);
            plan2.setPlanComment(null);
            planService.save(plan2);
        }
        if (pid > 0) {
            AoaPlanList plan = planService.findOne(pid);
            if (plan.getAttachId() == null) {
                if (!file.isEmpty()) {
                    att = (AoaAttachmentList) fileService.savefile(file, user, null, false);
                    attid = att.getAttachmentId();
                }
                plan2.setAttachId(attid);
                planService.save(plan2);
            }
            if (plan.getAttachId() != null) {
                fileService.updateatt(file, user, null, plan.getAttachId());
                planService.save(plan2);
            }

        }
        req.setAttribute("success", "后台验证成功");
        return "forward:/planedit";
    }
    @RequestMapping("plandelete")
    public String DSAGec(HttpServletRequest request, HttpSession session) {
        long realuserid = Long.valueOf(session.getAttribute("userId") + "");
        long pid = Long.valueOf(request.getParameter("pid"));
        long userid = planService.findOne(pid).getUser().getUserId();
        if (userid == realuserid) {
            planService.delete(pid);
            return "redirect:/planview";
        } else {
            return "redirect:/notlimit";
        }

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

    private void typestatus(Model model) {
        List<AoaTypeList> type = typeService.findByTypeModel("aoa_plan_list");
        List<AoaStatusList> status = statusService.findByStatusModel("aoa_plan_list");
        model.addAttribute("typelist", type);
        model.addAttribute("statuslist", status);
    }

}
