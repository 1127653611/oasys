package cn.oasys.web.model.dao.discuss;


import cn.oasys.web.model.pojo.discuss.AoaReplyList;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AoaReplyListMapper {
    List<AoaReplyList> findByDiscussId(Long id);

    List<AoaReplyList> findByDiscuss(@Param("id") Long num, @Param("sort") String sort);

    List<AoaReplyList> findByDiscussAndUser(@Param("id") Long num, @Param("uid") Long userId, @Param("sort") String sort);

    AoaReplyList findOne(Long replyId);

    void insertSelective(AoaReplyList aoaReplyList);

    @Delete("delete from aoa_reply_list where reply_id=#{id}")
    void delete(@Param("id") Long discussId);

    @Delete("delete from aoa_reply_list where discuss_id=#{id}")
    void deleteBydiscussId(Long discussId);
}