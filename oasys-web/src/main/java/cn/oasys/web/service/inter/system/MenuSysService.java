package cn.oasys.web.service.inter.system;

import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MenuSysService  {

    public void findMenuSys(HttpServletRequest request, AoaUser user);

    List <AoaSysMenu> findname(long roleid,boolean bo,boolean le,String val);

    List <AoaSysMenu> findChil( Long id, Long roleid, Boolean bo, Boolean le);

    AoaSysMenu findByChil(Long menuid, Long roleid, Boolean bo, Boolean le);

    void findAllMenuSys(HttpServletRequest request);

    void changeSortId(Integer sortId, Integer arithNum, Long parentId);

    void changeSortId2(Integer sortId, Integer arithNum, Long menuId);

    List<AoaSysMenu> findByParentIdOrderBySortId(long l);

    AoaSysMenu findOne(Long getId);

    void save(AoaSysMenu aoaSysMenu);

    void deleteThis(Long menuId);

    List<AoaSysMenu> findByMenuNameLike(String name);

    List<AoaSysMenu> findByRoleChiledAll(Long id,Long roleid);

    List<AoaSysMenu> findByRoleParentAll(Long id,Long roleid);

    void update(AoaSysMenu aoaSysMenu);
}
