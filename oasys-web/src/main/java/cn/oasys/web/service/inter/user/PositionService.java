package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.user.AoaPosition;

import java.util.List;

public interface PositionService {
    List<AoaPosition> findByDeptidAndNameNotLike(long l,String name);

    List<AoaPosition> findByDeptid(Long deptid);

    List<AoaPosition> findByDeptidAndNameLike(Long deptid, String s);

    List<AoaPosition> findAll();

    AoaPosition findOne(Long positionid);

    void save(AoaPosition position);

    void delete(Long positionid);
}
