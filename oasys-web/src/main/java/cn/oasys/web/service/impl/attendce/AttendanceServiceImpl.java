package cn.oasys.web.service.impl.attendce;

import cn.oasys.web.model.dao.attendce.AoaAttendsListMapper;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.service.inter.attendce.AttendanceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @ Description   :  TODO
 * @ Author        :  wang
 * @ CreateDate    :  2020/10/13 23:12
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AoaAttendsListMapper aoaAttendsListMapper;
    @Override
    public List<AoaAttendsList> paging(String baseKey, List<Long> ids, String type, String status, String time) {
        if (StringUtils.isEmpty(baseKey)){
            return aoaAttendsListMapper.findBySortAndKeyAndUser(null,ids,type,status,time);
        }else {
            return aoaAttendsListMapper.findBySortAndKeyAndUser("%"+baseKey+"%",ids,type,status,time);
        }
    }

    @Override
    public AoaAttendsList findOne(long id) {
        return aoaAttendsListMapper.findOne(id);
    }

    @Override
    public void save(AoaAttendsList attends) {
        if (StringUtils.isEmpty(attends.getAttendsId())){
            aoaAttendsListMapper.insertSelective(attends);
        }else {
            aoaAttendsListMapper.updateByPrimaryKeySelective(attends);
        }
    }

    @Override
    public void delete(long aid) {
        aoaAttendsListMapper.delete(aid);
    }

    @Override
    public List<AoaAttendsList> findoneweek(Date startdate, Date enddate, List<Long> ids) {
        return aoaAttendsListMapper.findoneweek(startdate,enddate,ids);
    }

    @Override
    public Integer countoffwork(String month, Long userId) {
        return aoaAttendsListMapper.countoffwork(month,userId);
    }

    @Override
    public Integer counttowork(String month, Long userId) {
        return aoaAttendsListMapper.counttowork(month,userId);
    }

    @Override
    public Integer countnum(String month, long statusId, Long userId) {
        return aoaAttendsListMapper.countnum(month,statusId,userId);
    }

    @Override
    public Integer countothernum(String month, long typeid, Long userId) {
        return aoaAttendsListMapper.countothernum(month,typeid,userId);
    }

    @Override
    public List<AoaAttendsList> singlepage(String baseKey, Long userid, String type, String status, String time) {
        if (StringUtils.isEmpty(baseKey)){
            return aoaAttendsListMapper.findBySortAndKeyAndUserId(null,userid,type,status,time);
        }else {
            return aoaAttendsListMapper.findBySortAndKeyAndUserId("%"+baseKey+"%",userid,type,status,time);
        }
    }

    @Override
    public AoaAttendsList findlastest(String nowdate, Long userId) {
        return aoaAttendsListMapper.findlastest("%"+nowdate+"%",userId);
    }

    @Override
    public Integer countrecord(String nowdate, Long userId) {
        return aoaAttendsListMapper.countrecord("%"+nowdate+"%",userId);
    }

    @Override
    public Long findoffworkid(String nowdate, Long userId) {
        return aoaAttendsListMapper.findoffworkid("%"+nowdate+"%",userId);
    }

    @Override
    public void updatetime(Date date, String hourmin, long statusId, Long aid) {
        AoaAttendsList aoaAttendsList=aoaAttendsListMapper.findOne(aid);
        aoaAttendsList.setAttendsTime(date);
        aoaAttendsList.setAttendHmtime(hourmin);
        aoaAttendsList.setStatusId(statusId);
        aoaAttendsListMapper.updateByPrimaryKeySelective(aoaAttendsList);
    }
}
