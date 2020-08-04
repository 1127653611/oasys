package cn.oasys.web.controller.process;

import cn.oasys.web.model.pojo.process.AoaBursement;
import cn.oasys.web.model.pojo.process.AoaSubject;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.util.List;

@Controller
@RequestMapping("/")
public class ProcessController {
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
        processService.index6(model, userId, page, size);
        model.addAttribute("second", second);
        model.addAttribute("sublist", sublist);
        model.addAttribute("uplist", uplist);
        return "process/bursement";
    }
    @RequestMapping("names")
    public String serch(Model model,HttpServletRequest req, @SessionAttribute("userId") Long userId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "title", required = false) String title){
        PageHelper.startPage(page,size);
        List<AoaUser> userlist;
        if (StringUtils.isEmpty(title)) {
            System.out.println(1);
            userlist = userService.findAll();
        } else {
            System.out.println(2);
            String key = "%" + title + "%";
            userlist = userService.findAllByLike(key);
        }
        PageInfo<AoaUser> pageuser=new PageInfo<>(userlist);
        Iterable<AoaDept> deptlist = deptService.findAll();
        // 查职位表
        Iterable<AoaPosition> poslist = positionService.findAll();
        model.addAttribute("emplist", userlist);
        model.addAttribute("page", pageuser);
        model.addAttribute("deptlist", deptlist);
        model.addAttribute("poslist", poslist);
        model.addAttribute("url", "names");
        return "common/recivers";

    }
    @RequestMapping("apply")
    public String apply(@RequestParam("filePath") MultipartFile filePath, HttpServletRequest req , AoaBursement bu,
                        @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException {

        return "redirect:/xinxeng";
    }
}
