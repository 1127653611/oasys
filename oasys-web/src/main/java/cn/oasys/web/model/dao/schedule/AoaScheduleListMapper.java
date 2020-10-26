package cn.oasys.web.model.dao.schedule;


import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaScheduleListMapper {

    List<AoaScheduleList> findByUserId(@Param("uid") Long userId,@Param("key")String key);

    AoaScheduleList findOne(Long rcid);

    void insertSelective(AoaScheduleList scheduleList);

    void updateByPrimaryKeySelective(AoaScheduleList scheduleList);

    @Delete("delete from aoa_schedule_list where rc_id = #{id}")
    void delete(Long rcId);

}