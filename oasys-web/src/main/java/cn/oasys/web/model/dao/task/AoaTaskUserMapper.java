package cn.oasys.web.model.dao.task;

import cn.oasys.web.model.pojo.task.AoaTaskList;
import cn.oasys.web.model.pojo.task.AoaTaskUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaTaskUserMapper {
    @Select("select COUNT(task_id)from aoa_task_user where task_recive_user_id=#{id} and status_id=#{tid}")
    Integer findByUserIdAndStatusId(@Param("id") Long userid, @Param("tid") Integer id);

    @Update("update aoa_task_user set status_id=#{lo} where task_id=#{ta}")
    void update(@Param("ta") Long taskId, @Param("lo") Integer loggerStatusid);

    @Select("select pk_id from aoa_task_user where task_id = #{tid} and task_recive_user_id = #{uid}")
    Long findpkId(@Param("tid") Long taskId, @Param("uid") Long userId);

    void updateByPrimaryKeySelective(AoaTaskUser tasku);

    void insertSelective(AoaTaskUser tasku);

    @Delete("delete from aoa_task_user where pk_id=#{id}")
    void delete(Long pkid);

    @Select("select task_id from aoa_task_user where task_recive_user_id=#{id}")
    List<Long> findByUserId(Long userId);

    @Delete("delete from aoa_task_user where task_id=#{id}")
    void deleteUser(Long ltaskid);

    void deleteUserNoId(@Param("pklist") List<Long> pklist, @Param("tid") Long taskId);

    @Select("select status_id from aoa_task_user where task_id=#{tid} and task_recive_user_id = #{uid}")
    Long findByuserIdAndTaskId(@Param("uid") Long userId, @Param("tid") Long tid);

    AoaTaskUser findOne(Long pkid);

    @Update("update aoa_task_user set status_id=#{sid} where pk_id=#{pid}")
    void updatestatus(@Param("pid") Long pkId, @Param("sid") Long statusId);
}