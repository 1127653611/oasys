package cn.oasys.web.model.dao.discuss;


import cn.oasys.web.model.pojo.discuss.AoaCommentList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaCommentListMapper {

    List<AoaCommentList> findComments(@Param("lon") Long[] replyLong);
}