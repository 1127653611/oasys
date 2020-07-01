package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaLoveUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaLoveUserMapper {
    List<AoaLoveUser> findUsersByReptyId(Long id);
}