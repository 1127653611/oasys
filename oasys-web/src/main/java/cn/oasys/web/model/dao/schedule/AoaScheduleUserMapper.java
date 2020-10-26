package cn.oasys.web.model.dao.schedule;


import cn.oasys.web.model.pojo.schedule.AoaScheduleUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaScheduleUserMapper {

    void insert(AoaScheduleUser aoaScheduleUser);

    void deleteUser(@Param("rcid") Long rcid,@Param("lon") List<Long> longs);

    @Delete("delete from aoa_schedule_user where rcid = #{id}")
    void delete(Long rcId);

    List<AoaScheduleUser> findByUserId(Long userId);
}