package cn.oasys.web.controller.user;

import cn.oasys.web.common.Utils.MD5Util;
import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.note.NoteService;
import cn.oasys.web.service.inter.notice.NoticeService;
import cn.oasys.web.service.inter.user.DeptService;
import cn.oasys.web.service.inter.user.PositionService;
import cn.oasys.web.service.inter.user.RoleService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.sun.mail.imap.protocol.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("usermanage")
    public String usermanage(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageHelper.startPage(page, size);
        List<AoaUser> users = userService.findByidLock(0);
        PageInfo<AoaUser> userspage = new PageInfo<>(users);
        model.addAttribute("users", users);
        model.addAttribute("page", userspage);
        model.addAttribute("url", "usermanagepaging");
        return "user/usermanage";
    }

    @RequestMapping("usermanagepaging")
    public String userPaging(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "baseKey", required = false) String baseKey
    ) {
        PageHelper.startPage(page, size);
        List<AoaUser> users;
        if (StringUtils.isEmpty(baseKey)) {
            users = userService.findByidLock(0);
        } else {
            model.addAttribute("baseKey",baseKey);
            String key = "%" + baseKey + "%";
            users = userService.findByidLockLike(0, key);
        }
        PageInfo<AoaUser> userspage = new PageInfo<>(users);
        model.addAttribute("users", users);
        model.addAttribute("page", userspage);
        model.addAttribute("url", "usermanagepaging");
        return "user/usermanagepaging";
    }

    @RequestMapping(value = "useredit", method = RequestMethod.GET)
    public String usereditget(@RequestParam(value = "userid", required = false) Long userid, Model model) {
        if (userid != null) {
            AoaUser user = userService.findOne(userid);
            model.addAttribute("where", "xg");
            model.addAttribute("user", user);
        }

        List<AoaDept> depts = deptService.findAll();
        List<AoaPosition> positions = positionService.findAll();
        List<AoaRole> roles = roleService.findAll();

        model.addAttribute("depts", depts);
        model.addAttribute("positions", positions);
        model.addAttribute("roles", roles);
        return "user/edituser";
    }

    @RequestMapping(value = "useredit", method = RequestMethod.POST)
    public String usereditpost(AoaUser user, @RequestParam(value = "isbackpassword", required = false) boolean isbackpassword,
                               Model model) throws PinyinException {
        AoaUser u1 = userService.findOne(user.getUserId());
        boolean istrue = false;
        AoaPosition aoaPosition = positionService.findOne(user.getPositionId());
        if (aoaPosition.getDeptid()==user.getDeptId()){
            istrue=true;
        }
        if (!istrue) {
            model.addAttribute("errormess", "部门职位不匹配，修改失败");
            return "user/edituser";
        }
        AoaPosition aoaPosition1 = positionService.findOne(u1.getPositionId());
        if (u1.getPositionId() != user.getPositionId()) {
            if (aoaPosition.getName().endsWith("经理") || aoaPosition1.getName().endsWith("经理")) {
                model.addAttribute("errormess", "不能操作经理职位");
                return "user/edituser";
            }
        }
        user.setRoleId(deptService.getRoleid(user));
        userService.saveUser(user, isbackpassword);
        model.addAttribute("success", 1);
        return "user/edituser";
    }

    @RequestMapping("deleteuser")
    public String deleteuser(@RequestParam("userid") Long userid, Model model) throws PinyinException {
        AoaUser user = userService.findOne(userid);
        AoaDept dept = deptService.findOne(user.getDeptId());
        if (dept.getDeptmanager() == user.getUserId()) {
            model.addAttribute("errormess", "请先去除经理职位");
            return "user/edituser";
        }
        user.setIsLock(1);

        userService.saveUser(user, false);

        model.addAttribute("success", 1);
        return "user/edituser";

    }

    @RequestMapping("userpanel")
    public String index(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        AoaUser user = userService.findOne(userId);
        PageHelper.startPage(page, size);
        List<AoaNotepaper> aoaNotepapers = noteService.findByUserIdOrderByCreateTimeDesc1(userId);
        PageInfo<AoaNotepaper> aoaNotepaperPageInfo = new PageInfo<>(aoaNotepapers);
        List<AoaMailReciver> maillist = mailService.findByReadAndDelAndReciverId(false, false, userId);
        Integer noticelist = noticeService.findByReadAndUserId(false, userId);
        model.addAttribute("deptname", user.getDept().getDeptName());
        model.addAttribute("positionname", user.getPosition().getName());
        model.addAttribute("user", user);
        model.addAttribute("noticelist", noticelist);
        model.addAttribute("maillist", maillist.size());
        model.addAttribute("notepaperlist", aoaNotepapers);
        model.addAttribute("page", aoaNotepaperPageInfo);
        model.addAttribute("url", "panel");
        return "user/userpanel";
    }

    @RequestMapping("panel")
    public String index(@SessionAttribute("userId") Long userId, Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        AoaUser user = userService.findOne(userId);
        //找便签
        PageHelper.startPage(page, size);
        List<AoaNotepaper> list = noteService.findByUserIdOrderByCreateTimeDesc1(userId);
        PageInfo<AoaNotepaper> aoaNotepaperPageInfo = new PageInfo<>(list);
        model.addAttribute("notepaperlist", list);
        model.addAttribute("page", aoaNotepaperPageInfo);
        model.addAttribute("url", "panel");
        return "user/panel";
    }
    @RequestMapping("writep")
    public String savepaper(AoaNotepaper npaper,@SessionAttribute("userId") Long userId,@RequestParam(value="concent",required=false)String concent){
        AoaUser user=userService.findOne(userId);
        npaper.setCreateTime(new Date());
        npaper.setNotepaperUserId(userId);
        if(npaper.getTitle()==null|| npaper.getTitle().equals(""))
            npaper.setTitle("无标题");
        if(npaper.getConcent()==null|| npaper.getConcent().equals(""))
            npaper.setConcent(concent);
        noteService.saveNotePaper(npaper);

        return "redirect:/userpanel";
    }
    @RequestMapping("saveuser")
    public String saveemp(@RequestParam("filePath") MultipartFile filePath, HttpServletRequest request, AoaUser user,
                          BindingResult br, @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException {
        if (StringUtils.isEmpty(user.getUserName())) {
            request.setAttribute("errormess", "用户名不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getUserTel())) {
            request.setAttribute("errormess", "电话不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getEamil())) {
            request.setAttribute("errormess", "邮箱不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getRealName())) {
            request.setAttribute("errormess", "真实姓名不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getAddress())) {
            request.setAttribute("errormess", "地址不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getUserEdu())) {
            request.setAttribute("errormess", "学历不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getUserSchool())) {
            request.setAttribute("errormess", "毕业院校不能为空");
            return "forward:/userpanel";
        }
        if (StringUtils.isEmpty(user.getBank())) {
            request.setAttribute("errormess", "卡号不能为空");
            return "forward:/userpanel";
        }
        if (!StringUtils.isEmpty(filePath.getOriginalFilename())){
            if (!filePath.getOriginalFilename().matches(".*(.jpg|.gif|.bmp|.png)$")){
                request.setAttribute("errormess", "头像格式不对");
                return "forward:/userpanel";
            }
        }
        String imgpath = fileService.upload(filePath);
        user.setUserId(userId);
        user.setPassword(MD5Util.getMD5String(user.getPassword()));
        if(!StringUtil.isEmpty(imgpath)){
            user.setImgPath(imgpath);

        }
        request.setAttribute("users", user);
        userService.update(user);
        request.setAttribute("success", "执行成功！");
        return "forward:/userpanel";

    }
}
