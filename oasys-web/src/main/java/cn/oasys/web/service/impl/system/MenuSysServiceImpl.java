package cn.oasys.web.service.impl.system;

import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.dao.role.AoaRolePowerListMapper;
import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.system.MenuSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional
public class MenuSysServiceImpl implements MenuSysService {
    @Autowired
    private AoaRoleMapper aoaRoleMapper;
    @Autowired
    private AoaRolePowerListMapper aoaRolePowerListMapper;
    @Autowired
    private AoaSysMenuMapper aoaSysMenuMapper;

    public MenuSysServiceImpl() {
    }

    @Override
    public void findMenuSys(HttpServletRequest request, AoaUser user) {
        List<AoaSysMenu> oneMenuAll=aoaSysMenuMapper.findbyparentxianall(0L, user.getRole().getRoleId(), true,true);
        List<AoaSysMenu> twoMenuAll=aoaSysMenuMapper.findbyparentsxian(0L, user.getRole().getRoleId(), true,true);
        request.setAttribute("oneMenuAll", oneMenuAll);
        request.setAttribute("twoMenuAll", twoMenuAll);
    }

    @Override
    public List<AoaSysMenu> findname( long roleid, boolean bo, boolean le,String val) {
        return aoaSysMenuMapper.findname(roleid,bo,le,val);
    }

    @Override
    public List<AoaSysMenu> findChil(Long id, Long roleid, Boolean bo, Boolean le) {
        return aoaSysMenuMapper.findbyparentxianall(id, roleid, bo,le);
    }

    @Override
    public AoaSysMenu findByChil(Long menuid, Long roleid, Boolean bo, Boolean le) {
        return aoaSysMenuMapper.findByChil(menuid,roleid,bo,le);
    }

    @Override
    public void findAllMenuSys(HttpServletRequest request) {
        //查找所有父菜单
        Iterable<AoaSysMenu> oneMenuAll=aoaSysMenuMapper.findByParentIdOrderBySortId(0L);
        //查找所有子级
        Iterable<AoaSysMenu> twoMenuAll=aoaSysMenuMapper.findByParentIdNotOrderBySortId(0L);
        request.setAttribute("oneMenuAll", oneMenuAll);
        request.setAttribute("twoMenuAll", twoMenuAll);
    }
    //修改其他    @Override
    public void changeSortId(Integer sortId, Integer arithNum, Long parentId) {
        aoaSysMenuMapper.changeSortId(sortId,arithNum,parentId);
    }
    //修改自己
    @Override
    public void changeSortId2(Integer sortId, Integer arithNum, Long menuId) {
        aoaSysMenuMapper.changeSortId2(sortId,arithNum,menuId);
    }

    @Override
    public List<AoaSysMenu> findByParentIdOrderBySortId(long l) {
        return aoaSysMenuMapper.findByParentIdOrderBySortId(l);
    }

    @Override
    public AoaSysMenu findOne(Long getId) {
        return aoaSysMenuMapper.findOne(getId);
    }

    @Override
    public void save(AoaSysMenu aoaSysMenu) {
        if (aoaSysMenu.getMenuId()!=null){
             aoaSysMenuMapper.update(aoaSysMenu);
        }
        else {
            aoaSysMenuMapper.sava(aoaSysMenu);
            List<AoaRole> aoaRoles = aoaRoleMapper.findAll();
            for (AoaRole aoaRole:aoaRoles){
                if (aoaRole.getRoleId()==1){
                    aoaRolePowerListMapper.sava(new AoaRolePowerList(1,aoaSysMenu.getMenuId(),aoaRole.getRoleId()));
                }else {
                    aoaRolePowerListMapper.sava(new AoaRolePowerList(0,aoaSysMenu.getMenuId(),aoaRole.getRoleId()));
                }
            }
        }
    }

    @Override
    public void deleteThis(Long menuId) {
        aoaRolePowerListMapper.deleteThis(menuId);
        aoaSysMenuMapper.deleteThis(menuId);
    }

    @Override
    public List<AoaSysMenu> findByMenuNameLike(String name) {
        return aoaSysMenuMapper.findByMenuNameLike(name);
    }

    @Override
    public List<AoaSysMenu> findByRoleChiledAll(Long id,Long roleid) {
        return aoaSysMenuMapper.findByRoleChiledAll(id,roleid);
    }

    @Override
    public List<AoaSysMenu> findByRoleParentAll(Long id,Long roleid) {
        return aoaSysMenuMapper.findByRoleParentAll(id,roleid);
    }

    @Override
    public void update(AoaSysMenu aoaSysMenu) {
         aoaSysMenuMapper.update(aoaSysMenu);
    }


}
