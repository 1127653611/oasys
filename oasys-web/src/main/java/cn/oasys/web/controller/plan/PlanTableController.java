package cn.oasys.web.controller.plan;

import cn.oasys.web.model.pojo.plan.AoaPlanList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.plan.PlanService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/")
@Controller
public class PlanTableController {
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private StatusService statusService;
    Date startDate,endDate;
    String choose2;
    @RequestMapping("myplan")
    public String test2(HttpServletRequest request, Model model, HttpSession session,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "baseKey", required = false) String baseKey) throws ParseException {
        plantablepaging(request, model, session, page, baseKey);
        return "plan/plantable";
    }

    /**
     * 计划报表
     *
     * @ CreateDate    :  2020/8/6 4:21
     * @ parm          :
     * @ return        :
     */
    private void plantablepaging(HttpServletRequest request, Model model, HttpSession session, int page,
                                 String baseKey) throws ParseException {
        List<AoaPlanList> pList=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<AoaTypeList> type = typeService.findByTypeModel("aoa_plan_list");
        List<AoaStatusList> status = statusService.findByStatusModel("aoa_plan_list");
        List<AoaPlanList> plans = new ArrayList<>();
        // 利用set过滤掉重复的plan_user_id 因为set不能重复
        Set<Long> number = new HashSet();
        AoaPlanList plan2;
        long typeid = 13;
        Long choose;
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        Date start = null;
        if (!StringUtils.isEmpty(starttime)) start=simpleDateFormat.parse(starttime);
        Date end = null;
        if (!StringUtils.isEmpty(endtime)) end=simpleDateFormat.parse(endtime);
        String choose1 = request.getParameter("choose");
        //分页的时候记住
        if (start == null && end == null && choose1 == null) {
            start = startDate;
            end = endDate;
            choose1 = choose2;
        }
        if (start != null && end != null && choose1 != null) {
            startDate = start;
            endDate = end;
            choose2 = choose1;
        }
        // 1是日计划2是周计划3是月计划
        if (choose1 == null || choose1.length() == 0)
            choose = 1l;
        else
            choose = Long.valueOf(choose1);
        if (choose == 1) {
            typeid = 13l;
        }
        if (choose == 2) {
            typeid = 14l;
        }
        if (choose == 3) {
            typeid = 15l;
        }
        pList = planService.findAll();
        Long userid = Long.valueOf(session.getAttribute("userId") + "");
        PageHelper.startPage(page,10);
        List<AoaUser> aoaUsers = userService.findmyemployuser(baseKey, userid);
        PageInfo<AoaUser> uListpage=new PageInfo<>(aoaUsers);
        for (AoaPlanList plan : pList) {
            number.add(plan.getUser().getUserId());
        }
        // 找到相对应的计划记录
        for (Long num : number) {
            plan2 = planService.findlatest(start, end, num, typeid);
            if (plan2 != null)
                plans.add(plan2);
        }
        // 将用户名和list绑定在一起
        Map<String, AoaPlanList> uMap = new HashMap<>();
        for (AoaUser user : aoaUsers) {
            if (plans.size() == 0)
                uMap.put(user.getUserName(), null);
            for (AoaPlanList plan : plans) {
                if (Objects.equals(user.getUserId(), plan.getUser().getUserId())) {
                    uMap.put(user.getUserName(), plan);
                    break;
                } else {
                    uMap.put(user.getUserName(), null);
                }
            }
        }

        //记住开始时间和结束时间以及选择
        model.addAttribute("starttime", starttime);
        model.addAttribute("endtime", endtime);
        model.addAttribute("choose", choose1);

        model.addAttribute("uMap", uMap);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("plans", plans);
        model.addAttribute("plist", pList);
        model.addAttribute("ulist", aoaUsers);
        model.addAttribute("page", uListpage);
        model.addAttribute("url", "realplantable");
    }
    @RequestMapping("realplantable")
    public String test23(HttpServletRequest request, Model model, HttpSession session,
                         @RequestParam(value="pid",required=false) String pid,
                         @RequestParam(value="comment",required=false) String comment,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "baseKey", required = false) String baseKey) throws ParseException {
        if (!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey",baseKey);
        }
        if(!StringUtils.isEmpty(pid)){
            AoaPlanList plan = planService.findOne(Long.valueOf(pid));
            if(plan.getPlanComment()==null)
                plan.setPlanComment(comment);
            else
                plan.setPlanComment(plan.getPlanComment() + comment);
            planService.save(plan);}
        plantablepaging(request, model, session, page, baseKey);
        return "plan/realplantable";
    }
}
