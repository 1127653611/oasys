package cn.oasys.web.service.impl.system;

import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.system.MenuSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public class MenuSysServiceImpl implements MenuSysService {
    @Autowired
    private AoaSysMenuMapper aoaSysMenuMapper;
    @Override
    public void findMenuSys(HttpServletRequest request, AoaUser user) {
        System.out.println(user);
        List<AoaSysMenu> oneMenuAll=aoaSysMenuMapper.findbyparentxianall(0L, user.getRole().getRoleId(), true,true);
        List<AoaSysMenu> twoMenuAll=aoaSysMenuMapper.findbyparentsxian(0L, user.getRole().getRoleId(), true,true);
        request.setAttribute("oneMenuAll", oneMenuAll);
        request.setAttribute("twoMenuAll", twoMenuAll);
    }
}
