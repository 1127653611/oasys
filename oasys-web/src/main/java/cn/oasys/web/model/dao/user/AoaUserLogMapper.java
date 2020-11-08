package cn.oasys.web.model.dao.user;


import cn.oasys.web.model.pojo.user.AoaUserLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface AoaUserLogMapper {

    List<AoaUserLog> findByUser(@Param("key") String s, @Param("uid") long userid, @Param("time") String time);

    void insertSelective(AoaUserLog uLog);

    @Select("SELECT * from aoa_user_log where user_id=#{id} ORDER BY log_time DESC LIMIT 0,1")
    AoaUserLog findLast(long l);
}