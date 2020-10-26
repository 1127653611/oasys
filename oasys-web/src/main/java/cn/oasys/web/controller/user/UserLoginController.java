package cn.oasys.web.controller.user;
import cn.oasys.web.model.pojo.schedule.AoaScheduleList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import cn.oasys.web.service.inter.schedule.DaymanageService;
import cn.oasys.web.service.inter.task.TaskService;
import cn.oasys.web.service.inter.user.UserLoginRecordService;
import cn.oasys.web.service.inter.user.UserService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class UserLoginController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private DaymanageService daymanageService;
    @Autowired
    private UserLoginRecordService userLoginRecordService;
    @RequestMapping("morelogrecord")
    public String test3df342(@RequestParam(value="page",defaultValue = "0")int page,
                             HttpSession session, Model model,
                             @RequestParam(value="baseKey",required=false)String basekey) {
        long userid=Long.valueOf(session.getAttribute("userId")+"");
        Page page1= PageHelper.startPage(page, 15);
        List<AoaUserLoginRecord> aoaUserLoginRecords=userLoginRecordService.getAll(userid);
        PageInfo<AoaUserLoginRecord> pageInfo=new PageInfo<>(aoaUserLoginRecords);
        model.addAttribute("userloglist", aoaUserLoginRecords);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "morelogrecordtable");
        return "user/userlogrecordmanage";
    }
    @RequestMapping("morelogrecordtable")
    public String test3dfrt342(@RequestParam(value="page",defaultValue = "0")int page,
                               HttpSession session,Model model,
                               @RequestParam(value="baseKey",required=false)String basekey) {
        long userid=Long.valueOf(session.getAttribute("userId")+"");
        PageHelper.startPage(page, 15);
        List<AoaUserLoginRecord> aoaUserLoginRecords=userLoginRecordService.getAll(userid);
        PageInfo<AoaUserLoginRecord> pageInfo=new PageInfo<>(aoaUserLoginRecords);
        model.addAttribute("userloglist", aoaUserLoginRecords);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "morelogrecordtable");
        return "user/userlogrecordmanagetable";

    }
    @RequestMapping("littlecalendar")
    /**
     * @ Description   :  日历数据
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/18 3:25
     * @ Param         :  session
     * @ Param         :  response 
     * @ return        :  java.lang.String
     */
    public String test3df(HttpSession session, HttpServletResponse response) throws IOException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long userid=Long.valueOf(session.getAttribute("userId")+"");
        List list=new ArrayList<>();
        List<AoaScheduleList>  dates=daymanageService.findstart(userid);
        for (AoaScheduleList scheduleList : dates) {
            list.add(sdf.format(scheduleList.getStartTime()));
        }
        String json= JSONObject.toJSONString(list);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(json);
        return null;
    }
    @RequestMapping("counttasknum")
    public String test3df(HttpServletResponse response) throws IOException {
        List<AoaUser> uList= userService.findAll();
        HashMap< String, Integer> hashMap=new HashMap<>();
        int i=0;
        for (AoaUser user : uList) {
            if(taskService.countfinish(7l, user.getUserId())>0){
                hashMap.put(user.getUserName(), taskService.countfinish(7l, user.getUserId()));
                i++;
            }
        }
        ArrayList<Map.Entry<String,Integer>> entries= sortMap(hashMap);
        ArrayList<Map.Entry<String,Integer>> entries2=new ArrayList<Map.Entry<String,Integer>>();

        if(entries.size()>=5)
            //获得前5个s
            for (int j = 0; j < 5; j++) {
                entries2.add(entries.get(j));
            }
        else {
            entries2= entries;
        }
        String json=JSONObject.toJSONString(entries2);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(json);
        return null;
    }
    @RequestMapping("countweeklogin")
    public String dfsa(HttpServletResponse response) throws IOException{
        Integer []r=new Integer[7];
        Calendar calendar=Calendar.getInstance();
        setToFirstDay(calendar);
        for (int i = 0; i < 7; i++) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            r[i]=userLoginRecordService.countlog(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        String json=JSONObject.toJSONString(r);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(json);
        return null;
    }
    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }
    public static ArrayList<Map.Entry<String,Integer>> sortMap(Map map){
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1 , Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        return (ArrayList<Map.Entry<String, Integer>>) entries;
    }
}
