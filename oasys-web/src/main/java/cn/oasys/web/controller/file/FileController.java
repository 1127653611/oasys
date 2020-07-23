package cn.oasys.web.controller.file;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/")
public class FileController {
    @RequestMapping("filemanage")
    public String usermanage(@SessionAttribute("userId")Long userid, Model model) {

        return "file/filemanage";
    }
}
