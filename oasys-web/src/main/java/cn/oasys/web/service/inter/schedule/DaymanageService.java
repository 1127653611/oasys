package cn.oasys.web.service.inter.schedule;

import cn.oasys.web.model.pojo.schedule.AoaScheduleList;

import java.util.List;

public interface DaymanageService {
    public List<AoaScheduleList> aboutmeschedule(Long userId);
}
