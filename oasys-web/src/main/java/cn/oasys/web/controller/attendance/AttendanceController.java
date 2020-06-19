package cn.oasys.web.controller.attendance;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AttendanceController {
    @RequestMapping("attendceatt")
    public String testdasf(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "baseKey", required = false) String baseKey,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "time", required = false) String time,
                           @RequestParam(value = "icon", required = false) String icon, Model model) {
        //allsortpaging(request, session, page, baseKey, type, status, time, icon, model);
        return "attendce/attendceview";
    }
}
