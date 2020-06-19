package cn.oasys.web.model.dao.task;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AoaTaskUserMapper {
    @Select("select COUNT(task_id)from aoa_task_user where task_recive_user_id=#{id} and status_id=#{tid}")
    Integer findByUserIdAndStatusId(@Param("id") Long userid, @Param("tid") Integer id);

}