package cn.oasys.web.service.impl.plan;

import cn.oasys.web.model.dao.plan.AoaPlanListMapper;
import cn.oasys.web.model.pojo.plan.AoaPlanList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.plan.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {
    @Autowired
    private AoaPlanListMapper aoaPlanListMapper;
    @Override
    public List<AoaPlanList> paging(String baseKey, long userid, String type, String status, String time) {
        if (StringUtils.isEmpty(baseKey)) {
            return aoaPlanListMapper.findBySortAndKeyAndUser(type, status, time, null, userid);
        }else {
            return aoaPlanListMapper.findBySortAndKeyAndUser(type, status, time, "%"+baseKey+"%", userid);
        }
    }

    @Override
    public AoaPlanList findOne(long pid) {
        return aoaPlanListMapper.findOne(pid);
    }

    @Override
    public void save(AoaPlanList plan2) {
        if (StringUtils.isEmpty(plan2.getPlanId())){
            aoaPlanListMapper.insertSelective(plan2);
        }else {
            aoaPlanListMapper.updateByPrimaryKeySelective(plan2);
        }
    }

    @Override
    public void delete(long pid) {
        aoaPlanListMapper.delete(pid);
    }

    @Override
    public List<AoaPlanList> findAll() {
        return aoaPlanListMapper.findAll();
    }

    @Override
    public AoaPlanList findlatest(Date start, Date end, Long id, long typeid) {
        return aoaPlanListMapper.findlatest(start,end,id,typeid);
    }

    @Override
    public List<AoaPlanList> findByUserlimit(Long userId) {
        return aoaPlanListMapper.findByUserlimit(userId);
    }
}
