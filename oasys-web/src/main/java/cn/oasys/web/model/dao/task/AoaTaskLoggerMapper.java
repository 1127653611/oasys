package cn.oasys.web.model.dao.task;

import cn.oasys.web.model.pojo.task.AoaTaskLogger;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaTaskLoggerMapper {

    List<AoaTaskLogger> findByTaskId(Long ltaskid);

    void updateByPrimaryKeySelective(AoaTaskLogger logger);

    void insertSelective(AoaTaskLogger logger);

    @Delete("delete from aoa_task_logger where logger_id=#{loggerId}")
    void delete(AoaTaskLogger tasklogger2);
}