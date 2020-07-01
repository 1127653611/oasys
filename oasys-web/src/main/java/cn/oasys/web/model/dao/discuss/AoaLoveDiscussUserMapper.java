package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaLoveDiscussUser;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaLoveDiscussUserMapper {
    List<AoaLoveDiscussUser>findUsersByDiscussId(Long id);
}