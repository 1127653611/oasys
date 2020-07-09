package cn.oasys.web.controller.aderss;

import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Set;

@RequestMapping("/")
@Controller
public class AddrController {
    @Autowired
    private UserService userService;
    @RequestMapping("addrmanage")
    public String addrmanage(@SessionAttribute("userId") Long userId, Model model,
                             @RequestParam(value="page",defaultValue="0") int page,
                             @RequestParam(value="size",defaultValue="10") int size) {
        AoaUser user=userService.findOne(userId);

        return "address/addrmanage";
    }
}
