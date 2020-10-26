package cn.oasys.web.controller.common;

import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
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
}
