package cn.oasys.web.model.dao.user;

import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AoaUserLoginRecordMapper {
    int insertSelective(AoaUserLoginRecord record);
}