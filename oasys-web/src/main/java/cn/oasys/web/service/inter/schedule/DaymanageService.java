package cn.oasys.web.service.inter.schedule;

import cn.oasys.web.model.pojo.process.AoaProcessList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleUser;

import java.util.List;

public interface DaymanageService {
    public List<AoaScheduleUser> aboutmeschedule(Long userId);

    List<AoaScheduleList> findByUser(Long userId,String key);

    AoaScheduleList findOne(Long rcid);

    void save(AoaScheduleList scheduleList);

    void saveUser(AoaScheduleUser aoaScheduleUser);

    void deleteUser(Long rcid,List<Long> longs);

    void delete(AoaScheduleList rc);

    List<AoaScheduleList> findstart(long userid);
}
