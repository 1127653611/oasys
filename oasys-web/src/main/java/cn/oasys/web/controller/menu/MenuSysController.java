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
        Long parentId = Long.parseLong(req.getParameter("parentid"));
        Long menuId = Long.parseLong(req.getParameter("menuid"));
        Integer sortId = Integer.parseInt(req.getParameter("sortid"));
        Integer arithNum = Integer.parseInt(req.getParameter("num"));
        if (arithNum == 1) {
            List<AoaSysMenu> aoaSysMenus = menuSysService.findBySortIdlt(parentId, sortId);
            if (aoaSysMenus.isEmpty()) {
                return "redirect:/testsysmenu";
            } else {
                menuSysService.upSort(aoaSysMenus, menuId);
            }
        } else if (arithNum == -1) {
            List<AoaSysMenu> aoaSysMenus = menuSysService.findBySortIdgt(parentId, sortId);
            if (aoaSysMenus.isEmpty()) {
                return "redirect:/testsysmenu";
            } else {
                menuSysService.downSort(aoaSysMenus, menuId);
            }
        }
        return "redirect:/testsysmenu";
    }

    @RequestMapping("menuedit")
    public String menuedit(HttpServletRequest request) {
        List<AoaSysMenu> parentList = menuSysService.findByParentIdOrderBySortId(0L);
        request.setAttribute("parentList", parentList);
        if (!StringUtils.isEmpty(request.getParameter("id"))) {
            Long getId = Long.parseLong(request.getParameter("id"));
            AoaSysMenu menuObj = menuSysService.findOne(getId);
            if (!StringUtils.isEmpty(request.getParameter("add"))) {
                Long getAdd = menuObj.getMenuId();
                String getName = menuObj.getMenuName();
                request.setAttribute("getAdd", getAdd);
                request.setAttribute("getName", getName);
            } else {
                request.setAttribute("menuObj", menuObj);
            }
        }
        return "systemcontrol/menuedit";
    }

    @RequestMapping("checkMenu")
    public String checkMenu(HttpServletRequest req, AoaSysMenu aoaSysMenu) {
        if (StringUtils.isEmpty(aoaSysMenu.getParentId())) aoaSysMenu.setParentId(0L);
        if (StringUtils.isEmpty(aoaSysMenu.getMenuName())) {
            req.setAttribute("errormess", "名称不能为空");
            return "forward:/menuedit";
        }
        if (StringUtils.isEmpty(aoaSysMenu.getMenuUrl())) {
            req.setAttribute("errormess", "路径不能为空");
            return "forward:/menuedit";
        }
        menuSysService.save(aoaSysMenu);
        req.setAttribute("success", "后台验证成功");
        return "forward:/menuedit";
    }

    @RequestMapping("deletethis")
    public String delete(HttpServletRequest req) {
        Long menuId = Long.parseLong(req.getParameter("id"));
        AoaSysMenu aoaSysMenu = menuSysService.findOne(menuId);
        if (aoaSysMenu.getMenuId() == 1 || aoaSysMenu.getParentId() == 1) {
            req.setAttribute("msgg", 1);
            return "forward:/notlimit";
        }
        menuSysService.deleteThis(menuId);
        return "forward:/testsysmenu";
    }

    @RequestMapping("menutable")
    public String menuTable(HttpServletRequest req) {
        if (!StringUtils.isEmpty(req.getParameter("name"))) {
            req.setAttribute("oneMenuAll", menuSysService.findByMenuNameLike("%" + req.getParameter("name") + "%"));

        } else {
            menuSysService.findAllMenuSys(req);
        }
        return "systemcontrol/menutable";
    }
}
