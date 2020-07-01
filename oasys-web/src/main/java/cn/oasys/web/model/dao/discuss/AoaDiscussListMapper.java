package cn.oasys.web.model.dao.discuss;


import cn.oasys.web.model.pojo.discuss.AoaDiscussList;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AoaDiscussListMapper {

    List<AoaDiscussList> findByTitleLike(@Param("tittle") String s);

    List<AoaDiscussList> findAll();

    List<AoaDiscussList> findByUserAndTitleLike(@Param("userid") Long userid, @Param("tittle") String s);

    List<AoaDiscussList> findByUser(Long userId);

    AoaDiscussList findOne(Long id);

    void updateByPrimaryKeySelective(AoaDiscussList discuss);
}