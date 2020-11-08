package cn.oasys.web.controller;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.plan.AoaPlanList;
import cn.oasys.web.model.pojo.process.AoaProcessList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleUser;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import cn.oasys.web.service.inter.adress.AdressService;
import cn.oasys.web.service.inter.attendce.AttendanceService;
import cn.oasys.web.service.inter.discuss.DiscussService;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.plan.PlanService;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.schedule.DaymanageService;
import cn.oasys.web.service.inter.system.MenuSysService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.task.TaskService;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private PlanService planService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private DaymanageService daymanageService;
    @Autowired
    private MenuSysService menuSysService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaskService taskService;
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        menuSysService.findMenuSys(request,user);
        List<AoaScheduleUser> aboutmenotice = daymanageService.aboutmeschedule(userId);
        for (AoaScheduleUser scheduleList : aboutmenotice) {
            if(scheduleList.getAoaScheduleList().getIsreminded()!=null && scheduleList.getAoaScheduleList().getIsreminded()==0){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//24小时制
//				simpleDateFormat.parse(scheduleList.getStartTime()).getTime();
                String start = simpleDateFormat.format(scheduleList.getAoaScheduleList().getStartTime());
                String now = simpleDateFormat.format(new Date());
                try {
                    long now2 = simpleDateFormat.parse(now).getTime();
                    long start2 = simpleDateFormat.parse(start).getTime();
                    long cha = start2-now2;
                    if(0<cha && cha <86400000){
                        AoaNoticeList remindnotices = new AoaNoticeList();
                        remindnotices.setTypeId(11l);
                        remindnotices.setStatusId(15l);
                        remindnotices.setTitle("您有一个日程即将开始");
                        remindnotices.setUrl("/daycalendar");
                        remindnotices.setUserId(userId);
                        remindnotices.setNoticeTime(new Date());
                        noticeService.save(remindnotices,userId);

                        scheduleList.getAoaScheduleList().setIsreminded(1);
                        daymanageService.save(scheduleList.getAoaScheduleList());
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        int notice=noticeService.findByReadAndUserId(false,userId);
        int mail=mailService.findByReadAndDelAndReciverId(false, false, userId).size();
        int task=taskService.findByUserIdAndStatusId(userId,3);
        model.addAttribute("task", task);
        model.addAttribute("notice", notice);
        model.addAttribute("mail", mail);
        model.addAttribute("user", user);
        return "index/index";
    }
    @RequestMapping("menucha")
    public String menuecha(HttpSession session, Model model,HttpServletRequest req){
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        String val=null;
        if (!StringUtils.isEmpty(req.getParameter("val")))
            val=req.getParameter("val");
        if (!StringUtils.isEmpty(val)){
            List<AoaSysMenu> MenuAll=menuSysService.findname( user.getRole().getRoleId(), true, true, val);
            Set<AoaSysMenu> oneMenuAll=new HashSet<>();
            Set<AoaSysMenu> twoMenuAll=new HashSet<>();
            for (AoaSysMenu aoaSysMenu:MenuAll) {
                if (aoaSysMenu.getParentId()==0){
                    oneMenuAll.add(aoaSysMenu);
                    twoMenuAll.addAll(menuSysService.findChil(aoaSysMenu.getMenuId(),user.getRole().getRoleId(),true,true));
                }else {
                    twoMenuAll.add(aoaSysMenu);
                    oneMenuAll.add(menuSysService.findByChil(aoaSysMenu.getParentId(), user.getRole().getRoleId(),true,true));
                }
            }
            req.setAttribute("oneMenuAll", oneMenuAll);
            req.setAttribute("twoMenuAll", twoMenuAll);
        }
        else {
            menuSysService.findMenuSys(req,user);
        }
        return "common/leftlists";
    }
    @RequestMapping("userlogs")
    public String usreLog(@SessionAttribute("userId") Long userId, HttpServletRequest req){
        List<AoaUserLog> userLogs=userService.findLogByUser(userId);
        req.setAttribute("userLogList", userLogs);
        return "user/userlog";
    }
    @RequestMapping("test2")
    public String test2(HttpSession session, Model model, HttpServletRequest request){
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user=userService.findOne(userId);
        request.setAttribute("user", user);
        //计算三个模块的记录条数
        request.setAttribute("filenum", fileService.count());
        request.setAttribute("directornum", adressService.count());
        request.setAttribute("discussnum", discussService.count());

        List<Map<String, Object>> list = noticeService.findMyNoticeLimit(userId);
        model.addAttribute("user", user);
        for (Map<String, Object> map : list) {
            map.put("status", statusService.findOne((Long) map.get("status_id")).getStatusName());
            map.put("type", typeService.findOne((Long) map.get("type_id")).getTypeName());
            map.put("statusColor", statusService.findOne((Long) map.get("status_id")).getStatusColor());
            map.put("userName", userService.findOne((Long) map.get("user_id")).getUserName());
            map.put("deptName", userService.findOne((Long) map.get("user_id")).getDept().getDeptName());
        }
        // List<Map<String, Object>>
        // noticeList=informRService.setList(noticeList1);
        showalist(model, userId);
        model.addAttribute("noticeList", list);
        //列举计划
        List<AoaPlanList> plans=planService.findByUserlimit(userId);
        model.addAttribute("planList", plans);
        List<AoaTypeList> ptype =  typeService.findByTypeModel("aoa_plan_list");
        List<AoaStatusList> pstatus = statusService.findByStatusModel("aoa_plan_list");
        model.addAttribute("ptypelist", ptype);
        model.addAttribute("pstatuslist", pstatus);

        //列举便签
        List<AoaNotepaper> notepapers=noteService.findByUserIdOrderByCreateTimeDesc(userId);
        model.addAttribute("notepaperList", notepapers);

        //列举几个流程记录
        List<AoaProcessList> pList=processService.findlastthree(userId);
        model.addAttribute("processlist", pList);
        List<AoaStatusList> processstatus =  statusService.findByStatusModel("aoa_process_list");
        model.addAttribute("prostatuslist", processstatus);
        return "systemcontrol/control";
    }
    private void showalist(Model model, Long userId) {
        // 显示用户当天最新的记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String nowdate = sdf.format(date);
        AoaAttendsList aList =attendanceService .findlastest(nowdate, userId);
        if (aList != null) {
            long type=aList.getTypeId();
            model.addAttribute("type", type);
        }
        model.addAttribute("alist", aList);
    }

    @RequestMapping("notlimit")
    public String notLimit() {
        return "common/notlimit";
    }
}
