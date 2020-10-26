package cn.oasys.web.controller.role;

import cn.oasys.web.common.Msg;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.user.RoleService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class RoleController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuSysService menuSysService;
    @Autowired
    private RoleService roleService;
    @RequestMapping("rolemanage")
    public String index(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "usersearch", required = false) String usersearch
                                ,Model model){

        PageHelper.startPage(page, size);
        List<AoaRole> aoaRoles = roleService.findAll();

        PageInfo pageInfo=new PageInfo(aoaRoles);
        model.addAttribute("page", pageInfo);
        model.addAttribute("rolelist", aoaRoles);
        return "role/rolemanage";
    }
    @RequestMapping("roleser")
    public String roleser(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          @RequestParam(value = "usersearch", required = false) String usersearch
                            ,Model model){
        PageHelper.startPage(page, size);
        List<AoaRole> aoaRoles;
        if (StringUtils.isEmpty(usersearch)) {
            aoaRoles = roleService.findAll();
        } else {
            String key = "%" + usersearch + "%";
            aoaRoles = roleService.findAllByLike(key);
        }
        PageInfo pageInfo=new PageInfo(aoaRoles);
        model.addAttribute("page", pageInfo);
        model.addAttribute("rolelist", aoaRoles);
        return "role/roletable";
    }
    @RequestMapping("roleset")
    public String index2(HttpServletRequest req, Model model){

        Long roleid=Long.parseLong(req.getParameter("id"));
        AoaRole role=roleService.findOne(roleid);
        List<AoaSysMenu> oneMenuAll=menuSysService.findByRoleParentAll(0L, roleid);
        List<AoaSysMenu> twoMenuAll=menuSysService.findByRoleChiledAll(0L, roleid);
       for (AoaSysMenu aoaSysMenu:oneMenuAll){
            if (aoaSysMenu.getIsShow()!=null &&aoaSysMenu.getIsShow()==1 && aoaSysMenu.getAoaRolePowerList().getIsShow()!=null && aoaSysMenu.getAoaRolePowerList().getIsShow()==1){
                aoaSysMenu.setIsShow(1);
            }else {
                aoaSysMenu.setIsShow(0);
            }
        }
        for (AoaSysMenu aoaSysMenu:twoMenuAll){
            if (aoaSysMenu.getIsShow()!=null && aoaSysMenu.getIsShow()==1 && aoaSysMenu.getAoaRolePowerList().getIsShow()!=null && aoaSysMenu.getAoaRolePowerList().getIsShow()==1){
                aoaSysMenu.setIsShow(1);
            }else {
                aoaSysMenu.setIsShow(0);
            }
        }
        model.addAttribute("oneMenuAll", oneMenuAll);
        model.addAttribute("twoMenuAll", twoMenuAll);
        model.addAttribute("roleid", roleid);
        model.addAttribute("rolename", role.getRoleName());
        return "role/roleset";

    }
    @RequestMapping("powerss")
    public @ResponseBody
    Msg power(HttpServletRequest req){
        Long roleid=Long.parseLong(req.getParameter("roleid"));
        String content=	req.getParameter("content").trim();
        Long menuid=Long.parseLong(	req.getParameter("menuid"));
        AoaSysMenu aoaSysMenu=menuSysService.findOne(menuid);
        if (aoaSysMenu.getIsShow()==null){
            return Msg.fail().add("msg","该菜单已经被关闭，是否重新打开");
        }else {
            AoaRolePowerList rolepower=roleService.findbyroleidandmenuid(roleid,menuid);
            if(content.equals("选中")){
                roleService.save(rolepower.getPkId(),1);
            }else{
                roleService.save(rolepower.getPkId(),null);
            }

            return null;
        }
        //return true;
    }
    @RequestMapping("powerss1")
    public @ResponseBody
    Msg power1(HttpServletRequest req){
        Long roleid=Long.parseLong(req.getParameter("roleid"));
        String content=	req.getParameter("content").trim();
        Long menuid=Long.parseLong(	req.getParameter("menuid"));
        AoaSysMenu aoaSysMenu=menuSysService.findOne(menuid);
        aoaSysMenu.setIsShow(1);
        menuSysService.update(aoaSysMenu);
        AoaRolePowerList rolepower=roleService.findbyroleidandmenuid(roleid,menuid);
        if(content.equals("选中")){
            roleService.save(rolepower.getPkId(),1);
        }else{
            roleService.save(rolepower.getPkId(),null);
        }
        return null;
        //return true;
    }
    @RequestMapping("addrole")
    public String index3(HttpServletRequest req,Model model){


        if(!StringUtil.isEmpty(req.getParameter("id"))){
            long lid =Long.parseLong(req.getParameter("id"));
            AoaRole role=roleService.findOne(lid);
            model.addAttribute("role", role);
        }

        return "role/addrole";
    }
    @RequestMapping("modifyrole")
    public String index4(HttpServletRequest req,  AoaRole role){
      roleService.update(role);
        req.setAttribute("success",1);
        return "role/addrole";
    }
    @RequestMapping("deshan")
    public String index5(HttpServletRequest req, Model model, HttpSession session){
        long userid = (long) session.getAttribute("userId");
        AoaUser  user=userService.findOne(userid);
        Long lid=null;
        if(!StringUtil.isEmpty(req.getParameter("id"))){
            lid=Long.parseLong(req.getParameter("id"));
        }
        if (user.getSuperman()==1){
            List<AoaUser> useist=userService.findByRole(lid);
            if(useist.size()>0){
                model.addAttribute("error", "此角色下还有职员，不允许删除。");
                return "common/proce";
            }else{
                roleService.delete(lid);
                model.addAttribute("success", 1);
                return "role/addrole";
            }
        }else {
            model.addAttribute("error", "只有超级管理员才能操作。");
            return "common/proce";
        }
    }

}
