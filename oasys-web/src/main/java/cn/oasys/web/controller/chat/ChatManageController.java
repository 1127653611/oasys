package cn.oasys.web.controller.chat;

import cn.oasys.web.model.pojo.discuss.AoaDiscussList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.discuss.DiscussService;
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

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class ChatManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussService discussService;
    @RequestMapping("adminmanage")
    public String adminManage(@RequestParam(value="page",defaultValue="0") int page, HttpSession session,
                              @SessionAttribute("userId") Long userId, Model model){

        return "chat/chatmanage";
    }
    @RequestMapping("chatlist")
    public String chatList(@RequestParam(value="page",defaultValue="0") int page,Model model,HttpSession session){
        PageHelper.startPage(page,10);
        List<AoaDiscussList> list=discussService.paging(null,null);
        PageInfo<AoaDiscussList> page2=new PageInfo<>(list);
        model.addAttribute("list",discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/seetable");
        model.addAttribute("name", "讨论列表");
        session.removeAttribute("returnUrl");
        session.setAttribute("returnUrl", "chatlist");
        return "chat/chatmanage";
    }
    /**
     * 查看列表页面的controller，分页处理
     * @return
     */
    @RequestMapping("seetable")
    public String seeTable(@RequestParam(value="page",defaultValue="0") int page,
                           @RequestParam(value="baseKey",required=false) String baseKey,
                           @RequestParam(value="type",required=false) String type,
                           @RequestParam(value="time",required=false) String time,
                           @RequestParam(value="visitnum",required=false) String visitnum,
                           @RequestParam(value="icon",required=false) String icon,
                           @SessionAttribute("userId") Long userId,Model model){
       // setSomething(baseKey, type, time, visitnum,  icon, model);
        //传过去的userid为null；
        PageHelper.startPage(page,10);
        List<AoaDiscussList> list=discussService.paging(baseKey,null);
        PageInfo<AoaDiscussList> page2=new PageInfo<>(list);
        model.addAttribute("list",discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/seetable");
        model.addAttribute("name", "讨论列表");
        return "chat/chattable";
    }
    @RequestMapping("seediscuss")
    public String seeDiscuss(@RequestParam(value="id") Long id,@RequestParam(value="pageNumber") Integer pageNumber,HttpSession session){
        discussService.addOneDiscuss(id);
        session.removeAttribute("id");
        session.setAttribute("id", id);
        session.setAttribute("pageNumber", pageNumber);
        return "redirect:/replymanage";
    }
    @RequestMapping("replymanage")
    public String replyManage(Model model,HttpSession session,
                              @RequestParam(value="page",defaultValue="0") int page,
                              @RequestParam(value="size",defaultValue="5") int size,
                              @SessionAttribute("userId") Long userId){
        Long id=Long.parseLong(session.getAttribute("id")+"");
        AoaUser user=userService.findOne(userId);
        AoaDiscussList discuss=discussService.findOne(id);
        //用来处理vote相关的数据
        discussService.voteServiceHandle(model, user, discuss);
        if(user.getSuperman()==1){
            model.addAttribute("manage", "具有管理权限");
        }else{
            if(Objects.equals(user.getUserId(), discuss.getAoaUser().getUserId())){
                model.addAttribute("manage", "具有管理权限");
            }
        }
        discussService.setDiscussMess(model, id,userId,page,size);
        return "chat/replaymanage";
    }

}
