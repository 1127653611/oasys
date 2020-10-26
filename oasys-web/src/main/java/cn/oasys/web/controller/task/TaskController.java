package cn.oasys.web.controller.task;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.task.AoaTaskList;
import cn.oasys.web.model.pojo.task.AoaTaskLogger;
import cn.oasys.web.model.pojo.task.AoaTaskUser;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.task.TaskService;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/")
public class TaskController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TaskService taskService;

    @RequestMapping("taskmanage")
    public String index(Model model,
                        @SessionAttribute("userId") Long userId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {

        PageHelper.startPage(page, size);
        List<AoaTaskList> tasklist = taskService.index(null, userId, null);
        List<Map<String, Object>> list = taskService.index2(tasklist, userId);
        PageInfo<AoaTaskList> aoaTaskListPageInfo = new PageInfo<>(tasklist);
        model.addAttribute("tasklist", list);
        model.addAttribute("page", aoaTaskListPageInfo);
        model.addAttribute("url", "paixu");
        return "task/taskmanage";
    }

    @RequestMapping("paixu")
    public String paixu(HttpServletRequest request,
                        @SessionAttribute("userId") Long userId, Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {

        // 通过发布人id找用户
        String type = null;
        String baseKey = null;
        if (!StringUtil.isEmpty(request.getParameter("val"))) {
            type = request.getParameter("val").trim();
            model.addAttribute("sort", "&val=" + type);
        }
        if (!StringUtil.isEmpty(request.getParameter("baseKey"))) {
            baseKey = request.getParameter("baseKey").trim();
        }

        PageHelper.startPage(page, size);
        List<AoaTaskList> tasklist = taskService.index(baseKey, userId, type);
        List<Map<String, Object>> list = taskService.index2(tasklist, userId);
        PageInfo<AoaTaskList> aoaTaskListPageInfo = new PageInfo<>(tasklist);
        model.addAttribute("tasklist", list);
        model.addAttribute("page", aoaTaskListPageInfo);
        model.addAttribute("url", "paixu");

        return "task/managetable";

    }

    /**
     * 查看任务
     *
     * @ CreateDate    :  2020/9/23 18:32
     * @ parm          :
     * @ return        :
     */
    @RequestMapping("seetasks")
    public ModelAndView index4(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("task/seetask");
        // 得到任务的 id
        String taskid = req.getParameter("id");
        Long ltaskid = Long.parseLong(taskid);
        // 通过任务id得到相应的任务
        AoaTaskList task = taskService.findOne(ltaskid);
        Long statusid = task.getStatusId().longValue();

        // 根据状态id查看状态表
        AoaStatusList status = statusService.findOne(statusid);
        // 查看状态表
        Iterable<AoaStatusList> statuslist = statusService.findAll();
        // 查看发布人
        AoaUser user = userService.findOne(task.getTaskPushUserId());
        // 查看任务日志表
        List<AoaTaskLogger> logger = taskService.findByTaskId(ltaskid);
        mav.addObject("task", task);
        mav.addObject("user", user);
        mav.addObject("status", status);
        mav.addObject("loggerlist", logger);
        mav.addObject("statuslist", statuslist);
        return mav;
    }

    /**
     * 保存日志
     *
     * @ CreateDate    :  2020/9/26 15:48
     * @ parm          :
     * @ return        :
     */
    @RequestMapping("tasklogger")
    public String tasklogger(AoaTaskLogger logger, @SessionAttribute("userId") Long userId) {

        AoaUser userlist = userService.findOne(userId);
        logger.setCreateTime(new Date());
        logger.setUsername(userlist.getUserName());

        // 存日志
        taskService.save(logger);
        // 修改任务状态
        taskService.updateStatusid(logger.getTaskId(), logger.getLoggerStatusid());

        return "redirect:/taskmanage";

    }

    /**
     * 编辑任务
     *
     * @ CreateDate    :  2020/9/26 16:18
     * @ parm          :
     * @ return        :
     */
    @RequestMapping("edittasks")
    public ModelAndView index3(HttpServletRequest req, @SessionAttribute("userId") Long userId,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {

        ModelAndView mav = new ModelAndView("task/edittask");
        AoaUser user = userService.findOne(userId);
        // 得到链接中的任务id
        String taskid = req.getParameter("id");
        Long ltaskid = Long.parseLong(taskid);
        // 通过任务id得到相应的任务
        AoaTaskList task = taskService.findOne(ltaskid);
        // 得到状态id
        Long statusid = task.getStatusId().longValue();
        // 得到类型id
        Long typeid = task.getTypeId();
        // 查看状态表
        AoaStatusList status = statusService.findOne(statusid);
        // 查询类型表
        AoaTypeList type = typeService.findOne(typeid);

        // 查询部门下面的员工
        PageHelper.startPage(page, size);
        List<AoaUser> emplist = userService.findByFatherId(user.getFatherId());
        PageInfo<AoaUser> userPageInfo = new PageInfo<>(emplist);

        // 查询部门表
        Iterable<AoaDept> deptlist = deptService.findAll();
        // 查职位表
        Iterable<AoaPosition> poslist = positionService.findAll();
        mav.addObject("type", type);
        mav.addObject("status", status);
        mav.addObject("emplist", emplist);
        mav.addObject("deptlist", deptlist);
        mav.addObject("poslist", poslist);
        mav.addObject("task", task);
        mav.addObject("page", userPageInfo);
        mav.addObject("url", "names");
        mav.addObject("qufen", "任务");
        return mav;
    }

    @RequestMapping("update")
    public String update(AoaTaskList task, @SessionAttribute("userId") long userid) {
        if (StringUtils.isEmpty(task.getIsCancel())) task.setIsCancel(0);
        if (StringUtils.isEmpty(task.getIsTop())) task.setIsTop(0);
        task.setTaskPushUserId(userid);
        task.setPublishTime(new Date());
        task.setModifyTime(new Date());
        taskService.savetask(task);

        // 分割任务接收人 还要查找联系人的主键
        StringTokenizer st = new StringTokenizer(task.getReciverlist(), ";");
        List<Long> pklist = new ArrayList<>();
        while (st.hasMoreElements()) {
            AoaUser reciver = userService.findByname(st.nextToken());
            Long pkid = taskService.findpkId(task.getTaskId(), reciver.getUserId());
            AoaTaskUser tasku = new AoaTaskUser();
            tasku.setPkId(pkid);
            tasku.setTaskId(task.getTaskId());
            tasku.setTaskReciveUserId(reciver.getUserId());
            tasku.setStatusId(Long.parseLong(task.getStatusId() + ""));
            // 存任务中间表
            taskService.savetaskUser(tasku);
            pklist.add(tasku.getPkId());

        }
        taskService.deleteUserNoId(pklist, task.getTaskId());

        return "redirect:/taskmanage";

    }

    /**
     * 新增任务
     *
     * @ CreateDate    :  2020/10/1 22:14
     * @ parm          : userId，page，size
     * @ return        : mav
     */
    @RequestMapping("addtask")
    public ModelAndView index2(@SessionAttribute("userId") Long userId,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {

        ModelAndView mav = new ModelAndView("task/addtask");
        AoaUser user = userService.findOne(userId);
        // 查询类型表
        Iterable<AoaTypeList> typelist = typeService.findAll();
        // 查询状态表
        Iterable<AoaStatusList> statuslist = statusService.findAll();
        // 查询部门下面的员工
        PageHelper.startPage(page, size);
        List<AoaUser> pagelist = userService.findByFatherId(user.getFatherId());
        PageInfo<AoaUser> userPageInfo = new PageInfo<>(pagelist);
        // 查询部门表
        Iterable<AoaDept> deptlist = deptService.findAll();
        // 查职位表
        Iterable<AoaPosition> poslist = positionService.findAll();
        mav.addObject("typelist", typelist);
        mav.addObject("statuslist", statuslist);
        mav.addObject("emplist", pagelist);
        mav.addObject("deptlist", deptlist);
        mav.addObject("poslist", poslist);
        mav.addObject("page", userPageInfo);
        mav.addObject("url", "names");
        mav.addObject("qufen", "任务");
        return mav;
    }

    @RequestMapping("ck_addtask")
    /**
     * @ Description   :  添加任务
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/7 20:37
     * @ Param         :  request
     * @ Param         :  tlist
     * @ Param         :  userId
     * @ Param         :  page
     * @ Param         :  size
     * @ return        :  java.lang.String
     */

    public String addtask(HttpServletRequest request, AoaTaskList tlist, @SessionAttribute("userId") Long userId,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {

        if (StringUtils.isEmpty(tlist.getTitle())) {
            request.setAttribute("errormess", "标题不能为空");
            return "forward:/addtask";
        }
        if (StringUtils.isEmpty(tlist.getReciverlist())) {
            request.setAttribute("errormess", "接收人不能为空");
            return "forward:/addtask";
        }
        tlist.setTaskPushUserId(userId);
        tlist.setPublishTime(new Date());
        tlist.setModifyTime(new Date());
        if (StringUtils.isEmpty(request.getParameter("top"))) {
            tlist.setIsTop(0);
        } else {
            tlist.setIsTop(1);
        }
        if (StringUtils.isEmpty(request.getParameter("cancel"))) {
            tlist.setIsCancel(0);
        } else {
            tlist.setIsCancel(1);
        }

        taskService.insert(tlist);
        StringTokenizer st = new StringTokenizer(tlist.getReciverlist(), ";");
        while (st.hasMoreElements()) {
            AoaUser reciver = userService.findByname(st.nextToken());
            AoaTaskUser task = new AoaTaskUser();
            task.setTaskId(tlist.getTaskId());
            task.setTaskReciveUserId(reciver.getUserId());
            task.setStatusId(Long.parseLong(tlist.getStatusId() + ""));
            // 存任务中间表
            taskService.savetaskUser(task);

        }
        request.setAttribute("success", 1);
        return "redirect:/taskmanage";
    }

    @RequestMapping("shanchu")
    /**
     * @ Description   :  删除任务
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/7 20:38
     * @ Param         :  req
     * @ Param         :  userId 
     * @ return        :  java.lang.String
     */
    public String delete(HttpServletRequest req, @SessionAttribute("userId") Long userId) {
        // 得到任务的 id
        String taskid = req.getParameter("id");
        Long ltaskid = Long.parseLong(taskid);

        // 根据任务id找出这条任务
        AoaTaskList task = taskService.findOne(ltaskid);
        if (task.getTaskPushUserId().equals(userId)) {
            // 删除日志表
            taskService.detelelogger(ltaskid);
            // 分割任务接收人 还要查找联系人的主键并删除接收人中间表
            StringTokenizer st = new StringTokenizer(task.getReciverlist(), ";");
            while (st.hasMoreElements()) {
                AoaUser reciver = userService.findByname(st.nextToken());
                Long pkid = taskService.findpkId(task.getTaskId(), reciver.getUserId());
                taskService.delete(pkid);

            }
            taskService.deleteUser(ltaskid);
            // 删除这条任务
            taskService.deteletask(task);
        } else {
            return "redirect:/notlimit";

        }
        return "redirect:/taskmanage";

    }

    @RequestMapping("mytask")
    /**
     * @ Description   :  我的任务
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/7 20:56
     * @ Param         :  userId
     * @ Param         :  model
     * @ Param         :  page
     * @ Param         :  size 
     * @ return        :  java.lang.String
     */
    public String index5(@SessionAttribute("userId") Long userId, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<AoaTaskList> aoaTaskLists = taskService.index3(userId, null);
        PageInfo<AoaTaskList> aoaTaskListPageInfo = new PageInfo<>(aoaTaskLists);
        List<Map<String, Object>> list = taskService.index4(aoaTaskLists, userId);
        model.addAttribute("page", aoaTaskListPageInfo);
        model.addAttribute("tasklist", list);
        model.addAttribute("url", "mychaxun");
        return "task/mytask";

    }

    @RequestMapping("mychaxun")
    /**
     * @ Description   :  查询任务
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/12 21:54
     * @ Param         :  request
     * @ Param         :  userId
     * @ Param         :  model
     * @ Param         :  page
     * @ Param         :  size 
     * @ return        :  java.lang.String
     * @ throws        :  ParseExceptions
     */
    public String select(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException {

        String title = null;
        if (!StringUtil.isEmpty(request.getParameter("title"))) {
            title = request.getParameter("title").trim();
        }
        PageHelper.startPage(page, size);
        List<AoaTaskList> aoaTaskLists = taskService.index3(userId, title);
        PageInfo<AoaTaskList> aoaTaskListPageInfo = new PageInfo<>(aoaTaskLists);
        List<Map<String, Object>> list = taskService.index4(aoaTaskLists, userId);
        model.addAttribute("page", aoaTaskListPageInfo);
        model.addAttribute("tasklist", list);
        model.addAttribute("url", "mychaxun");
        model.addAttribute("sort", "&title=" + title);
        return "task/mytasklist";
    }

    @RequestMapping("myseetasks")
    /**
     * @ Description   :  查看我的任务
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/12 22:01
     * @ Param         :  req
     * @ Param         :  userId 
     * @ return        :  org.springframework.web.servlet.ModelAndView
     */
    public ModelAndView myseetask(HttpServletRequest req, @SessionAttribute("userId") Long userId) {

        ModelAndView mav = new ModelAndView("task/myseetask");
        // 得到任务的 id
        String taskid = req.getParameter("id");

        Long ltaskid = Long.parseLong(taskid);
        // 通过任务id得到相应的任务
        AoaTaskList task = taskService.findOne(ltaskid);

        // 查看状态表
        Iterable<AoaStatusList> statuslist = statusService.findAll();
        // 查询接收人的任务状态
        Long ustatus = taskService.findByuserIdAndTaskId(userId, ltaskid);
        AoaStatusList status = statusService.findOne(ustatus);


        // 查看发布人
        AoaUser user = userService.findOne(task.getTaskPushUserId());
        // 查看任务日志表
        List<AoaTaskLogger> logger = taskService.findByTaskId(ltaskid);

        mav.addObject("task", task);
        mav.addObject("user", user);
        mav.addObject("status", status);
        mav.addObject("statuslist", statuslist);
        mav.addObject("loggerlist", logger);
        return mav;

    }

    @RequestMapping("uplogger")
    /**
     * @ Description   :  修改日志
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/12 22:29
     * @ Param         :  logger
     * @ Param         :  userId 
     * @ return        :  java.lang.String
     */
    public String updatelo(AoaTaskLogger logger, @SessionAttribute("userId") Long userId) {

        AoaUser userlist = userService.findOne(userId);
        // 查任务
        AoaTaskList task = taskService.findOne(logger.getTaskId());
        logger.setCreateTime(new Date());
        logger.setUsername(userlist.getUserName());

        // 存日志
        taskService.save(logger);

        // 修改任务中间表状态
        Long pkid = taskService.findpkId(logger.getTaskId(), userId);
        AoaTaskUser tasku = taskService.findOneUser(pkid);;
        tasku.setStatusId(Long.parseLong(logger.getLoggerStatusid() + ""));
        // 存任务中间表
        taskService.savetaskUser(tasku);

        // 修改任务状态
        // 通过任务id查看总状态

        List<AoaTaskLogger> statu = taskService.findByTaskId(logger.getTaskId());

        // 选出最小的状态id 修改任务表里面的状态
        AoaTaskLogger min = statu.get(0);

        for (AoaTaskLogger integer : statu) {
            if (integer.getLoggerStatusid() < min.getLoggerStatusid()) {
                min = integer;
            }
        }

        taskService.updatelistStatus(logger.getTaskId(), min.getLoggerStatusid());

        return "redirect:/mytask";

    }
   /**
    * @ Description   :  接收人这边删除
    * @ Author        :  wang
    * @ CreateDate    :  2020/10/13 0:53
    * @ Param         :  null 
    * @ return        :  
    */
    @RequestMapping("myshanchu")
    public String mydelete(HttpServletRequest req, @SessionAttribute("userId") Long userId) {
        // 用户id
        // 得到任务的 id
        String taskid = req.getParameter("id");
        Long ltaskid = Long.parseLong(taskid);
        Long pkid = taskService.findpkId(ltaskid, userId);
        taskService.delete(pkid);

        return "redirect:/mytask";

    }
}
