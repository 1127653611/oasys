package cn.oasys.web.model.dao.process;


import cn.oasys.web.model.pojo.process.AoaProcessList;
import cn.oasys.web.model.pojo.process.AoaReviewed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaReviewedMapper {

    int insertSelective(AoaReviewed record);

    List<AoaReviewed> findByUserIdAndSort(@Param("key") String key, @Param("uid") Long userId,@Param("del") Boolean isdel);

    AoaReviewed findByProIdAndUserId(@Param("pid") Long processId, @Param("uid") Long userId);

    List<AoaReviewed> findByReviewedTimeNotNullAndProId(Long processid);

    void updateByPrimaryKeySelective(AoaReviewed re);
}