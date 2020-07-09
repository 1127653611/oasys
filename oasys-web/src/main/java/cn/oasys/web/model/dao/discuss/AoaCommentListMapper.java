package cn.oasys.web.model.dao.discuss;


import cn.oasys.web.model.pojo.discuss.AoaCommentList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaCommentListMapper {

    List<AoaCommentList> findComments(@Param("lon") Long[] replyLong);

    List<AoaCommentList> findByReoly(Long id);

    void insertSelective(AoaCommentList aoaCommentList);

    @Delete("delete from aoa_comment_list where reply_id=#{discussId}")
    void deleteByReply(Long discussId);

    AoaCommentList findone(Long discussId);

    @Delete("delete from aoa_comment_list where comment_id=#{discussId}")
    void delete(Long discussId);

    void deleteByReplys(@Param("lon") Long[] replyLong);
}