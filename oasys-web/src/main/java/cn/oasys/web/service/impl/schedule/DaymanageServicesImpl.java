package cn.oasys.web.service.impl.schedule;

import cn.oasys.web.model.dao.schedule.AoaScheduleListMapper;
import cn.oasys.web.model.dao.schedule.AoaScheduleUserMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleUser;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.schedule.DaymanageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DaymanageServicesImpl implements DaymanageService {
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaScheduleUserMapper aoaScheduleUserMapper;
    @Autowired
    private AoaScheduleListMapper aoaScheduleListMapper;
    @Override
    public List<AoaScheduleUser> aboutmeschedule(Long userId) {
      return aoaScheduleUserMapper.findByUserId(userId);
    }

    @Override
    public List<AoaScheduleList> findByUser(Long userId,String key) {
        if (StringUtils.isEmpty(key)) {
            return aoaScheduleListMapper.findByUserId(userId,null);
        }else {
            return aoaScheduleListMapper.findByUserId(userId,"%"+key+"%");
        }
    }

    @Override
    public AoaScheduleList findOne(Long rcid) {
        return aoaScheduleListMapper.findOne(rcid);
    }

    @Override
    public void save(AoaScheduleList scheduleList) {
        if (StringUtils.isEmpty(scheduleList.getRcId())){
            aoaScheduleListMapper.insertSelective(scheduleList);
        }else {
            aoaScheduleListMapper.updateByPrimaryKeySelective(scheduleList);
        }
    }

    @Override
    public void saveUser(AoaScheduleUser aoaScheduleUser) {
        aoaScheduleUserMapper.insert(aoaScheduleUser);
    }

    @Override
    public void deleteUser(Long rcid,List<Long> longs) {
        aoaScheduleUserMapper.deleteUser(rcid,longs);
    }

    @Override
    public void delete(AoaScheduleList rc) {
        aoaScheduleUserMapper.delete(rc.getRcId());
        aoaScheduleListMapper.delete(rc.getRcId());
    }

    @Override
    public List<AoaScheduleList> findstart(long userid) {
        return aoaScheduleListMapper.findByUserId(userid,null);
    }
}
