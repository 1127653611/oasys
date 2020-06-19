package cn.oasys.web.controller.user;
import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import cn.oasys.web.service.inter.user.UserLoginRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserLoginController {
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
}
