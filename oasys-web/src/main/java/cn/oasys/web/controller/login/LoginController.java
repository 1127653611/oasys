package cn.oasys.web.controller.login;

import cn.oasys.web.common.Utils.MD5Util;
import cn.oasys.web.common.Utils.VerifyCodeUtils;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import cn.oasys.web.service.inter.user.UserLoginRecordService;
import cn.oasys.web.service.inter.user.UserService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginRecordService userLoginRecordService;
    public static final String CAPTCHA_KEY = "session_captcha";

    private Random rnd = new Random();

    @RequestMapping(value = "logins", method = RequestMethod.GET)
    public String login() {
        return "login/login";
    }

    @RequestMapping("loginout")
    public String loginout(HttpSession session) {
        session.removeAttribute("userId");
        return "redirect:/logins";
    }

    @RequestMapping(value = "logins", method = RequestMethod.POST)
    public String loginCheck(HttpServletRequest request, HttpSession session, Model model) throws UnknownHostException {
        String userName = request.getParameter("userName").trim();
        String password = request.getParameter("password");
        String ca = request.getParameter("code").toLowerCase();
        String sessioncode = (String) session.getAttribute(CAPTCHA_KEY);
        model.addAttribute("userName", userName);
        if (!ca.equals(sessioncode.toLowerCase())) {
            model.addAttribute("errormess", "验证码输入错误!");
            request.setAttribute("errormess", "验证码输入错误!");
            return "login/login";
        }
        AoaUser user = userService.findOneUser(userName, MD5Util.getMD5String(password));
        if (Objects.isNull(user)) {
            model.addAttribute("errormess", "账号或密码错误!");
            return "login/login";
        }
        if (user.getIsLock() == 1) {
            model.addAttribute("errormess", "账号已被冻结!");
            return "login/login";
        }
        Object sessionId = session.getAttribute("userId");
        if (sessionId == user.getUserId()) {
            model.addAttribute("hasmess", "当前用户已经登录了；不能重复登录");
            session.setAttribute("thisuser", user);

            return "login/login";
        } else {
            session.setAttribute("userId", user.getUserId());
            if (user.getSuperman()==1){
                session.setAttribute("manage",true);
            }else {
                session.setAttribute("manage",false);
            }
            Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
            Version version = browser.getVersion(request.getHeader("User-Agent"));
            String info = browser.getName() + "/" + version.getVersion();
            String ip = InetAddress.getLocalHost().getHostAddress();
            userLoginRecordService.save(new AoaUserLoginRecord(info, ip, new Date(), null, user.getUserId()));
        }
        return "redirect:/index";
    }

    @RequestMapping("handlehas")
    public String handleHas(HttpSession session) {
        if (!StringUtils.isEmpty(session.getAttribute("thisuser"))) {
            AoaUser user = (AoaUser) session.getAttribute("thisuser");
            session.removeAttribute("userId");
            session.setAttribute("userId", user.getUserId());
            if (user.getSuperman()==1){
                session.setAttribute("manage",true);
            }else {
                session.setAttribute("manage",false);
            }
        } else {
            return "login/login";
        }
        return "redirect:/index";
    }

    @RequestMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);

        // 生成图片
        int w = 135, h = 40;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);

        // 将验证码存储在session以便登录时校验
        session.setAttribute(CAPTCHA_KEY, verifyCode.toLowerCase());
    }

}
