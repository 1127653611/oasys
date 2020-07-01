package cn.oasys.web.service.inter.system;

import cn.oasys.web.model.pojo.system.AoaStatusList;

import java.util.List;

public interface StatusService {
    List<AoaStatusList> findAll();

    AoaStatusList findOne(Long statusid);

    void save(AoaStatusList aoaStatusList);

    void deleteStatus(Long statusId);

    List<AoaStatusList> findByStatusNameLikeOrStatusModelLike(String name);

    List<AoaStatusList> findByStatusModel(String inform);
}
