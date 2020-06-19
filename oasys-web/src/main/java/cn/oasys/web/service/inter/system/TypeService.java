package cn.oasys.web.service.inter.system;

import cn.oasys.web.model.pojo.system.AoaTypeList;

import java.util.List;

public interface TypeService {
    List<AoaTypeList> findAll();

    AoaTypeList findOneById(long typeid);

    void save(AoaTypeList aoaTypeList);

    void deleteType(Long typeId);

    List<AoaTypeList> findByTypeNameLikeOrTypeModelLike(String name);

    List<AoaTypeList> findByTypeModel(String aoa_bursement);
}
