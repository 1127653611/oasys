package cn.oasys.web.controller.user;

import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.stuxuhai.jpinyin.PinyinException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class DeptController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;

    @RequestMapping("deptmanage")
    public String deptmanage(HttpServletRequest request) {
        List<AoaDept> depts = deptService.findAll();
        request.setAttribute("depts", depts);
        return "user/deptmanage";
    }

    @RequestMapping(value = "deptedit")
    public String changedept(@RequestParam(value = "dept", required = false) Long deptId, HttpServletRequest request) {
        if (deptId != null) {
            AoaDept dept = deptService.findOne(deptId);
            request.setAttribute("dept", dept);
        }
        return "user/deptedit";
    }

    @RequestMapping(value = "checkDept", method = RequestMethod.POST)
    public String adddept(AoaDept dept, HttpServletRequest request) {
        if (StringUtils.isEmpty(dept.getDeptName())) {
            request.setAttribute("errormess", "名称不能为空");
            return "user/deptedit";
        }
        deptService.save(dept);
        request.setAttribute("success", 1);
        return "user/deptedit";
    }

    @RequestMapping("deletdept")
    public String deletdept(@RequestParam("deletedeptid") Long deletedeptid) {
        deptService.delete(deletedeptid);
        return "forward:/deptmanage";

    }

    @RequestMapping("readdept")
    public String readdept(@RequestParam(value = "deptid") Long deptId, Model model) {
        AoaDept dept = deptService.findOne(deptId);
        AoaUser deptmanage = null;
        if(dept.getDeptmanager()!=null){
            deptmanage = userService.findOne(dept.getDeptmanager());
            model.addAttribute("deptmanage",deptmanage);
        }
        List<AoaDept> depts =  deptService.findAll();
        List<AoaPosition> positions = positionService.findByDeptidAndNameNotLike(deptId,"%经理");
        List<AoaUser> formaluser = new ArrayList<>();
        List<AoaUser> deptusers = userService.findByDept(dept.getDeptId());
        for (AoaUser deptuser : deptusers) {
            AoaPosition position = deptuser.getPosition();
            if(!position.getName().endsWith("经理")){
                formaluser.add(deptuser);
            }
        }
        model.addAttribute("positions",positions);
        model.addAttribute("depts",depts);
        model.addAttribute("deptuser",formaluser);
        model.addAttribute("dept",dept);
        model.addAttribute("isread",1);
        return "user/deptread";
    }
    @RequestMapping("selectdept")
    @ResponseBody
    public List<AoaPosition> selectdept(@RequestParam("selectdeptid") Long deptid){

        return positionService.findByDeptidAndNameNotLike(deptid,"%经理");
    }
    @RequestMapping("deptandpositionchange")
    public String deptandpositionchange(@RequestParam("positionid") Long positionid,
                                        @RequestParam("changedeptid") Long changedeptid,
                                        @RequestParam("userid") Long userid,
                                        @RequestParam("deptid") Long deptid,
                                        Model model) throws PinyinException {
        AoaUser user=userService.findOne(userid);
        user.setPositionId(positionid);
        user.setDeptId(changedeptid);
        userService.saveUser(user,false);
        return "forward:/readdept";
    }
    @RequestMapping("deptmanagerchange")
    public String deptmanagerchange(@RequestParam("oldpositionid") Long oldpostionid,
                                    @RequestParam(value="positionid",required=false) Long positionid,
                                    @RequestParam(value="changedeptid",required=false) Long changedeptid,
                                    @RequestParam(value="oldmanageid",required=false) Long oldmanageid,
                                    @RequestParam(value="newmanageid",required=false) Long newmanageid,
                                    @RequestParam("deptid") Long deptid,
                                    Model model) throws PinyinException {
        if(oldmanageid!=null){
            AoaUser user=userService.findOne(oldmanageid);
            user.setDeptId(changedeptid);
            user.setPositionId(positionid);
            userService.saveUser(user,false);
            if(newmanageid!=null){
                deptService.updateManage(deptid,newmanageid);
                AoaUser user1=userService.findOne(newmanageid);
                user1.setPositionId(oldpostionid);
                user1.setDeptId(deptid);
                userService.saveUser(user1,false);
                List<AoaUser> deptUsers=userService.findByDept(deptid);
                for (AoaUser u: deptUsers){
                    userService.saveUser(u,false);
                }
            }else{
                deptService.updateManage(deptid,null);
                List<AoaUser> deptUsers=userService.findByDept(deptid);
                for (AoaUser u: deptUsers){
                    userService.saveUser(u,false);
                }
            }
        }else{
            AoaPosition manage = positionService.findByDeptidAndNameLike(deptid, "%经理").get(0);
            deptService.updateManage(deptid,newmanageid);
            AoaUser user3=userService.findOne(newmanageid);
            user3.setPositionId(manage.getPositionId());
            user3.setDeptId(deptid);
            userService.saveUser(user3,false);
            List<AoaUser> deptUsers=userService.findByDept(deptid);
            for (AoaUser u: deptUsers){
                userService.saveUser(u,false);
            }
        }

        return "forward:/readdept";
    }

}
