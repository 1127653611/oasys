package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.List;

public interface DeptService {
    List<AoaDept> findAll();

    AoaDept findOne(Long deptId);

    void save(AoaDept dept);

    void delete(Long deletedeptid);

    void updateManage(Long deptid, Long newmanageid);

    Long getRoleid(AoaUser user);
}
