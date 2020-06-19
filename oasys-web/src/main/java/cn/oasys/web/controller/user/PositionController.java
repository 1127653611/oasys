package cn.oasys.web.controller.user;

import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.stuxuhai.jpinyin.PinyinException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class PositionController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @RequestMapping("positionmanage")
    public String positionmanage(Model model) {

        List<AoaPosition> positions =  positionService.findAll();

        model.addAttribute("positions",positions);

        return "user/positionmanage";
    }
    @RequestMapping(value = "positionedit" ,method = RequestMethod.GET)
    public String positioneditget(@RequestParam(value = "positionid",required=false) Long positionid, Model model){
        if(positionid!=null){

            AoaPosition position = positionService.findOne(positionid);
            AoaDept dept = deptService.findOne(position.getDeptid());
            model.addAttribute("positiondept",dept);
            model.addAttribute("position",position);
        }
        List<AoaDept> depts =  deptService.findAll();
        model.addAttribute("depts", depts);
        return "user/positionedit";
    }
    @RequestMapping(value = "positionedit" ,method = RequestMethod.POST)
    public String positioneditpost(AoaPosition position,Model model) throws PinyinException {
        positionService.save(position);
        List<AoaUser> aoaUsers= userService.findByPosition(position.getPositionId());
        for (AoaUser user : aoaUsers){
            user.setDeptId(position.getDeptid());
            userService.saveUser(user,false);
        }
        model.addAttribute("success",1);
        return "user/positionedit";
    }
    @RequestMapping("removeposition")
    public String removeposition(@RequestParam("positionid") Long positionid,Model model){
        positionService.delete(positionid);
        model.addAttribute("success",1);
        return "forward:/positionmanage";
    }
}
