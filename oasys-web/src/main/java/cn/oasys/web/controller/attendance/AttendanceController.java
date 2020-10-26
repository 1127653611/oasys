package cn.oasys.web.controller.attendance;

import cn.oasys.web.common.Utils.Sort;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
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
        List<AoaUser> users = userService.findByFatherId(one.getFatherId());
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
        if (!StringUtils.isEmpty(starttime))
            startdate = simpleDateFormat.parse(starttime);
        if (!StringUtils.isEmpty(endtime))
            enddate = simpleDateFormat.parse(endtime);
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
        if(startdate!=null&&enddate!=null)
        {start=startdate;end=enddate;}
        if(startdate==null&&enddate==null)
            startdate=start;enddate=end;
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
                        @RequestParam(value = "baseKey", required = false) String baseKey) {
        monthtablepaging(request, model, session, page, baseKey);
        return "attendce/monthtable";
    }

    private void monthtablepaging(HttpServletRequest request, Model model, HttpSession session, int page, String baseKey) {

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
            offnum = attendanceService.countoffwork(month, user.getUserId());
            //当月该用户上班次数
            toworknum = attendanceService.counttowork(month, user.getUserId());
            for (long statusId = 10; statusId < 13; statusId++) {
                //这里面记录了正常迟到早退等状态
                if (statusId == 12)
                    result.add(attendanceService.countnum(month, statusId, user.getUserId()) + toworknum - offnum);
                else
                    result.add(attendanceService.countnum(month, statusId, user.getUserId()));
            }
            //添加请假和出差的记录//应该是查找 使用sql的sum（）函数来统计出差和请假的次数
            int hday=0;int chucai=0;
            if (attendanceService.countothernum(month, 46L, user.getUserId()) != null){
                hday=attendanceService.countothernum(month, 46L, user.getUserId());
            }
                result.add(hday);
            if (attendanceService.countothernum(month, 47L, user.getUserId()) != null){
                chucai=attendanceService.countothernum(month, 47L, user.getUserId());
            }
                result.add(chucai);
            //这里记录了旷工的次数 还有请假天数没有记录 旷工次数=30-8-请假次数-某天签到次数
            //这里还有请假天数没有写
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String date_month = sdf.format(date);
            if (month != null) {
                if (month.compareTo(date_month) >= 0)
                    result.add(0);
                else
                    result.add(30 - 8 - offnum-chucai-hday);
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
                        @RequestParam(value = "baseKey", required = false) String baseKey) {
        monthtablepaging(request, model, session, page, baseKey);
        return "attendce/realmonthtable";
    }
    @RequestMapping(value="attendcelist",method=RequestMethod.GET)
    public String test(HttpServletRequest request,  Model model,HttpSession session,
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
        if (!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey",baseKey);
        }
        Sort.setSomething(baseKey,type,status,time,icon,model);
        PageHelper.startPage(page,10);
        List<AoaAttendsList> page2 = attendanceService.singlepage(baseKey, userid,type, status, time);
        PageInfo<AoaAttendsList> aoaAttendsListPageInfo=new PageInfo<>(page2);
        typestatus(request);
        request.setAttribute("alist", page2);
        request.setAttribute("page", aoaAttendsListPageInfo);
        request.setAttribute("url", "attendcelisttable");
    }
    @RequestMapping(value="attendcelisttable",method=RequestMethod.GET)
    public String testdf(HttpServletRequest request,  Model model,HttpSession session,
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
