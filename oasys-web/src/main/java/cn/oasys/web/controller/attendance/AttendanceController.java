package cn.oasys.web.controller.attendance;

import cn.oasys.web.common.Common;
import cn.oasys.web.common.Utils.Sort;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.attendce.AttendanceService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AttendanceController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;
    @Autowired
    private AttendanceService attendanceService;
    Date start, end;
    String month_;

    @RequestMapping("singin")
    public String Datag(HttpSession session, Model model, HttpServletRequest request) throws InterruptedException, UnknownHostException {
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user = userService.findOne(userId);
        //首先获取ip
        InetAddress ia = null;
        ia = ia.getLocalHost();
        String attendip = ia.getHostAddress();
        // 时间规范
        // 状态默认是正常
        long typeId, statusId = 10;
        AoaAttendsList attends = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String nowdate = sdf.format(date);
        // 星期 判断该日期是星期几
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEEE");
        // 截取时分
        SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
        // 截取时分秒
        SimpleDateFormat sdf5 = new SimpleDateFormat("HH:mm:ss");
        AoaDept dept = user.getDept();
        String start = sdf5.format(dept.getStartTime());
        String end = sdf5.format(dept.getEndTime());
        // 一周当中的星期几
        String weekofday = sdf3.format(date);
        // 时分
        String hourmin = sdf4.format(date);

        // 时分秒
        String hourminsec = sdf5.format(date);
        Long aid = null;

        // 查找用户当天的所有记录
        Integer count = attendanceService.countrecord(nowdate, userId);
        if (hourminsec.compareTo("23:59:00") > 0) {
            // 在17之后签到无效
            model.addAttribute("error", "1");
        }
        if (hourminsec.compareTo("05:00:00") < 0) {
            //在凌晨5点之前不能签到
            model.addAttribute("error", "2");
        } else if ((hourminsec.compareTo("05:00:00") > 0) && (hourminsec.compareTo("23:59:00") < 0)) {
            // 明确一点就是一个用户一天只能产生两条记录
            if (count == 0) {
                if (hourminsec.compareTo(end) < 0) {
                    // 没有找到当天的记录就表示此次点击是上班 就是用来判断该记录的类型
                    // 上班id8
                    typeId = 8;
                    // 上班就只有迟到和正常
                    if (hourminsec.compareTo(start) > 0) {
                        // 迟于规定时间 迟到
                        statusId = 11;
                    } else if (hourminsec.compareTo(start) < 0) {
                        statusId = 10;
                    }
                    attends = new AoaAttendsList(attendip, null, date, statusId, typeId, userId, hourmin, weekofday, (double) 0, null);
                    attendanceService.save(attends);
                } else {
                    model.addAttribute("error", "已经下班了");
                    return "common/proce";
                }
            }
            if (count == 1) {
                // 找到当天的一条记录就表示此次点击是下班
                // 下班id9
                typeId = 9;
                // 下班就只有早退和正常
                if (hourminsec.compareTo(end) > 0) {
                    // 在规定时间晚下班正常
                    statusId = 10;
                } else if (hourminsec.compareTo(end) < 0) {
                    // 在规定时间早下班早退
                    statusId = 12;
                }
                attends = new AoaAttendsList(attendip, null, date, statusId, typeId, userId, hourmin, weekofday, 0d, null);
                attendanceService.save(attends);
            }
            if (count >= 2) {
                // 已经是下班的状态了 大于2就是修改考勤时间了
                // 下班id9
                if (hourminsec.compareTo(end) > 0) { // 最进一次签到在规定时间晚下班正常
                    statusId = 10;
                } else if (hourminsec.compareTo(end) < 0) {
                    // 最后一次签到在规定时间早下班早退
                    statusId = 12;
                }
                aid = attendanceService.findoffworkid(nowdate, userId);
                AoaAttendsList attends2 = attendanceService.findOne(aid);
                attends2.setAttendsIp(attendip);
                attendanceService.save(attends2);
                attendanceService.updatetime(date, hourmin, statusId, aid);
                AoaAttendsList aList = attendanceService.findlastest(nowdate, userId);
            }
        }
        // 显示用户当天最新的记录
        AoaAttendsList aList = attendanceService.findlastest(nowdate, userId);
        if (aList != null) {
            long type=aList.getTypeId();
            model.addAttribute("type", type);
        }
        model.addAttribute("alist", aList);
        return "systemcontrol/signin";
    }

    @RequestMapping("attendceatt")
    public String testdasf(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "baseKey", required = false) String baseKey,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "time", required = false) String time,
                           @RequestParam(value = "icon", required = false) String icon, Model model) {
        allsortpaging(request, session, page, baseKey, type, status, time, icon, model);
        return "attendce/attendceview";
    }

    private void allsortpaging(HttpServletRequest request, HttpSession session, int page, String baseKey, String type, String status, String time, String icon, Model model) {
        Sort.setSomething(baseKey, type, status, time, icon, model);
        if (!StringUtils.isEmpty(baseKey)) {
            model.addAttribute("baseKey", baseKey);
        }
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        List<Long> ids = new ArrayList<>();
        AoaUser one = userService.findOne(userId);
        List<AoaUser> users = userService.findByFatherId(one.getUserId());
        for (AoaUser user : users) {
            ids.add(user.getUserId());
        }
        if (ids.size() == 0) {
            ids.add(0L);
        }
        PageHelper.startPage(page, 10);
        List<AoaAttendsList> page2 = attendanceService.paging(baseKey, ids, type, status, time);
        PageInfo<AoaAttendsList> aoaAttendsListPageInfo = new PageInfo<>(page2);
        request.setAttribute("alist", page2);
        request.setAttribute("page", aoaAttendsListPageInfo);
        request.setAttribute("url", "attendcetable");

    }

    @RequestMapping("attendcetable")
    public String table(HttpServletRequest request, HttpSession session,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "baseKey", required = false) String baseKey,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "time", required = false) String time,
                        @RequestParam(value = "icon", required = false) String icon, Model model) {
        allsortpaging(request, session, page, baseKey, type, status, time, icon, model);
        return "attendce/attendcetable";
    }

    @RequestMapping("attendceedit")
    public String test4(@Param("aid") String aid, Model model, HttpServletRequest request, HttpSession session) {
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        if (aid == null) {
            model.addAttribute("write", 0);
        } else if (aid != null) {
            long id = Long.valueOf(aid);
            AoaAttendsList attends = attendanceService.findOne(id);
            model.addAttribute("write", 1);
            model.addAttribute("attends", attends);
        }
        typestatus(request);
        return "attendce/attendceedit";
    }

    @RequestMapping(value = "attendcesave", method = RequestMethod.POST)
    /**
     * @ Description   :  修改考勤
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/14 2:54
     * @ Param         :  model
     * @ Param         :  session
     * @ Param         :  request 
     * @ return        :  java.lang.String
     */
    public String updateAtt(Model model, HttpSession session, HttpServletRequest request) {
        Long userid = Long.parseLong(session.getAttribute("userId") + "");
        String remark = request.getParameter("remark");
        String status = request.getParameter("status");
        System.out.println(status);
        Long statusid = Long.parseLong(status);
        long id = Long.parseLong(request.getParameter("id"));
        AoaAttendsList attends = attendanceService.findOne(id);
        attends.setAttendsRemark(remark);
        attends.setStatusId(statusid);
        attendanceService.save(attends);
        return "redirect:/attendceatt";
    }

    @RequestMapping("attdelete")
    /**
     * @ Description   :  删除考勤
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/14 3:07
     * @ Param         :  request
     * @ Param         :  session 
     * @ return        :  java.lang.String
     */
    public String dsfa(HttpServletRequest request, HttpSession session) {
        long aid = Long.valueOf(request.getParameter("aid"));
        attendanceService.delete(aid);
        return "redirect:/attendceatt";
    }

    @RequestMapping("attendceweek")
    /**
     * @ Description   :  周报表
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/14 3:10
     * @ Param         :  request
     * @ Param         :  session
     * @ Param         :  page
     * @ Param         :  baseKey 
     * @ return        :  java.lang.String
     */
    public String test3(HttpServletRequest request, HttpSession session,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "baseKey", required = false) String baseKey) throws ParseException {
        weektablepaging(request, session, page, baseKey);
        return "attendce/weektable";
    }

    private void weektablepaging(HttpServletRequest request, HttpSession session, int page, String baseKey) throws ParseException {
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        // 格式转化
        Date startdate = null;
        Date enddate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isEmpty(starttime)) {
            startdate = simpleDateFormat.parse(starttime);

        }
        if (!StringUtils.isEmpty(endtime)) {
            Calendar calendar = Calendar.getInstance();
            enddate = simpleDateFormat.parse(endtime);
            calendar.setTime(enddate);
            calendar.add(Calendar.HOUR, 23);
            calendar.add(Calendar.MINUTE, 59);
            calendar.add(Calendar.SECOND, 59);
            enddate = calendar.getTime();
        }
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        List<Long> ids = new ArrayList<>();
        PageHelper.startPage(page, 10);
        List<AoaUser> userspage = userService.findmyemployuser(baseKey, userId);
        PageInfo<AoaUser> userPageInfo = new PageInfo<>(userspage);
        for (AoaUser user : userspage) {
            ids.add(user.getUserId());
        }
        if (ids.size() == 0) {
            ids.add(0L);
        }
        if (startdate != null && enddate != null) {
            start = startdate;
            end = enddate;
        }
        if (startdate == null && enddate == null)
            startdate = start;
        enddate = end;
        List<AoaAttendsList> alist = attendanceService.findoneweek(startdate, enddate, ids);
        for (AoaUser user : userspage) {
            Set<AoaAttendsList> attenceset = new HashSet<>();
            for (AoaAttendsList attence : alist) {
                if (Objects.equals(attence.getAttendsUserId(), user.getUserId())) {
                    attenceset.add(attence);
                }
            }
            user.setaSet(attenceset);
        }
        String[] weekday = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        request.setAttribute("ulist", userspage);
        request.setAttribute("page", userPageInfo);
        request.setAttribute("weekday", weekday);
        request.setAttribute("url", "realweektable");
    }

    @RequestMapping("realweektable")
    public String dsaf(HttpServletRequest request, HttpSession session,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "baseKey", required = false) String baseKey, Model model) throws ParseException {
        if (StringUtils.isEmpty(baseKey))
            model.addAttribute("baseKey", baseKey);
        weektablepaging(request, session, page, baseKey);
        return "attendce/realweektable";

    }

    @RequestMapping("attendcemonth")
    /**
     * @ Description   :  月报表
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/15 0:07
     * @ Param         :  request
     * @ Param         :  model
     * @ Param         :  session
     * @ Param         :  page
     * @ Param         :  baseKey 
     * @ return        :  java.lang.String
     */
    public String test2(HttpServletRequest request, Model model, HttpSession session,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "baseKey", required = false) String baseKey) throws ParseException {
        monthtablepaging(request, model, session, page, baseKey);
        return "attendce/monthtable";
    }

    private void monthtablepaging(HttpServletRequest request, Model model, HttpSession session, int page, String baseKey) throws ParseException {
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        PageHelper.startPage(page, 10);
        List<AoaUser> userspage = userService.findmyemployuser(baseKey, userId);
        PageInfo<AoaUser> userPageInf = new PageInfo<>(userspage);
        String month = request.getParameter("month");
        if (month != null)
            month_ = month;
        else
            month = month_;

        for (AoaUser user : userspage) {
            Integer offnum, toworknum;
            List<Integer> result = new ArrayList<>();
            //当月该用户下班次数
            offnum = attendanceService.countoffwork("%" + month + "%", user.getUserId());
            //当月该用户上班次数
            toworknum = attendanceService.counttowork("%" + month + "%", user.getUserId());
            for (long statusId = 10; statusId < 13; statusId++) {
                //这里面记录了正常迟到早退等状态
                if (statusId == 12)
                    result.add(attendanceService.countnum(month, statusId, user.getUserId()) + toworknum - offnum);
                else
                    result.add(attendanceService.countnum(month, statusId, user.getUserId()));
            }
            //添加请假和出差的记录//应该是查找 使用sql的sum（）函数来统计出差和请假的次数
            int hday = 0;
            int chucai = 0;
            if (attendanceService.countothernum("%" + month + "%", 46L, user.getUserId()) != null) {
                hday = attendanceService.countothernum("%" + month + "%", 46L, user.getUserId());
            }
            result.add(hday);
            if (attendanceService.countothernum(month, 47L, user.getUserId()) != null) {
                chucai = attendanceService.countothernum("%" + month + "%", 47L, user.getUserId());
            }
            result.add(chucai);
            //这里记录了旷工的次数 还有请假天数没有记录 旷工次数=30-8-请假次数-某天签到次数
            //这里还有请假天数没有写
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String date_month = sdf.format(date);
            if (month != null) {

                if (month.compareTo(date_month) >= 0) {
                    result.add(0);
                } else {
                    Date holiday = sdf.parse(month);
                    List<Integer> integers = Common.getHoliday(holiday);
                    result.add(integers.get(1) - integers.get(0) - offnum - hday - chucai);
                }
            }

            user.setuMap(result);
        }

        model.addAttribute("ulist", userspage);
        model.addAttribute("page", userPageInf);
        model.addAttribute("url", "realmonthtable");
    }

    @RequestMapping("realmonthtable")
    public String dfshe(HttpServletRequest request, Model model, HttpSession session,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "baseKey", required = false) String baseKey) throws ParseException {
        monthtablepaging(request, model, session, page, baseKey);
        return "attendce/realmonthtable";
    }

    @RequestMapping(value = "attendcelist", method = RequestMethod.GET)
    public String test(HttpServletRequest request, Model model, HttpSession session,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "baseKey", required = false) String baseKey,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "time", required = false) String time,
                       @RequestParam(value = "icon", required = false) String icon) {
        signsortpaging(request, model, session, page, baseKey, type, status, time, icon);
        return "attendce/attendcelist";
    }

    private void signsortpaging(HttpServletRequest request, Model model, HttpSession session, int page, String baseKey, String type, String status, String time, String icon) {
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        if (!StringUtils.isEmpty(baseKey)) {
            model.addAttribute("baseKey", baseKey);
        }
        Sort.setSomething(baseKey, type, status, time, icon, model);
        PageHelper.startPage(page, 10);
        List<AoaAttendsList> page2 = attendanceService.singlepage(baseKey, userid, type, status, time);
        PageInfo<AoaAttendsList> aoaAttendsListPageInfo = new PageInfo<>(page2);
        typestatus(request);
        request.setAttribute("alist", page2);
        request.setAttribute("page", aoaAttendsListPageInfo);
        request.setAttribute("url", "attendcelisttable");
    }

    @RequestMapping(value = "attendcelisttable", method = RequestMethod.GET)
    public String testdf(HttpServletRequest request, Model model, HttpSession session,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "baseKey", required = false) String baseKey,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "icon", required = false) String icon) {
        signsortpaging(request, model, session, page, baseKey, type, status, time, icon);
        return "attendce/attendcelisttable";
    }

    private void typestatus(HttpServletRequest request) {
        List<AoaTypeList> type = typeService.findByTypeModel("aoa_attends_list");
        List<AoaStatusList> status = statusService.findByStatusModel("aoa_attends_list");
        request.setAttribute("typelist", type);
        request.setAttribute("statuslist", status);
    }

}
