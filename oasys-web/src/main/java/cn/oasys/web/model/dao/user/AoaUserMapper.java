package cn.oasys.web.model.dao.user;

import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AoaUserMapper {
    public AoaUser findOneUserByname(@Param("username") String userName, @Param("password") String password);
    public AoaUser findOneById(@Param("id")Long id);
}