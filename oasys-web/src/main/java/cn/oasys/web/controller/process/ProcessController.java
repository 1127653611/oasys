package cn.oasys.web.controller.process;

import cn.oasys.web.common.Utils.User;
import cn.oasys.web.exception.NameErrorException;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.process.*;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/")
public class ProcessController {
    @Autowired
    private User us;
    @Autowired
    private StatusService statusService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private TypeService typeService;

    @RequestMapping("newprocess")
    public String newProcess() {
        return "process/procedure";
    }

    @RequestMapping("burse")
    public String bursement(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<AoaTypeList> uplist = typeService.findByTypeModel("aoa_bursement");
        //查找费用科目生成树
        List<AoaSubject> second = processService.findByParentId(1L);
        List<AoaSubject> sublist = processService.findByParentIdNot(1L);
        processService.index6(model, userId, page, size);
        model.addAttribute("second", second);
        model.addAttribute("sublist", sublist);
        model.addAttribute("uplist", uplist);
        return "process/bursement";
    }


    @RequestMapping("apply")
    public String apply(@RequestParam("filePath") MultipartFile filePath, HttpServletRequest req, AoaBursement bu,
                        @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, NameErrorException {

        AoaUser lu = userService.findOne(userId);//申请人
        AoaUser reuser;
        AoaUser zhuti;
        try {
            reuser = userService.findByname(req.getParameter("username"));//审核人
            zhuti = userService.findByname(req.getParameter("namemoney"));//证明人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }
        Integer allinvoice = 0;
        Double allmoney = 0.0;
        Long roleid = reuser.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = reuser.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            List<AoaDetailsburse> mm = bu.getDetails();
            if (mm != null && !mm.isEmpty()) {
                for (AoaDetailsburse detailsBurse : mm) {
                    allinvoice += detailsBurse.getInvoices();
                    allmoney += detailsBurse.getDetailmoney();
                    detailsBurse.setBursmentId(bu.getBursementId());
                }
            }
            bu.setAllinvoices(allinvoice);
            bu.setAllMoney(allmoney);
            bu.setUserName(zhuti.getUserId());
            AoaProcessList pro = bu.getAoaProcessList();
            processService.index5(pro, val, lu, filePath, reuser.getUserName());
            processService.saveburse(pro, bu, mm);
            //审核表
            processService.saveReview(reuser, pro);
        } else {
            return "common/proce";
        }
        req.setAttribute("success", "后台验证成功");
        return "forward:/burse";
    }

    @RequestMapping("evection")
    /**
     * @ Description   :  出差申请
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/16 1:26
     * @ Param         :  model
     * @ Param         :  userId
     * @ Param         :  request
     * @ Param         :  page
     * @ Param         :  size 
     * @ return        :  java.lang.String
     */
    public String evection(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        //查找类型
        List<AoaTypeList> outtype = typeService.findByTypeModel("aoa_evection");
        processService.index6(model, userId, page, size);
        model.addAttribute("outtype", outtype);
        return "process/evection";
    }

    @RequestMapping("evec")
    public String evec(@RequestParam("filePath") MultipartFile filePath, HttpServletRequest req, AoaEvection eve,
                       @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, NameErrorException {
        AoaUser lu = userService.findOne(userId);//申请人
        AoaUser shen;
        try {
            shen = userService.findByname(eve.getNameuser());//审核人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }

        Long roleid = shen.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = shen.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            //set主表
            AoaProcessList pro = eve.getAoaProcessList();
            processService.index5(pro, val, lu, filePath, shen.getUserName());
            processService.saveEvection(pro, eve);
            //存审核表
            processService.saveReview(shen, pro);
        } else {
            return "common/proce";
        }

        req.setAttribute("success", "后台验证成功");
        return "forward:/evection";
    }

    @RequestMapping("overtime")
    /**
     * @ Description   :  加班申请
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/16 1:57
     * @ Param         :  model
     * @ Param         :  userId
     * @ Param         :  request
     * @ Param         :  page
     * @ Param         :  size 
     * @ return        :  java.lang.String
     */
    public String overtime(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        //查找类型
        List<AoaTypeList> overtype = typeService.findByTypeModel("aoa_overtime");
        processService.index6(model, userId, page, size);
        model.addAttribute("overtype", overtype);
        return "process/overtime";
    }

    @RequestMapping("over")
    public String over(HttpServletRequest req, AoaOvertime eve,
                       @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, NameErrorException {
        AoaUser lu;
        AoaUser shen;
        try {
            lu = userService.findOne(userId);//申请人
            shen = userService.findByname(eve.getNameuser());//审核人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }

        Long roleid = shen.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = shen.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            //set主表
            AoaProcessList pro = eve.getAoaProcessList();
            processService.index8(pro, val, lu, shen.getUserName());
            processService.saveOvertime(pro, eve);
            //存审核表
            processService.saveReview(shen, pro);
        } else {
            return "common/proce";
        }
        req.setAttribute("success", "后台验证成功");
        return "forward:/overtime";

    }

    @RequestMapping("regular")
    public String regular(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        processService.index6(model, userId, page, size);
        return "process/regular";
    }

    @RequestMapping("holiday")
    public String holiday(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        //查找类型
        List<AoaTypeList> overtype = typeService.findByTypeModel("aoa_holiday");
        processService.index6(model, userId, page, size);
        model.addAttribute("overtype", overtype);
        return "process/holiday";
    }

    @RequestMapping("resign")
    public String resign(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {
        processService.index6(model, userId, page, size);
        return "process/resign";
    }

    @RequestMapping("regu")
    public String regu(HttpServletRequest req, AoaRegular eve,
                       @SessionAttribute("userId") Long userId, Model model) throws IOException, NameErrorException {
        AoaUser lu;
        AoaUser shen;
        try {
            lu = userService.findOne(userId);//申请人
            shen = userService.findByname(eve.getNameuser());//审核人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }
        Long roleid = shen.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = shen.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            if (lu.getRole().getRoleId() == 6 || lu.getRole().getRoleId() == 7) {

                //set主表
                AoaProcessList pro = eve.getAoaProcessList();
                processService.index8(pro, val, lu, shen.getUserName());
                processService.saveRegular(pro, eve);
                //存审核表
                processService.saveReview(shen, pro);
            } else {
                model.addAttribute("error", "你不需要转正。。。");
                return "common/proce";
            }
        } else {
            return "common/proce";
        }
        req.setAttribute("success", "后台验证成功");
        return "forward:/regular";

    }

    @RequestMapping("holi")
    public String holi(@RequestParam("filePath") MultipartFile filePath, HttpServletRequest req, AoaHoliday eve,
                       @SessionAttribute("userId") Long userId, Model model) throws IllegalStateException, IOException, NameErrorException {
        AoaUser lu;
        AoaUser shen;
        try {
            lu = userService.findOne(userId);//申请人
            shen = userService.findByname(eve.getNameuser());//审核人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }
        Long roleid = shen.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = shen.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            AoaTypeList type = typeService.findOne(eve.getTypeId());
            if (type.getSortValue() < eve.getLeaveDays()) {
                model.addAttribute("error", type.getTypeName() + "必须小于" + type.getSortValue() + "天");
                return "common/proce";
            } else {
                //set主表
                AoaProcessList pro = eve.getAoaProcessList();
                processService.index5(pro, val, lu, filePath, shen.getUserName());
                processService.saveHoliday(pro, eve);
                //存审核表
                processService.saveReview(shen, pro);
            }
        } else {
            return "common/proce";
        }

        req.setAttribute("success", "后台验证成功");
        return "forward:/holiday";

    }

    @RequestMapping("res")
    public String res(HttpServletRequest req, AoaResign eve,
                      @SessionAttribute("userId") Long userId, Model model) throws IllegalStateException, IOException, NameErrorException {
        if (StringUtils.isEmpty(req.getParameter("finish"))) {
            eve.setIsFinish(true);
        } else {
            eve.setIsFinish(false);
        }
        AoaUser lu;
        AoaUser shen;
        try {
            lu = userService.findOne(userId);//申请人
            shen = userService.findByname(eve.getNameuser());//审核人
        } catch (Exception e) {
            throw new NameErrorException("人员数量错误");
        }
        Long roleid = shen.getRole().getRoleId();//申请人角色id
        Long fatherid = lu.getFatherId();//申请人父id
        Long userid = shen.getUserId();//审核人userid
        String val = req.getParameter("val");
        if (roleid <= 4L && Objects.equals(fatherid, userid)) {
            //set主表
            AoaProcessList pro = eve.getAoaProcessList();
            processService.index8(pro, val, lu, shen.getUserName());
            if (!StringUtils.isEmpty(req.getParameter("handuser"))) {
                eve.setHandUser(userService.findByname(req.getParameter("handuser")).getUserId());
            }
            processService.saveResign(pro, eve);
            //存审核表
            processService.saveReview(shen, pro);
        } else {
            return "common/proce";
        }

        req.setAttribute("success", "后台验证成功");
        return "forward:/resign";

    }

    @RequestMapping("flowmanage")
    public String flowManage(@SessionAttribute("userId") Long userId, Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<AoaProcessList> pagelist = processService.findByuserId(userId, null);
        PageInfo<AoaProcessList> aoaProcessListPageInfo = new PageInfo<>(pagelist);
        model.addAttribute("page", aoaProcessListPageInfo);
        model.addAttribute("prolist", pagelist);
        model.addAttribute("url", "shenser");
        return "process/flowmanage";
    }

    @RequestMapping("shenser")
    public String ser(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                      @RequestParam(value = "page", defaultValue = "0") int page,
                      @RequestParam(value = "size", defaultValue = "10") int size,
                      @RequestParam(value = "baseKey", required = false) String baseKey) {
        if (!StringUtils.isEmpty(baseKey)) {
            model.addAttribute("baseKey", baseKey);
        }
        PageHelper.startPage(page, size);
        List<AoaProcessList> pagelist = processService.findByuserId(userId, baseKey);
        PageInfo<AoaProcessList> aoaProcessListPageInfo = new PageInfo<>(pagelist);
        model.addAttribute("page", aoaProcessListPageInfo);
        model.addAttribute("prolist", pagelist);
        model.addAttribute("url", "shenser");
        return "process/managetable";
    }

    @RequestMapping("particular")
    public String particular(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req) {
        AoaUser user = userService.findOne(userId);//审核人或者申请人
        String id = req.getParameter("id");
        Long proid = Long.parseLong(id);
        String typename = req.getParameter("typename");//类型名称
        String name = null;
        Map<String, Object> map = new HashMap<>();
        AoaProcessList process = processService.findOne(proid);//查看该条申请
        Boolean flag = process.getProcessUserId().equals(userId);//判断是申请人还是审核人

        if (!flag) {
            name = "审核";
        } else {
            name = "申请";
        }
        map = processService.index3(name, user, typename, process);
        String url = processService.index4(typename, model, process.getProcessId());
        model.addAttribute("map", map);
        if (!StringUtils.isEmpty(url)) {
            return url;
        }
        return "process/serch";
    }

    @RequestMapping("audit")
    public String auding(@SessionAttribute("userId") Long userId, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {
        AoaUser user = userService.findOne(userId);
        PageHelper.startPage(page, size);
        List<AoaReviewed> pagelist = processService.index(user, null, model);
        PageInfo<AoaReviewed> aoaProcessListPageInfo = new PageInfo<>(pagelist);
        List<Map<String, Object>> prolist = processService.index2(pagelist, user);
        model.addAttribute("page", aoaProcessListPageInfo);
        model.addAttribute("prolist", prolist);
        model.addAttribute("url", "serch");

        return "process/auditing";
    }

    @RequestMapping("serch")
    public String serch(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "baseKey", required = false) String baseKey) {
        if (!StringUtils.isEmpty(baseKey)) {
            model.addAttribute("baseKey", baseKey);
        }
        AoaUser user = userService.findOne(userId);
        PageHelper.startPage(page, size);
        List<AoaReviewed> pagelist = processService.index(user, baseKey, model);
        PageInfo<AoaReviewed> aoaProcessListPageInfo = new PageInfo<>(pagelist);
        List<Map<String, Object>> prolist = processService.index2(pagelist, user);
        model.addAttribute("page", aoaProcessListPageInfo);
        model.addAttribute("prolist", prolist);
        model.addAttribute("url", "serch");

        return "process/audtable";
    }

    @RequestMapping("auditing")
    /**
     * @ Description   :  审核
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/22 2:15
     * @ Param         :  userId
     * @ Param         :  model
     * @ Param         :  req
     * @ Param         :  page
     * @ Param         :  size 
     * @ return        :  java.lang.String
     */
    public String auditing(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        AoaUser u = userService.findOne(userId);
        //流程id
        Long id = Long.parseLong(req.getParameter("id"));
        AoaProcessList process = processService.findOne(id);

        AoaReviewed re = processService.findByProIdAndUserId(process.getProcessId(), userId);//查找审核表

        String typename = process.getTypeName().trim();
        processService.findPro(model, process.getProcessId(), typename, process.getProcessUserId());

        us.getAllUser(page, size, model);
        List<Map<String, Object>> list = processService.indexx(process);
        model.addAttribute("process", process);
        model.addAttribute("revie", list);
        model.addAttribute("size", list.size());
        model.addAttribute("statusid", process.getStatusId());
        model.addAttribute("ustatusid", re.getStatusId());
        model.addAttribute("positionid", u.getPosition().getPositionId());
        model.addAttribute("typename", typename);

        return "process/audetail";

    }

    @RequestMapping("susave")
    public String save(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req, AoaReviewed reviewed) {
        String name = null;
        String typename = req.getParameter("type");
        AoaProcessList pro = processService.findOne(reviewed.getProId());//找到该条流程
        AoaUser shen = userService.findOne(pro.getProcessUserId());//申请人
        if (!StringUtil.isEmpty(req.getParameter("liuzhuan"))) {
            name = req.getParameter("liuzhuan");
        }
        if (!StringUtil.isEmpty(name)) {
            //审核并流转
            AoaUser u2 = userService.findByname(req.getParameter("username"));//找到下一个审核人
            if (("离职申请").equals(typename)) {
                if (userId.equals(pro.getUser().getFatherId())) {
                    if (u2.getPositionId().equals(5L)) {
                        processService.save(reviewed.getProId(), userId, reviewed, pro, u2);
                    } else {
                        model.addAttribute("error", "请选财务经理。");
                        return "common/proce";
                    }
                } else {
                    if (u2.getPositionId().equals(7L)) {
                        processService.save(reviewed.getProId(), userId, reviewed, pro, u2);
                    } else {
                        model.addAttribute("error", "请选人事经理。");
                        return "common/proce";
                    }
                }

            } else if (("费用报销").equals(typename) || ("出差费用").equals(typename)) {

                if (u2.getPositionId().equals(5L)) {
                    processService.save(reviewed.getProId(), userId, reviewed, pro, u2);
                } else {
                    model.addAttribute("error", "请选财务经理。");
                    return "common/proce";
                }
            } else {
                if (u2.getPositionId().equals(7L)) {
                    processService.save(reviewed.getProId(), userId, reviewed, pro, u2);
                } else {
                    model.addAttribute("error", "请选人事经理。");
                    return "common/proce";
                }
            }

        } else {
            //审核并结案
            AoaReviewed re = processService.findByProIdAndUserId(reviewed.getProId(), userId);
            re.setAdvice(reviewed.getAdvice());
            re.setStatusId(reviewed.getStatusId());
            re.setReviewedTime(new Date());
            processService.updatereview(re);
            pro.setStatusId(reviewed.getStatusId());//改变主表的状态
            processService.updatePro(pro);
            if (("请假申请").equals(typename) || ("出差申请").equals(typename)) {
                if (reviewed.getStatusId() == 25) {
                    AoaAttendsList attend = new AoaAttendsList();
                    attend.setHolidayDays(Double.valueOf(pro.getProcseeDays()));
                    attend.setHolidayStart(pro.getStartTime());
                    attend.setAttendsUserId(pro.getProcessUserId());
                    if (("请假申请").equals(typename)) {
                        attend.setStatusId(46L);
                    } else if (("出差申请").equals(typename)) {
                        attend.setStatusId(47L);
                    }
                    processService.insertAtt(attend);
                }
            }
        }
        processService.uodateObj(typename, pro, shen, userService.findOne(userId), reviewed);

        return "redirect:/audit";

    }
}
