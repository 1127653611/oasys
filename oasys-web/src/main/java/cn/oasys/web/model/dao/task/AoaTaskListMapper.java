package cn.oasys.web.model.dao.task;

import cn.oasys.web.model.pojo.task.AoaTaskList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaTaskListMapper {
    List<AoaTaskList> findByUsersIdOrderByTypeId(@Param("uid") Long userId, @Param("key") String val);

    List<AoaTaskList> findByUsersIdOrderByStatusAndCancel(@Param("uid") Long userId, @Param("key") String val);

    List<AoaTaskList> findByUsersIdOrderByPublishTimeDesc(@Param("uid") Long userId, @Param("key") String val);

    List<AoaTaskList> findByUsersId(@Param("uid") Long userId, @Param("key") String val);

    AoaTaskList findOne(Long ltaskid);

    @Update("update aoa_task_list set status_id=#{lo} where task_id=#{ta}")
    void update(@Param("ta") Long taskId, @Param("lo") Integer loggerStatusid);

    void updateByPrimaryKeySelective(AoaTaskList task);

    void insertSelective(AoaTaskList tlist);

    @Delete("delete from aoa_task_list where task_id=#{id}")
    void delete(Long taskId);

    List<AoaTaskList> findTaskByTaskIds(@Param("taskid") List<Long> taskid);

    List<AoaTaskList> findtaskByTitleLikeAndTaskId(@Param("taskid") List<Long> taskid, @Param("key") String key);

    @Select("select count(*) from aoa_task_list where status_id = #{sid} and task_push_user_id = #{uid}")
    Integer countfinish(@Param("sid") long status, @Param("uid") Long userId);
}
