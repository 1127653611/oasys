package cn.oasys.web.controller.system;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.service.inter.system.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class StatusSysController {
    @Autowired
    private StatusService statusService;
    @RequestMapping("testsysstatus")
    public String testsysstatus(HttpServletRequest req){
        Iterable<AoaStatusList> statusList=statusService.findAll();
        req.setAttribute("statusList", statusList);
        return "systemcontrol/statusmanage";
    }
    @RequestMapping("statusedit")
    public String typeEdit(HttpServletRequest req){
        if(!StringUtils.isEmpty(req.getParameter("statusid"))){
            Long statusid=Long.parseLong(req.getParameter("statusid"));
            AoaStatusList statusList= statusService.findOne(statusid);
            req.setAttribute("status", statusList);
        }
        return "systemcontrol/statusedit";
    }
    @RequestMapping("statuscheck")
    public String testMess(AoaStatusList aoaStatusList,HttpServletRequest req){
        if (StringUtils.isEmpty(aoaStatusList.getStatusName())){
            req.setAttribute("errormess", "名字不能为空");
            return "systemcontrol/statusedit";
        }
        statusService.save(aoaStatusList);
        req.setAttribute("success", "后台验证成功");
        return "systemcontrol/statusedit";
    }
    @RequestMapping("deletestatus")
    public String deleteStatus(HttpServletRequest req){
        Long statusId=Long.parseLong(req.getParameter("id"));
        statusService.deleteStatus(statusId);
        return "forward:/testsysstatus";
    }
    @RequestMapping("statustable")
    public String statusTable(HttpServletRequest req){
        if(!StringUtils.isEmpty(req.getParameter("name"))){
            String name="%"+req.getParameter("name")+"%";
            req.setAttribute("statusList",statusService.findByStatusNameLikeOrStatusModelLike(name));
        }
        else{
            Iterable<AoaStatusList> statusList=statusService.findAll();
            req.setAttribute("statusList", statusList);
        }
        return "systemcontrol/statustable";
    }


}
