package cn.oasys.web.controller.chat;

import cn.oasys.web.common.Msg;
import cn.oasys.web.model.pojo.discuss.*;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.discuss.DiscussService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/")
public class ChatManageController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussService discussService;

    @RequestMapping("adminmanage")
    public String adminManage(@RequestParam(value = "page", defaultValue = "0") int page,
                              @SessionAttribute("userId") Long userId, Model model) {
        boolean issuperman = false;
        AoaUser user = userService.findOne(userId);
        if (user.getSuperman() == 1) {
            issuperman = true;
        }
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(null, userId, 1, issuperman,null,null,null);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/chattable");
        model.addAttribute("manage", "manage");
        model.addAttribute("name", "超级管理员");
        model.addAttribute("ro", 1);
        return "chat/chatmanage";
    }
    @RequestMapping("chattable")
    public String chatTable(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "baseKey", required = false) String baseKey,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "time", required = false) String time,
                            @RequestParam(value = "visitnum", required = false) String visitnum,
                            @RequestParam(value = "icon", required = false) String icon,
                            @SessionAttribute("userId") Long userId, Model model) {
        setSomething(baseKey, type, time, visitnum,  icon, model);
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(baseKey, userId, 1, true,type,time,visitnum);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/chattable");
        model.addAttribute("manage", "manage");
        model.addAttribute("name", "超级管理员");
        model.addAttribute("ro", 1);
        return "chat/chattable";
    }
    @RequestMapping("chatmanage")
    public String chatManage(@RequestParam(value = "page", defaultValue = "0") int page,
                             @SessionAttribute("userId") Long userId, Model model, HttpSession session) {
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(null, userId, 2, false,null,null,null);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("me", "me");
        model.addAttribute("page", page2);
        model.addAttribute("url", "/metable");
        model.addAttribute("manage", "manage");
        model.addAttribute("name", "我的管理");
        model.addAttribute("ro", 2);
        return "chat/chatmanage";
    }
    @RequestMapping("metable")
    public String meTable(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "baseKey", required = false) String baseKey,
                          @RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "time", required = false) String time,
                          @RequestParam(value = "visitnum", required = false) String visitnum,
                          @RequestParam(value = "icon", required = false) String icon,
                          @SessionAttribute("userId") Long userId, Model model) {
        setSomething(baseKey, type, time, visitnum,  icon, model);
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(baseKey, userId, 2, false,type,time,visitnum);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("me", "me");
        model.addAttribute("url", "/metable");
        model.addAttribute("manage", "manage");
        model.addAttribute("name", "我的管理");
        model.addAttribute("ro", 2);
        return "chat/chattable";
    }
    @RequestMapping("chatlist")
    public String chatList(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(null, null, 3, false,null,null,null);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/seetable");
        model.addAttribute("name", "讨论列表");
        model.addAttribute("ro", 3);
        return "chat/chatmanage";
    }
    @RequestMapping("seetable")
    public String seeTable(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "baseKey", required = false) String baseKey,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "time", required = false) String time,
                           @RequestParam(value = "visitnum", required = false) String visitnum,
                           @RequestParam(value = "icon", required = false) String icon,
                           @SessionAttribute("userId") Long userId, Model model) {
        setSomething(baseKey, type, time, visitnum,  icon, model);
        PageHelper.startPage(page, 10);
        List<AoaDiscussList> list = discussService.paging(baseKey, null, 3, false,type,time,visitnum);
        PageInfo<AoaDiscussList> page2 = new PageInfo<>(list);
        model.addAttribute("list", discussService.packaging(list));
        model.addAttribute("page", page2);
        model.addAttribute("url", "/seetable");
        model.addAttribute("name", "讨论列表");
        model.addAttribute("ro", 3);
        return "chat/chattable";
    }


    private void setSomething(String baseKey, String type, String time, String visitnum,String icon,
                              Model model) {
        if(!StringUtils.isEmpty(icon)){
            model.addAttribute("icon", icon);
            if(!StringUtils.isEmpty(type)){
                model.addAttribute("type", type);
                if("1".equals(type)){
                    model.addAttribute("sort", "&type=1&icon="+icon);
                }else{
                    model.addAttribute("sort", "&type=0&icon="+icon);
                }
            }
            if(!StringUtils.isEmpty(visitnum)){
                model.addAttribute("visitnum", visitnum);
                if("1".equals(visitnum)){
                    model.addAttribute("sort", "&visitnum=1&icon="+icon);
                }else{
                    model.addAttribute("sort", "&visitnum=0&icon="+icon);
                }
            }
            if(!StringUtils.isEmpty(time)){
                model.addAttribute("time", time);
                if("1".equals(time)){
                    model.addAttribute("sort", "&time=1&icon="+icon);
                }else{
                    model.addAttribute("sort", "&time=0&icon="+icon);
                }
            }
        }
        if(!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey", baseKey);
        }
    }
    /**
     * 查看列表页面的controller，分页处理
     *
     * @return
     */



    @RequestMapping("seediscuss")
    public String seeDiscuss(@RequestParam(value = "id") Long id, @RequestParam(value = "pageNumber") Integer pageNumber,
                             HttpSession session, @RequestParam(value = "ro", defaultValue = "3") String ro) {
        discussService.addOneDiscuss(id);
        session.setAttribute("pageNumber", pageNumber);
        session.removeAttribute("id");
        session.setAttribute("id", id);
        session.removeAttribute("ro");
        session.setAttribute("ro", ro);
        return "redirect:/replymanage";
    }

    @RequestMapping("replymanage")
    public String replyManage(Model model, HttpSession session,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @SessionAttribute("userId") Long userId) {
        Long id = Long.parseLong(session.getAttribute("id") + "");
        AoaUser user = userService.findOne(userId);
        AoaDiscussList discuss = discussService.findOne(id);
        //用来处理vote相关的数据
        discussService.voteServiceHandle(model, user, discuss);

        discussService.discussHandle(model, id, userId, page, size, null, null);
        if (!StringUtils.isEmpty(session.getAttribute("ro"))) {
            String ro = (String) session.getAttribute("ro");
            if (ro.equals("1")) model.addAttribute("ro", "/adminmanage");
            if (ro.equals("2")) model.addAttribute("ro", "/chatmanage");
            if (ro.equals("3")) model.addAttribute("ro", "/chatlist");
        }
        return "chat/replaymanage";
    }

    @RequestMapping("/replypaging")
    public String replyPaging(HttpServletRequest req,
                              @RequestParam(value = "selecttype") Long selecttype,
                              @RequestParam(value = "selectsort") Long selectsort,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @SessionAttribute("userId") Long userId, Model model) {
        Long num = Long.parseLong(req.getParameter("num"));
        discussService.discussHandle(model, num, userId, page, size, selecttype, selectsort);

        return "chat/replytable";
    }

    @RequestMapping("likeuserload")
    public String likeUserLoad(HttpServletRequest req, Model model, @SessionAttribute("userId") Long userId) {
        Long replyId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");
        if ("discuss".equals(module)) {
            //处理讨论表的点赞，刷新
            discussService.likeThisFun(userId, replyId, "discuss", model, null);
            return "chat/discusslike";
        } else if ("reply".equals(module)) {
            String rightNum = req.getParameter("rightNum");
            discussService.likeThisFun(userId, replyId, "reply", model, rightNum);
            return "chat/replylike";
        } else {
            return null;
        }
    }

    @RequestMapping("replyhandle")
    @ResponseBody
    public Msg reply(HttpServletRequest req,
                     @RequestParam(value = "page", defaultValue = "0") int page,
                     @RequestParam(value = "size", defaultValue = "5") int size,
                     @SessionAttribute("userId") Long userId, Model model) {
        if (StringUtils.isEmpty(req.getParameter("comment"))) {
            return Msg.fail().add("msg", "评论不能为空");
        }

        Long discussId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");    //用来判断是保存在哪个表
        String comment = req.getParameter("comment");
        return discussService.saveReply(discussId, module, comment, userId);

    }

    @RequestMapping("writechat")
    public String writeChat(HttpServletRequest req, @SessionAttribute(value = "userId") Long userId, Model model) {
        HttpSession session = req.getSession();
        if (!StringUtils.isEmpty(req.getParameter("id"))) {
            //修改界面的显示
            Long disId = Long.parseLong(req.getParameter("id"));
            AoaDiscussList discuss = discussService.findOne(disId);
            //回填投票的信息
            if (!Objects.isNull(discuss.getAoaVoteList())) {
                model.addAttribute("voteList", discuss.getAoaVoteList());
                List<AoaVoteTitles> voteTitles = discuss.getAoaVoteList().getAoaVoteTitles();
                model.addAttribute("voteTitles", voteTitles);
            }
            //回填投票标题的信息
            model.addAttribute("discuss", discuss);
            model.addAttribute("typeName", typeService.findOne(discuss.getTypeId()).getTypeName());
        }
        if (!StringUtils.isEmpty(req.getAttribute("success"))) {
            model.addAttribute("success", "成功了");
        }

        AoaUser user = userService.findOne(userId);
        List<AoaTypeList> typeList = typeService.findByTypeModel("chat");
        model.addAttribute("typeList", typeList);
        model.addAttribute("user", user);
        return "chat/writechat";
    }

    @RequestMapping("adddiscuss")
    public String addDiscuss(HttpServletRequest req, AoaDiscussList discussList, AoaVoteList voteList) {
        HttpSession session = req.getSession();
        Long userId = Long.parseLong(session.getAttribute("userId") + "");
        AoaUser user = userService.findOne(userId);
        if (StringUtils.isEmpty(discussList.getTitle())) {
            req.setAttribute("errormess", "标题不能为空");
            return "forward:/writechat";
        }
        if (!StringUtils.isEmpty(discussList.getDiscussId())) {
            discussList.setModifyTime(new Date());
            discussService.saveDiscuss(discussList, voteList, null, true);
            req.setAttribute("success", "成功了");
            return "forward:/writechat";
        } else {
            discussList.setDiscussUserId(userId);
            Set<AoaVoteTitles> voteTitles = new HashSet<>();
            if (!StringUtils.isEmpty(req.getParameter("votetitle"))) {
                String[] title2 = req.getParameterValues("votetitle");
                String[] colors = req.getParameterValues("votecolor");
                //处理投票标题
                for (int i = 0; i < colors.length; i++) {
                    AoaVoteTitles voteTitle = new AoaVoteTitles();
                    voteTitle.setColor(colors[i]);
                    voteTitle.setTitle(title2[i]);
                    voteTitles.add(voteTitle);
                }
                if (voteTitles.size() < 2) {
                    req.setAttribute("errormess", "至少两个投票选项");
                    return "forward:/writechat";
                }
            }
            discussList.setVisitNum(0);
            discussList.setCreateTime(new Date());
            discussService.saveDiscuss(discussList, voteList, voteTitles, false);
            req.setAttribute("success", "成功了");
            return "forward:/writechat";
        }
    }

    @RequestMapping("votehandle")
    @ResponseBody
    public Msg voteHandle(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model) {
        Long discussId = Long.parseLong(req.getParameter("discussId"));
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        Integer selectOne = Integer.parseInt(req.getParameter("selectType"));
        AoaDiscussList discuss = discussService.findOne(discussId);
        Date date = new Date();
        if (date.getTime() < discuss.getAoaVoteList().getStartTime().getTime()) {
            return Msg.fail().add("msg", "投票还没开始");
        } else if (date.getTime() > discuss.getAoaVoteList().getEndTime().getTime()) {
            return Msg.fail().add("msg", "投票已经结束");
        }
        List<AoaVoteTitleUser> li = discussService.findByVoteTitlesAndUser(titleId, userId);
        if (!li.isEmpty()) {
            return Msg.fail().add("msg", "已经投过票了");
        }
        AoaVoteTitleUser aoaVoteTitleUser = new AoaVoteTitleUser(discuss.getVoteId(), userId, titleId);
        discussService.saveVotetittleUser(aoaVoteTitleUser);
        return Msg.success();
    }

    @RequestMapping("votehandle1")
    public String voteHandle1(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model) {
        Long discussId = Long.parseLong(req.getParameter("discussId"));
        AoaUser user = userService.findOne(userId);
        AoaDiscussList discuss = discussService.findOne(discussId);
        discussService.voteServiceHandle(model, user, discuss);
        model.addAttribute("discuss", discuss);
        return "chat/votetable";
    }

    @RequestMapping("/replydelete")
    public String replyDelete(HttpServletRequest req,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @SessionAttribute("userId") Long userId, Model model,
                              @RequestParam(value = "selecttype") Long selecttype,
                              @RequestParam(value = "selectsort") Long selectsort) {
        AoaUser user = userService.findOne(userId);
        Long num = Long.parseLong(req.getParameter("num"));
        AoaDiscussList discuss = discussService.findOne(num);
        Long discussId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");    //用来判断是保存在哪个表
        if ("reply".equals(module)) {
            if (user.getSuperman() != 1) {
                if (!Objects.equals(userId, discuss.getAoaUser().getUserId())) {
                    AoaReplyList replyList=discussService.findReply(discussId);
                    if (!Objects.equals(userId,replyList.getReplyUserId())) {
                        return "redirect:/notlimit";
                    }
                }
            }
            discussService.deleteCommentByReply(discussId);
            discussService.deleteLoveByreply(discussId);
            discussService.deleteReply(discussId);

        } else if ("comment".equals(module)) {
            if (user.getSuperman() != 1) {
                if (!Objects.equals(userId, discuss.getAoaUser().getUserId())) {
                    AoaCommentList conment=discussService.findConment(discussId);
                    if (!Objects.equals(userId,conment.getCommentUserId())) {
                        return "redirect:/notlimit";
                    }
                }
            }
            discussService.deleteComment(discussId);
        }
        discussService.discussHandle(model, num, userId, page, size,selecttype,selectsort);
        return "chat/replytable";
    }
    @RequestMapping("deletediscuss")
    public String deletediscuss(HttpServletRequest req,@SessionAttribute("userId") Long userId){
        String name=req.getParameter("name");
        Long discussId=Long.parseLong(req.getParameter("discussId"));
        Integer page=Integer.parseInt(req.getParameter("page"));
        AoaDiscussList discuss=discussService.findOne(discussId);
        AoaUser user=userService.findOne(userId);
            if (user.getSuperman() != 1) {
                if (!Objects.equals(userId, discuss.getAoaUser().getUserId())) {
                        return "redirect:/notlimit";
                }
            }

        discussService.deleteDiscuss(discuss);
        if("超级管理员".equals(name)){
            return "forward:/adminmanage?page="+page;
        }else if("我的管理".equals(name)){
            return "forward:/chatmanage?page="+page;
        }else{
            return "forward:/chatlist?page="+page;
        }
    }
}
