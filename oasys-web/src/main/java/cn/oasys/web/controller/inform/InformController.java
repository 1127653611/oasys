package cn.oasys.web.controller.inform;

import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
public class InformController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private NoticeService noticeService;
    @RequestMapping("infrommanage")
    public String inform(@RequestParam(value = "page", defaultValue = "0") int page, @SessionAttribute("userId") Long userId, Model model) {
        PageHelper.startPage(page,10);
        List<AoaNoticeList> noticeList=noticeService.findByUserId(userId);
        PageInfo<AoaNoticeList> page2=new PageInfo<>(noticeList);
        List<Map<String, Object>> list=noticeService.fengZhuang(noticeList);
        model.addAttribute("list", list);
        model.addAttribute("page", page2);
        //设置变量，需要load的url；
        model.addAttribute("url", "infrommanagepaging");
        return "inform/informmanage";
    }
    @RequestMapping("informedit")
    public String infromEdit(HttpServletRequest req, HttpSession session, Model model) {
        session.removeAttribute("noticeId");
        List<AoaTypeList> typeList = typeService.findByTypeModel("inform");
        List<AoaStatusList> statusList = statusService.findByStatusModel("inform");
        if (!StringUtils.isEmpty(req.getAttribute("errormess"))) {
            req.setAttribute("errormess", req.getAttribute("errormess"));
        }
        if (!StringUtils.isEmpty(req.getAttribute("success"))) {
            req.setAttribute("success", "数据保存成功");
        }
        req.setAttribute("typeList", typeList);
        req.setAttribute("statusList", statusList);

        return "inform/informedit";
    }

}
