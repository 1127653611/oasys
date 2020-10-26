package cn.oasys.web.service.inter.plan;

import cn.oasys.web.model.pojo.plan.AoaPlanList;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;
import java.util.List;

public interface PlanService {
    List<AoaPlanList> paging(String baseKey, long userId, String type, String status, String time);

    AoaPlanList findOne(long pid);

    void save(AoaPlanList plan2);

    void delete(long pid);

    List<AoaPlanList> findAll();

    AoaPlanList findlatest(Date start, Date end, Long uid, long typeid);

    List<AoaPlanList> findByUserlimit(Long userId);
}
