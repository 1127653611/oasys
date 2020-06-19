package cn.oasys.web.controller.process;

import cn.oasys.web.model.pojo.process.AoaSubject;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.system.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class ProcessController {
    @Autowired
    private ProcessService processService;
    @Autowired
    private TypeService typeService;
    @RequestMapping("newprocess")
    public String newProcess(){
        return "process/procedure";
    }
    @RequestMapping("burse")
    public String bursement(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size){
        List<AoaTypeList> uplist=typeService.findByTypeModel("aoa_bursement");
        //查找费用科目生成树
        List<AoaSubject> second=processService.findByParentId(1L);
        List<AoaSubject> sublist=processService.findByParentIdNot(1L);
        return "process/bursement";
    }
}
