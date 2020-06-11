package cn.oasys.web.controller;

import cn.oasys.web.model.pojo.user.AoaUser;
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

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private UserService userService;
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        if(StringUtils.isEmpty(session.getAttribute("userId"))){
            return "login/login";
        }
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        System.out.println(user);
        return "index/index";
    }
}
