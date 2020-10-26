package cn.oasys.web.controller.daymanage;

import cn.oasys.web.common.Utils.User;
import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import cn.oasys.web.model.pojo.schedule.AoaScheduleUser;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.schedule.DaymanageService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @ Description   :  日曾管理
 * @ Author        :  wang
 * @ CreateDate    :  2020/10/13 0:57
 */
@Controller
@RequestMapping("/")
public class DayManageController {
    @Autowired
    private User u;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private DaymanageService daymanageService;

    @RequestMapping("daymanage")
    private String daymanage(@SessionAttribute("userId") Long userId,
                             Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageHelper.startPage(page, size);
        List<AoaScheduleList> scheduleLists = daymanageService.findByUser(userId, null);
        PageInfo<AoaScheduleList> myday = new PageInfo<>(scheduleLists);
        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("page", myday);
        model.addAttribute("url", "daymanagepaging");
        model.addAttribute("ismyday", 1);
        return "daymanage/daymanage";
    }

    @RequestMapping("daymanagepaging")
    private String daymanagepaging(@SessionAttribute("userId") Long userid,
                                   Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size, @RequestParam("key") String key
    ) {


        PageHelper.startPage(page, size);
        List<AoaScheduleList> scheduleLists = daymanageService.findByUser(userid, key);
        PageInfo<AoaScheduleList> myday = new PageInfo<>(scheduleLists);
        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("page", myday);
        model.addAttribute("url", "daymanagepaging");
        model.addAttribute("sort", "&key=" + key);
        model.addAttribute("ismyday", 1);
        return "daymanage/daymanagepaging";
    }

    @RequestMapping("dayedit")
    private String dayedit(@RequestParam(value = "rcid", required = false) Long rcid,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           Model model
    ) {
        u.getAllUser(page, size, model);
        List<AoaTypeList> types = typeService.findByTypeModel("aoa_schedule_list");
        List<AoaStatusList> statuses = statusService.findByStatusModel("aoa_schedule_list");
        AoaScheduleList rc = null;
        if (rcid != null) {
            rc = daymanageService.findOne(rcid);
        }

        model.addAttribute("rc", rc);
        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        return "daymanage/editday";
    }

    @RequestMapping("addandchangeday")
    public String addandchangeday(AoaScheduleList scheduleList, @RequestParam("shareuser") String shareuser,
                                  @SessionAttribute("userId") Long userid, HttpServletRequest request) {
        if (!StringUtils.isEmpty(request.getParameter("Remind")))
            scheduleList.setIsRemind(1);
        else scheduleList.setIsRemind(0);

        if (StringUtils.isEmpty(scheduleList.getRcId())) {
            scheduleList.setUserId(userid);
            scheduleList.setCreateTime(new Date());
            scheduleList.setIsreminded(0);

        }
        daymanageService.save(scheduleList);
        StringTokenizer st = new StringTokenizer(shareuser, ";");

        while (st.hasMoreElements()) {
            AoaScheduleUser aoaScheduleUser=new AoaScheduleUser();
            aoaScheduleUser.setRcid(scheduleList.getRcId());
            aoaScheduleUser.setUserId(userService.findByname(st.nextToken()).getUserId());
            daymanageService.saveUser(aoaScheduleUser);

        }

        return "redirect:/daymanage";
    }
    @RequestMapping("dayremove")
    /**
     * @ Description   :  删除
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/13 20:41
     * @ Param         :  rcid 
     * @ return        :  java.lang.String
     */
    public String dayremove(@RequestParam(value="rcid") Long rcid){
        AoaScheduleList rc = daymanageService.findOne(rcid);

        daymanageService.delete(rc);

        return "redirect:/daymanage";
    }
    @RequestMapping("daycalendar")
    private String daycalendar() {
        return "daymanage/daycalendar";
    }
    @RequestMapping("mycalendarload")
    /**
     * @ Description   :  TODO
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/13 22:16
     * @ Param         :  userid
     * @ Param         :  response 
     * @ return        :  java.util.List<ScheduleList>
     */
    public @ResponseBody
    List<AoaScheduleUser> mycalendarload(@SessionAttribute("userId") Long userid, HttpServletResponse response) throws IOException {
        List<AoaScheduleUser> se = daymanageService.aboutmeschedule(userid);

        return se;
    }
}
