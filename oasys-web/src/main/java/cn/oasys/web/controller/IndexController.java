package cn.oasys.web.controller;

import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        if(StringUtils.isEmpty(session.getAttribute("userId"))){
            return "login/login";
        }
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        menuSysService.findMenuSys(request,user);

        List<AoaNoticeUserRelation> notice=noticeService.findByReadAndUserId(false,userId);
        List<AoaMailReciver> mail=mailService.findByReadAndDelAndReciverId(false, false, userId);
        model.addAttribute("notice", notice.size());
        model.addAttribute("mail", mail.size());
        model.addAttribute("user", user);
        return "index/index";
    }
}
