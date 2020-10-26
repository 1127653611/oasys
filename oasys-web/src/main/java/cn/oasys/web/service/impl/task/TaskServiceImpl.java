package cn.oasys.web.service.impl.task;

import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.task.AoaTaskListMapper;
import cn.oasys.web.model.dao.task.AoaTaskLoggerMapper;
import cn.oasys.web.model.dao.task.AoaTaskUserMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.task.AoaTaskList;
import cn.oasys.web.model.pojo.task.AoaTaskLogger;
import cn.oasys.web.model.pojo.task.AoaTaskUser;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private AoaTaskLoggerMapper aoaTaskLoggerMapper;
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaTaskListMapper aoaTaskListMapper;
    @Autowired
    private AoaTaskUserMapper aoaTaskUserMapper;
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Autowired
    private AoaStatusListMapper aoaStatusListMapper;

    @Override
    public Integer findByUserIdAndStatusId(Long userid, Integer id) {
        return aoaTaskUserMapper.findByUserIdAndStatusId(userid, id);
    }

    @Override
    public List<AoaTaskList> index(String val, Long userId, String type) {
        if (type != null) {
            switch (type) {
                case "类型":
                    return aoaTaskListMapper.findByUsersIdOrderByTypeId(userId, val);
                case "状态":
                    return aoaTaskListMapper.findByUsersIdOrderByStatusAndCancel(userId, val);
                case "发布时间":
                    return aoaTaskListMapper.findByUsersIdOrderByPublishTimeDesc(userId, val);
                default:
                    return aoaTaskListMapper.findByUsersId(userId, val);
            }
        } else {
            return aoaTaskListMapper.findByUsersId(userId, val);
        }
    }

    @Override
    public List<Map<String, Object>> index2(List<AoaTaskList> task, Long userId) {
        AoaUser user = aoaUserMapper.findOneById(userId);
        String username = user.getUserName();
        String deptname = user.getDept().getDeptName();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < task.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            Long statusid = task.get(i).getStatusId().longValue();
            result.put("taskid", task.get(i).getTaskId());
            result.put("typename", aoaTypeListMapper.findname(task.get(i).getTypeId()));
            result.put("statusname", aoaStatusListMapper.findname(statusid));
            result.put("statuscolor", aoaStatusListMapper.findcolor(statusid));
            result.put("title", task.get(i).getTitle());
            result.put("publishtime", task.get(i).getPublishTime());
            result.put("zhiding", task.get(i).getIsTop());
            result.put("cancel", task.get(i).getIsCancel());
            result.put("username", username);
            result.put("deptname", deptname);
            list.add(result);
        }
        return list;
    }

    @Override
    public AoaTaskList findOne(Long ltaskid) {
        return aoaTaskListMapper.findOne(ltaskid);
    }

    @Override
    public List<AoaTaskLogger> findByTaskId(Long ltaskid) {
        return aoaTaskLoggerMapper.findByTaskId(ltaskid);
    }

    @Override
    public void save(AoaTaskLogger logger) {
        if (!StringUtils.isEmpty(logger.getLoggerId())) {
            aoaTaskLoggerMapper.updateByPrimaryKeySelective(logger);
        } else {
            aoaTaskLoggerMapper.insertSelective(logger);
        }
    }

    @Override
    public void updateStatusid(Long taskId, Integer loggerStatusid) {
        aoaTaskListMapper.update(taskId, loggerStatusid);
        aoaTaskUserMapper.update(taskId, loggerStatusid);
    }

    @Override
    public void savetask(AoaTaskList task) {
        aoaTaskListMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public Long findpkId(Long taskId, Long userId) {
        return aoaTaskUserMapper.findpkId(taskId, userId);
    }

    @Override
    public void savetaskUser(AoaTaskUser tasku) {
        if (StringUtils.isEmpty(tasku.getPkId())) {
            aoaTaskUserMapper.insertSelective(tasku);
        } else {
            aoaTaskUserMapper.updateByPrimaryKeySelective(tasku);
        }
    }

    @Override
    public void insert(AoaTaskList tlist) {
        aoaTaskListMapper.insertSelective(tlist);
    }

    @Override
    public void detelelogger(Long ltaskid) {
        List<AoaTaskLogger> taskLogger = aoaTaskLoggerMapper.findByTaskId(ltaskid);
        if (taskLogger.size() != 0) {
            for (AoaTaskLogger tasklogger2 : taskLogger) {
                aoaTaskLoggerMapper.delete(tasklogger2);
            }
        }
    }

    @Override
    public void delete(Long pkid) {
        if (!Objects.isNull(pkid)) {
            aoaTaskUserMapper.delete(pkid);
        }
    }

    @Override
    public void deteletask(AoaTaskList task) {
        aoaTaskListMapper.delete(task.getTaskId());
    }

    @Override
    public void deleteUser(Long ltaskid) {
        aoaTaskUserMapper.deleteUser(ltaskid);
    }

    @Override
    public void deleteUserNoId(List<Long> pklist, Long taskId) {
        aoaTaskUserMapper.deleteUserNoId(pklist, taskId);
    }

    @Override
    public List<Map<String, Object>> index4(List<AoaTaskList> tasklist, Long userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (tasklist != null) {
            for (int i = 0; i < tasklist.size(); i++) {
                Map<String, Object> result = new HashMap<>();
                // 查询任务id
                Long tid = tasklist.get(i).getTaskId();

                // 查询接收人的任务状态id
                Long statusid = aoaTaskUserMapper.findByuserIdAndTaskId(userId, tid);
                // 查询发布人
                AoaUser ptu = aoaUserMapper.findOneById(tasklist.get(i).getTaskPushUserId());
                String username = ptu.getUserName();
                String deptname = ptu.getDept().getDeptName();

                result.put("taskid", tid);
                result.put("typename", aoaTypeListMapper.findname(tasklist.get(i).getTypeId()));
                result.put("statusname", aoaStatusListMapper.findname(statusid));
                result.put("statuscolor", aoaStatusListMapper.findcolor(statusid));
                result.put("title", tasklist.get(i).getTitle());
                result.put("publishtime", tasklist.get(i).getPublishTime());
                result.put("zhiding", tasklist.get(i).getIsTop());
                result.put("cancel", tasklist.get(i).getIsCancel());
                result.put("username", username);
                result.put("deptname", deptname);

                list.add(result);
            }
        }

        return list;
    }

    @Override
    public Long findByuserIdAndTaskId(Long userId, Long ltaskid) {
        return aoaTaskUserMapper.findByuserIdAndTaskId(userId,ltaskid);
    }

    @Override
    public AoaTaskUser findOneUser(Long pkid) {
        return aoaTaskUserMapper.findOne(pkid);
    }

    @Override
    public void updatelistStatus(Long taskId, Integer loggerStatusid) {
        aoaTaskListMapper.update(taskId,loggerStatusid);
    }

    @Override
    public Integer countfinish(long status, Long userId) {
        return aoaTaskListMapper.countfinish(status,userId);
    }


    @Override
    public List<AoaTaskList> index3(Long userId, String key) {
        List<Long> taskid = aoaTaskUserMapper.findByUserId(userId);
        if (StringUtils.isEmpty(key)) {
            return aoaTaskListMapper.findTaskByTaskIds(taskid);
        } else {
            return aoaTaskListMapper.findtaskByTitleLikeAndTaskId(taskid, "%" + key + "%");
        }
    }


}
