package cn.oasys.web.model.dao.user;

import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaUserLoginRecordMapper {
    int insertSelective(AoaUserLoginRecord record);

    List<AoaUserLoginRecord> getAllById(@Param("id") long userid);

    @Select("select count(*) from aoa_user_login_record where DATE_FORMAT(login_time,'%Y-%m-%d') = #{format}")
    Integer countlog(String format);
}