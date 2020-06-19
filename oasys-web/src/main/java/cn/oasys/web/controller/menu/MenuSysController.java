package cn.oasys.web.controller.menu;

import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class MenuSysController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuSysService menuSysService;

    @RequestMapping("testsysmenu")
    public String testsysmenu(HttpServletRequest request) {
        menuSysService.findAllMenuSys(request);
        return "systemcontrol/menumanage";
    }

    @RequestMapping("changeSortId")
    public String changeSortId(HttpServletRequest req, @SessionAttribute("userId") Long userId) {
        AoaUser user = userService.findOne(userId);
        Long parentId = Long.parseLong(req.getParameter("parentid"));
        Long menuId = Long.parseLong(req.getParameter("menuid"));
        Integer sortId = Integer.parseInt(req.getParameter("sortid"));
        Integer arithNum = Integer.parseInt(req.getParameter("num"));
        menuSysService.changeSortId(sortId, arithNum, parentId);
        menuSysService.changeSortId2(sortId, arithNum, menuId);
        return "redirect:/testsysmenu";
    }

    @RequestMapping("menuedit")
    public String menuedit(HttpServletRequest request) {
        if (!StringUtils.isEmpty(request.getAttribute("errormess"))) {
            request.setAttribute("errormess", request.getAttribute("errormess"));
        }
        if (!StringUtils.isEmpty(request.getAttribute("success"))) {
            request.setAttribute("success", request.getAttribute("success"));
        }
        List<AoaSysMenu> parentList=menuSysService.findByParentIdOrderBySortId(0L);
        request.setAttribute("parentList", parentList);
        if (!StringUtils.isEmpty(request.getParameter("id"))) {
            Long getId = Long.parseLong(request.getParameter("id"));
            AoaSysMenu menuObj=menuSysService.findOne(getId);
            if (!StringUtils.isEmpty(request.getParameter("add"))) {
                Long getAdd = menuObj.getMenuId();
                String getName=menuObj.getMenuName();
                request.setAttribute("getAdd", getAdd);
                request.setAttribute("getName", getName);
            }else {
                request.setAttribute("menuObj", menuObj);
            }
        }
        return "systemcontrol/menuedit";
    }
    @RequestMapping("checkMenu")
    public String checkMenu(HttpServletRequest req,AoaSysMenu aoaSysMenu){
        System.out.println(aoaSysMenu);
        if (StringUtils.isEmpty(aoaSysMenu.getParentId())) aoaSysMenu.setParentId(0L);
        if (StringUtils.isEmpty(aoaSysMenu.getMenuName())){
            req.setAttribute("errormess", "名称不能为空");
            return "forward:/menuedit";
        }
        if (StringUtils.isEmpty(aoaSysMenu.getMenuUrl())){
            req.setAttribute("errormess", "路径不能为空");
            return "forward:/menuedit";
        }
        menuSysService.save(aoaSysMenu);
        req.setAttribute("success", "后台验证成功");
        return "forward:/menuedit";
    }
    @RequestMapping("deletethis")
    public String delete(HttpServletRequest req){
        Long menuId=Long.parseLong(req.getParameter("id"));
        menuSysService.deleteThis(menuId);
        return "forward:/testsysmenu";
    }
    @RequestMapping("menutable")
    public String menuTable(HttpServletRequest req){
        if(!StringUtils.isEmpty(req.getParameter("name"))){
            req.setAttribute("oneMenuAll", menuSysService.findByMenuNameLike("%"+req.getParameter("name")+"%"));

        }
        else{
            menuSysService.findAllMenuSys(req);
        }
        return "systemcontrol/menutable";
    }
}
