package cn.oasys.web.common.Utils;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

public class Sort {
    public static void setSomething(String baseKey, Object type, Object status, Object time, Object icon, Model model) {
        if (!StringUtils.isEmpty(icon)) {
            model.addAttribute("icon", icon);
            if (!StringUtils.isEmpty(type)) {
                model.addAttribute("type", type);

                model.addAttribute("sort", "&type=" + type + "&icon=" + icon);

            }
            if (!StringUtils.isEmpty(status)) {
                model.addAttribute("status", status);

                model.addAttribute("sort", "&status=" + status + "&icon=" + icon);

            }
            if (!StringUtils.isEmpty(time)) {
                model.addAttribute("time", time);

                model.addAttribute("sort", "&time=" + time + "&icon=" + icon);

            }
        }

    }
}
