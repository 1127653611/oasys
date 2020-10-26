package cn.oasys.web.model.dao.notice;

import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaNoticeUserRelationMapper {
    @Select("select COUNT(relatin_id) from aoa_notice_user_relation where is_read=#{read} and relatin_user_id=#{id}")
    Integer findByReadAndUserId(@Param("read") Boolean read, @Param("id") Long id);

    AoaNoticeUserRelation findOne(Long relationId);

    void insertSelective(AoaNoticeUserRelation relation);

    void updateByPrimaryKeySelective(AoaNoticeUserRelation relation);

    List<AoaNoticeUserRelation> findByNoticeId(Long noticeId);

    void delete(@Param("rl") List<AoaNoticeUserRelation> relationList);

    AoaNoticeUserRelation findByUserIdAndNoticeId(@Param("uid") Long userId, @Param("nid") Long noticeId);

    void deleteOne(Long relatinId);
}