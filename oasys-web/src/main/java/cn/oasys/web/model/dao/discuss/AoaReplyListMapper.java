package cn.oasys.web.model.dao.discuss;


import cn.oasys.web.model.pojo.discuss.AoaReplyList;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AoaReplyListMapper {
    Set<AoaReplyList> findByDiscussId(Long id);

    List<AoaReplyList> findByDiscuss(@Param("id") Long num, @Param("sort") String sort);

    List<AoaReplyList> findByDiscussAndUser(@Param("id") Long num, @Param("uid") Long userId, @Param("sort") String pa);
}