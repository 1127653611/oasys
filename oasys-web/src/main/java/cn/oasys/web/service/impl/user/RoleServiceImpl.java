package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.dao.role.AoaRolePowerListMapper;
import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private AoaSysMenuMapper aoaSysMenuMapper;
    @Autowired
    private AoaRoleMapper aoaRoleMapper;
    @Autowired
    private AoaRolePowerListMapper aoaRolePowerListMapper;
    @Override
    public List<AoaRole> findAll() {
        return aoaRoleMapper.findAll();
    }

    @Override
    public AoaRole findOne(Long roleid) {
        return aoaRoleMapper.findOne(roleid);
    }

    @Override
    public AoaRolePowerList findbyroleidandmenuid(Long roleid, Long menuid) {
        return aoaRolePowerListMapper.findbyroleidandmenuid(roleid,menuid);
    }

    @Override
    public void save(Long pkId, Object i) {
        aoaRolePowerListMapper.update(pkId,i);
    }

    @Override
    public void update(AoaRole role) {
        if (role.getRoleId()==null){
            aoaRoleMapper.insert(role);
            List<AoaSysMenu> aoaSysMenus=aoaSysMenuMapper.findAll();
            for (AoaSysMenu aoaSysMenu: aoaSysMenus){
               aoaRolePowerListMapper.sava(new AoaRolePowerList(null,aoaSysMenu.getMenuId(),role.getRoleId()));
            }
        }else {
            aoaRoleMapper.update(role);
        }
    }

    @Override
    public void delete(Long lid) {
        aoaRolePowerListMapper.deleteThisByRoleId(lid);
        aoaRoleMapper.deleteThis(lid);
    }

    @Override
    public List<AoaRole> findAllByLike(String key) {
        return aoaRoleMapper.findAllByLike(key);
    }
}
