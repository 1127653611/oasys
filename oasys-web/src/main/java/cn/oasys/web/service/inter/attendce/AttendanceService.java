package cn.oasys.web.service.inter.attendce;

import cn.oasys.web.model.pojo.attendce.AoaAttendsList;

import java.util.Date;
import java.util.List;

public interface AttendanceService {
    List<AoaAttendsList> paging(String baseKey, List<Long> ids, String type, String status, String time);

    AoaAttendsList findOne(long id);

    void save(AoaAttendsList attends);

    void delete(long aid);

    List<AoaAttendsList> findoneweek(Date startdate, Date enddate, List<Long> ids);

    Integer countoffwork(String month, Long userId);

    Integer counttowork(String month, Long userId);

    Integer countnum(String month, long statusId, Long userId);

    Integer countothernum(String month, long typeid, Long userId);

    List<AoaAttendsList> singlepage(String baseKey, Long userid, String type, String status, String time);

    AoaAttendsList findlastest(String nowdate, Long userId);
}
