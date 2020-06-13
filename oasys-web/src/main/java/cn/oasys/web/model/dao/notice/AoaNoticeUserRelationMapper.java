package cn.oasys.web.model.dao.notice;

import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaNoticeUserRelationMapper {

    List<AoaNoticeUserRelation> findByReadAndUserId(@Param("read") Boolean read, @Param("id") Long id);
}