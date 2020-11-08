package cn.oasys.web.common.intercepter;

import cn.oasys.web.common.Utils.application;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.user.RoleService;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class recordInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (!StringUtils.isEmpty(session.getAttribute("userId"))) {
            //导入dao类
            UserService userService = application.applicationContext.getBean(UserService.class);
            MenuSysService menuSysService = application.applicationContext.getBean(MenuSysService.class);
            Long uid = Long.parseLong(session.getAttribute("userId") + "");
            AoaUser user = userService.findOne(uid);
            List<AoaSysMenu> oneMenuAll = menuSysService.findChil(0L, user.getRole().getRoleId(), true, false);
            List<AoaSysMenu> twoMenuAll = menuSysService.findbyparentsxian(0L, user.getRole().getRoleId(), true, false);
            List<AoaSysMenu> all = new ArrayList<>();
            //获取当前访问的路径
            String url = request.getServletPath();
            String zhuan = "notlimit";
            if (oneMenuAll.size() > 0) {
                all.addAll(oneMenuAll);
            }
            if (twoMenuAll.size() > 0) {
                all.addAll(twoMenuAll);
            }
            for (AoaSysMenu rolemenu : all) {
                if (rolemenu.getMenuUrl().equals(url)) {
                    request.getRequestDispatcher(zhuan).forward(request, response);
                    break;
                }
            }

        } else {
            response.sendRedirect("logins");
            return false;
        }

        return super.preHandle(request, response, handler);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex)
            throws Exception {
        HttpSession session=request.getSession();
        //导入dao类
        UserService userService=application.applicationContext.getBean(UserService.class);
        MenuSysService menuSysService = application.applicationContext.getBean(MenuSysService.class);
        AoaUserLog uLog= new AoaUserLog();
        //首先就获取ip
        InetAddress ia=null;
        ia=ia.getLocalHost();
        String ip=ia.getHostAddress();
        uLog.setIpAddr(ip);
        uLog.setUrl(request.getServletPath());
        uLog.setLogTime(new Date());
        Long id = (Long) session.getAttribute("userId");
        if(StringUtils.isEmpty(id)){
            return ;
        }
        uLog.setUserId(id);
        //从菜单表里面匹配
        List<AoaSysMenu> sMenus= menuSysService.findAll();
        AoaUserLog aoaUserLog=userService.findByUserlaset(id);
        if (aoaUserLog==null){
            uLog.setTitle(menuSysService.findbyUrl(request.getServletPath()).getMenuName());
            userService.savelog(uLog);
        }
        for (AoaSysMenu systemMenu : sMenus) {
            if(systemMenu.getMenuUrl().equals(request.getServletPath())){
                //只有当该记录的路径不等于第一条的时候
                if(!aoaUserLog.getUrl().equals(systemMenu.getMenuUrl())){
                    uLog.setTitle(systemMenu.getMenuName());
                    //只要匹配到一个保存咯
                    userService.savelog(uLog);
                }
            }
        }
    }
}
