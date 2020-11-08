package cn.oasys.web.controller.inform;

import cn.oasys.web.common.Utils.Sort;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

@RequestMapping("/")
@Controller
public class InformController {
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private NoticeService noticeService;

    @RequestMapping("infrommanage")
    public String inform(@RequestParam(value = "page", defaultValue = "0") int page, @SessionAttribute("userId") Long userId, Model model) {
        PageHelper.startPage(page, 10);
        List<AoaNoticeList> noticeList = noticeService.findByUserId(userId);
        PageInfo<AoaNoticeList> page2 = new PageInfo<>(noticeList);
        List<Map<String, Object>> list = noticeService.fengZhuang(noticeList);
        model.addAttribute("list", list);
        model.addAttribute("page", page2);
        //设置变量，需要load的url；
        model.addAttribute("url", "infrommanagepaging");
        return "inform/informmanage";
    }

    @RequestMapping("infrommanagepaging")
    public String inform(@RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "baseKey", required = false) String baseKey,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "icon", required = false) String icon, @SessionAttribute("userId") Long userId,
                         Model model, HttpServletRequest req) {
        if (!StringUtils.isEmpty(baseKey)) {
            model.addAttribute("baseKey", baseKey);
        }
        Sort.setSomething(baseKey, type, status, time, icon, model);
        PageHelper.startPage(page, 10);
        List<AoaNoticeList> page2 = noticeService.pageThis(userId, baseKey, type, status, time);
        PageInfo<AoaNoticeList> aoaNoticeListPageInfo = new PageInfo<>(page2);
        List<Map<String, Object>> list = noticeService.fengZhuang(page2);
        model.addAttribute("url", "infrommanagepaging");
        model.addAttribute("list", list);
        model.addAttribute("page", aoaNoticeListPageInfo);
        return "inform/informtable";
    }

    @RequestMapping("informedit")
    public String infromEdit(HttpServletRequest req, Model model) {
        List<AoaTypeList> typeList = typeService.findByTypeModel("inform");
        List<AoaStatusList> statusList = statusService.findByStatusModel("inform");
        req.setAttribute("typeList", typeList);
        req.setAttribute("statusList", statusList);
        if (!StringUtils.isEmpty(req.getParameter("id"))) {
            Long noticeId = Long.parseLong(req.getParameter("id"));
            AoaNoticeList noticeList = noticeService.findOne(noticeId);
            model.addAttribute("noticeList", noticeList);
        }
        return "inform/informedit";
    }

    @RequestMapping("informshow")
    public String informShow(HttpServletRequest req, Model model) {
        Long noticeId = Long.parseLong(req.getParameter("id"));
        if (!StringUtils.isEmpty(req.getParameter("read"))) {
            if (("0").equals(req.getParameter("read"))) {
                Long relationId = Long.parseLong(req.getParameter("relationid"));
                AoaNoticeUserRelation relation = noticeService.findOneRe(relationId);
                relation.setIsRead(1);
                noticeService.saveRe(relation);
            }
        }
        AoaNoticeList notice = noticeService.findOne(noticeId);
        AoaUser user = userService.findOne(notice.getUserId());
        model.addAttribute("notice", notice);
        model.addAttribute("userName", user.getUserName());
        return "inform/informshow";
    }

    @RequestMapping("informcheck")
    public String testMess(HttpServletRequest req, AoaNoticeList menu, @SessionAttribute("userId") Long userId) {
        ;
        if (!StringUtils.isEmpty(req.getParameter("top"))) {
            menu.setIsTop(1);
        } else {
            menu.setIsTop(0);
        }
        noticeService.save(menu, userId);
        req.setAttribute("success", "后台验证成功");
        return "forward:/informedit";
    }

    @RequestMapping("infromdelete")
    public String infromDelete(HttpSession session, HttpServletRequest req) {
        Long noticeId = Long.parseLong(req.getParameter("id"));
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaNoticeList notice = noticeService.findOne(noticeId);
        if (!Objects.equals(userId, notice.getUserId())) {
            return "redirect:/notlimit";
        }
        noticeService.deleteOne(noticeId);
        return "redirect:/infrommanage";
    }

    @RequestMapping("infromlist")
    public String infromList(HttpSession session, HttpServletRequest req, Model model,
                             @RequestParam(value = "pageNum", defaultValue = "1") int page) {
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        PageHelper.startPage(page, 10);
        List<Map<String, Object>> list = noticeService.findMyNotice(userId);
        PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(list);
        List<Map<String, Object>> list2 = noticeService.setList(list);
        model.addAttribute("url", "informlistpaging");
        model.addAttribute("list", list2);
        model.addAttribute("page", pageinfo);
        return "inform/informlist";
    }

    @RequestMapping("forwardother")
    public String forwardOther(@SessionAttribute("userId") Long userId, @RequestParam(value = "noticeId") Long noticeId) {
        List<AoaUser> users = userService.findByFatherId(userId);
        AoaNoticeList nl = noticeService.findOne(noticeId);
        List<AoaNoticeUserRelation> nurs = new ArrayList<>();
        for (AoaUser user : users) {
            if (user.getUserId().equals(userId)) {
                continue;
            }
            nurs.add(new AoaNoticeUserRelation(0, nl.getNoticeId(), user.getUserId()));
        }
        noticeService.saves(nurs);
        return "redirect:/infromlist";
    }

    @RequestMapping("informlistdelete")
    public String informListDelete(HttpServletRequest req, HttpSession session) {
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        Long noticeId = Long.parseLong(req.getParameter("id"));
        AoaNoticeUserRelation relation = noticeService.findByUserIdAndNoticeId(userId,noticeId);
        if (Objects.isNull(relation)) {
            return "redirect:/notlimit";
        }
        noticeService.deleteRe(relation);
        return "forward:/infromlist";
    }
    @RequestMapping("informlistpaging")
    public String informListPaging(@RequestParam(value = "pageNum", defaultValue = "1") int page,
                                   @RequestParam(value = "baseKey", required = false) String baseKey,
                                   @RequestParam(value="type",required=false) String type,
                                   @RequestParam(value="status",required=false) String status,
                                   @RequestParam(value="time",required=false) String time,
                                   @RequestParam(value="icon",required=false) String icon,
                                   @SessionAttribute("userId") Long userId,
                                   Model model,HttpServletRequest req){
        if (!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey",baseKey);
        }
        Sort.setSomething(baseKey, type, status, time, icon, model);
        PageHelper.startPage(page, 10);
        List<Map<String, Object>> list=noticeService.sortMyNotice(userId, baseKey, type, status, time);
        PageInfo<Map<String, Object>> pageinfo=new PageInfo<Map<String, Object>>(list);
        List<Map<String, Object>> list2=noticeService.setList(list);
        model.addAttribute("url", "informlistpaging");
        model.addAttribute("list", list2);
        model.addAttribute("page", pageinfo);
        return "inform/informlistpaging";

    }
}
