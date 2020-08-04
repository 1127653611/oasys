package cn.oasys.web.controller;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.task.TaskService;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuSysService menuSysService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaskService taskService;
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        if(StringUtils.isEmpty(session.getAttribute("userId"))){
            return "login/login";
        }
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        menuSysService.findMenuSys(request,user);

        int notice=noticeService.findByReadAndUserId(false,userId);
        int mail=mailService.findByReadAndDelAndReciverId(false, false, userId);
        int task=taskService.findByUserIdAndStatusId(userId,3);
        model.addAttribute("task", task);
        model.addAttribute("notice", notice);
        model.addAttribute("mail", mail);
        model.addAttribute("user", user);
        return "index/index";
    }
    @RequestMapping("menucha")
    public String menuecha(HttpSession session, Model model,HttpServletRequest req){
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        String val=null;
        if (!StringUtils.isEmpty(req.getParameter("val")))
            val=req.getParameter("val");
        if (!StringUtils.isEmpty(val)){
            List<AoaSysMenu> MenuAll=menuSysService.findname( user.getRole().getRoleId(), true, true, val);
            Set<AoaSysMenu> oneMenuAll=new HashSet<>();
            Set<AoaSysMenu> twoMenuAll=new HashSet<>();
            for (AoaSysMenu aoaSysMenu:MenuAll) {
                if (aoaSysMenu.getParentId()==0){
                    oneMenuAll.add(aoaSysMenu);
                    twoMenuAll.addAll(menuSysService.findChil(aoaSysMenu.getMenuId(),user.getRole().getRoleId(),true,true));
                }else {
                    twoMenuAll.add(aoaSysMenu);
                    oneMenuAll.add(menuSysService.findByChil(aoaSysMenu.getParentId(), user.getRole().getRoleId(),true,true));
                }
            }
            req.setAttribute("oneMenuAll", oneMenuAll);
            req.setAttribute("twoMenuAll", twoMenuAll);
        }
        else {
            menuSysService.findMenuSys(req,user);
        }
        return "common/leftlists";
    }
    @RequestMapping("userlogs")
    public String usreLog(@SessionAttribute("userId") Long userId, HttpServletRequest req){
        List<AoaUserLog> userLogs=userService.findLogByUser(userId);
        req.setAttribute("userLogList", userLogs);
        return "user/userlog";
    }
    @RequestMapping("test2")
    public String test2(HttpSession session, Model model, HttpServletRequest request){
        return "systemcontrol/control";
    }
    @RequestMapping("notlimit")
    public String notLimit() {
        return "common/notlimit";
    }
}
