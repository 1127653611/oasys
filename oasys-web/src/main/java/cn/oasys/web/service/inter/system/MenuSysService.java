package cn.oasys.web.service.inter.system;

import cn.oasys.web.model.pojo.user.AoaUser;

import javax.servlet.http.HttpServletRequest;

public interface MenuSysService  {
    public void findMenuSys(HttpServletRequest request, AoaUser user);
}
