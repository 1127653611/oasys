package cn.oasys.web.model.dao.plan;


import cn.oasys.web.model.pojo.plan.AoaPlanList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AoaPlanListMapper {

    List<AoaPlanList> findBySortAndKeyAndUser(@Param("type") String type, @Param("status") String status, @Param("time") String time, @Param("key") String baseKey, @Param("uid") long userid);

    AoaPlanList findOne(long pid);

    void insertSelective(AoaPlanList plan2);

    void updateByPrimaryKeySelective(AoaPlanList plan2);

    @Delete("delete from aoa_plan_list where plan_id = #{id}")
    void delete(long pid);

    List<AoaPlanList> findAll();

    AoaPlanList findlatest(@Param("start") Date start, @Param("end") Date end, @Param("uid") Long uid, @Param("tid") long typeid);

    List<AoaPlanList> findByUserlimit(Long userId);
}