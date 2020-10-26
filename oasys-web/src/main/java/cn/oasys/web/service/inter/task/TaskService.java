package cn.oasys.web.service.inter.task;

import cn.oasys.web.model.pojo.task.AoaTaskList;
import cn.oasys.web.model.pojo.task.AoaTaskLogger;
import cn.oasys.web.model.pojo.task.AoaTaskUser;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Integer findByUserIdAndStatusId(Long userid,Integer id);

    List<AoaTaskList> index(String val, Long userId,String type);

    List<Map<String, Object>> index2(List<AoaTaskList> tasklist, Long userId);

    AoaTaskList findOne(Long ltaskid);

    List<AoaTaskLogger> findByTaskId(Long ltaskid);

    void save(AoaTaskLogger logger);

    void updateStatusid(Long taskId, Integer loggerStatusid);

    void savetask(AoaTaskList task);

    Long findpkId(Long taskId, Long userId);

    void savetaskUser(AoaTaskUser tasku);

    void insert(AoaTaskList tlist);

    void detelelogger(Long ltaskid);

    void delete(Long pkid);

    void deteletask(AoaTaskList task);

    List<AoaTaskList> index3(Long userId, String key);

    void deleteUser(Long ltaskid);

    void deleteUserNoId(List<Long> pklist, Long taskId);

    List<Map<String, Object>> index4(List<AoaTaskList> aoaTaskLists, Long userId);

    Long findByuserIdAndTaskId(Long userId, Long ltaskid);

    AoaTaskUser findOneUser(Long pkid);


    void updatelistStatus(Long taskId, Integer loggerStatusid);

    Integer countfinish(long status, Long userId);
}
