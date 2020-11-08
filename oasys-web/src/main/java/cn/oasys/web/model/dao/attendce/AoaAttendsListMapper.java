package cn.oasys.web.model.dao.attendce;


import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface AoaAttendsListMapper {
    int insertSelective(AoaAttendsList record);

    int updateByPrimaryKeySelective(AoaAttendsList record);

    List<AoaAttendsList> findBySortAndKeyAndUser(@Param("key") String key, @Param("ids") List<Long> ids, @Param("type") String type, @Param("status") String status, @Param("time") String time);

    AoaAttendsList findOne(long id);

    void delete(long aid);

    List<AoaAttendsList> findoneweek(@Param("start") Date startdate, @Param("end") Date enddate, @Param("ids") List<Long> ids);

    @Select("select count(*) from aoa_attends_list where DATE_FORMAT(attends_time,'%Y-%m') like #{month} and attends_user_id = #{uid} and type_id = 9")
    Integer countoffwork(@Param("month") String month,@Param("uid") Long userId);

    @Select("select count(*) from aoa_attends_list where DATE_FORMAT(attends_time,'%Y-%m') like #{month} and attends_user_id = #{uid} and type_id = 8")
    Integer counttowork(@Param("month") String month,@Param("uid") Long userId);

    @Select("select count(*) from aoa_attends_list where DATE_FORMAT(attends_time,'%Y-%m') like #{month} and attends_user_id = #{uid} and status_id = #{statusid}")
    Integer countnum(@Param("month") String month, @Param("statusid") long statusId, @Param("uid") Long userId);

    @Select("select count(*) from aoa_attends_list where DATE_FORMAT(attends_time,'%Y-%m') like #{month} and attends_user_id = #{uid} and type_id = #{typeid}")
    Integer countothernum(@Param("month") String month, @Param("typeid") long typeid, @Param("uid") Long userId);

    List<AoaAttendsList> findBySortAndKeyAndUserId(@Param("key") String key, @Param("uid") Long userId, @Param("type") String type, @Param("status") String status, @Param("time") String time);

    AoaAttendsList findlastest(@Param("date") String nowdate, @Param("uid") Long userId);

    @Select("SELECT COUNT(*) from aoa_attends_list  WHERE DATE_format(attends_time,'%Y-%m-%d') like #{date} and attends_user_id=#{uid}")
    Integer countrecord(@Param("date") String nowdate, @Param("uid") Long userId);

    @Select("select attends_id from aoa_attends_list  WHERE DATE_format(attends_time,'%Y-%m-%d') like #{date} and attends_user_id=#{uid} and type_id=9")
    Long findoffworkid(@Param("date") String nowdate, @Param("uid") Long userId);
}