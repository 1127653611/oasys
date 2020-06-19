package cn.oasys.web.service.impl.task;

import cn.oasys.web.model.dao.task.AoaTaskUserMapper;
import cn.oasys.web.service.inter.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private AoaTaskUserMapper aoaTaskUserMapper;
    @Override
    public Integer findByUserIdAndStatusId(Long userid, Integer id) {
        return aoaTaskUserMapper.findByUserIdAndStatusId(userid,id);
    }
}
