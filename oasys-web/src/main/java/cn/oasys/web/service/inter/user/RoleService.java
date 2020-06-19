package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;

import java.util.List;

public interface RoleService {
    List<AoaRole> findAll();

    AoaRole findOne(Long roleid);

    AoaRolePowerList findbyroleidandmenuid(Long roleid, Long menuid);

    void save(Long pkId, Object i);

    void update(AoaRole role);

    void delete(Long lid);

    List<AoaRole> findAllByLike(String key);
}
