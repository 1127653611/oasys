package cn.oasys.web.controller.user;

import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.RoleService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stuxuhai.jpinyin.PinyinException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("usermanage")
    public String usermanage(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageHelper.startPage(page, size);
        List<AoaUser> users = userService.findByidLock(0);
        PageInfo<AoaUser> userspage = new PageInfo<>(users);
        model.addAttribute("users", users);
        model.addAttribute("page", userspage);
        model.addAttribute("url", "usermanagepaging");
        return "user/usermanage";
    }

    @RequestMapping("usermanagepaging")
    public String userPaging(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "usersearch", required = false) String usersearch
    ) {
        PageHelper.startPage(page, size);
        List<AoaUser> users;
        if (StringUtils.isEmpty(usersearch)) {
            users = userService.findByidLock(0);
        } else {
            String key = "%" + usersearch + "%";
            users = userService.findByidLockLike(0, key);
        }
        PageInfo<AoaUser> userspage = new PageInfo<>(users);
        model.addAttribute("users", users);
        model.addAttribute("page", userspage);
        model.addAttribute("url", "usermanagepaging");
        return "user/usermanagepaging";
    }

    @RequestMapping(value = "useredit", method = RequestMethod.GET)
    public String usereditget(@RequestParam(value = "userid", required = false) Long userid, Model model) {
        if (userid != null) {
            AoaUser user = userService.findOne(userid);
            model.addAttribute("where", "xg");
            model.addAttribute("user", user);
        }

        List<AoaDept> depts = deptService.findAll();
        List<AoaPosition> positions = positionService.findAll();
        List<AoaRole> roles = roleService.findAll();

        model.addAttribute("depts", depts);
        model.addAttribute("positions", positions);
        model.addAttribute("roles", roles);
        return "user/edituser";
    }

    @RequestMapping(value = "useredit", method = RequestMethod.POST)
    public String usereditpost(AoaUser user, @RequestParam(value = "isbackpassword", required = false) boolean isbackpassword,
                               Model model) throws PinyinException {
        AoaUser u1 = userService.findOne(user.getUserId());
        boolean istrue = false;
        List<AoaPosition> aoaPositions = positionService.findByDeptid(user.getDeptId());
        for (AoaPosition aoaPosition : aoaPositions) {
            if (aoaPosition.getPositionId() == user.getPositionId()) {
                istrue = true;
            }
        }
        if (!istrue) {
            model.addAttribute("errormess", "部门不职位不匹配，修改失败");
            return "user/edituser";
        }
        AoaPosition aoaPosition = positionService.findOne(user.getPositionId());
        AoaPosition aoaPosition1 = positionService.findOne(u1.getPositionId());
        if (u1.getPositionId() != user.getPositionId()) {
            if (aoaPosition.getName().endsWith("经理") || aoaPosition1.getName().endsWith("经理")) {
                model.addAttribute("errormess", "不能操作经理职位");
                return "user/edituser";
            }
        }
        userService.saveUser(user, isbackpassword);
        model.addAttribute("success", 1);
        return "user/edituser";
    }

    @RequestMapping("deleteuser")
    public String deleteuser(@RequestParam("userid") Long userid, Model model) throws PinyinException {
        AoaUser user = userService.findOne(userid);
        AoaDept dept = deptService.findOne(user.getDeptId());
        if (dept.getDeptmanager() == user.getUserId()) {
            model.addAttribute("errormess", "请先去除经理职位");
            return "user/edituser";
        }
        user.setIsLock(1);

        userService.saveUser(user, false);

        model.addAttribute("success", 1);
        return "user/edituser";

    }

}
