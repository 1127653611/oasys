package cn.oasys.web.controller.mail;

import cn.oasys.web.common.Utils.User;
import cn.oasys.web.model.pojo.mail.AoaInMailList;
import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.model.pojo.mail.AoaMailnumber;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.adress.AdressService;
import cn.oasys.web.service.inter.file.FileService;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.process.ProcessService;
import cn.oasys.web.service.inter.system.StatusService;
import cn.oasys.web.service.inter.system.TypeService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@RequestMapping("/")
@Controller
public class MailController {
    @Autowired
    private User us;
    @Value("${img.rootpath}")
    private String rootpath;
    @Autowired
    private AdressService adressService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mservice;
    @Autowired
    private TypeService typeService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private UserService userService;

    @RequestMapping("accountmanage")
    public String account(@SessionAttribute("userId") Long userId, Model model,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        // 通过邮箱建立用户id找用户对象
        PageHelper.startPage(page, size);
        List<AoaMailnumber> pagelist = mservice.index(userId, null, model);
        PageInfo<AoaMailnumber> pageInfo = new PageInfo<>(pagelist);
        List<Map<String, Object>> list = mservice.up(pagelist);

        model.addAttribute("account", list);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "mailpaixu");
        return "mail/mailmanage";
    }

    @RequestMapping("mailpaixu")
    public String paixu(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        // 通过发布人id找用户
        //得到传过来的值
        String val = null;
        if (!StringUtil.isEmpty(request.getParameter("val"))) {

            val = request.getParameter("val");
        }
        PageHelper.startPage(page, size);
        List<AoaMailnumber> pagelist = mservice.index(userId, val, model);
        PageInfo<AoaMailnumber> pageInfo = new PageInfo<>(pagelist);
        List<Map<String, Object>> list = mservice.up(pagelist);

        model.addAttribute("account", list);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "mailpaixu");
        return "mail/mailtable";
    }

    @RequestMapping("addaccount")
    public String add(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req) {
        // 通过用户id找用户
        AoaUser tu = userService.findOne(userId);

        AoaMailnumber mailn = null;
        if (!StringUtil.isEmpty(req.getParameter("id"))) {
            Long id = Long.parseLong(req.getParameter("id"));
            AoaMailnumber mailnum = mservice.findOne(id);
            model.addAttribute("mails", mailnum);

        } else {
            List<AoaTypeList> typelist = typeService.findByTypeModel("aoa_mailnumber");
            List<AoaStatusList> statuslist = statusService.findByStatusModel("aoa_mailnumber");
            model.addAttribute("typelist", typelist);
            model.addAttribute("statuslist", statuslist);
        }
        model.addAttribute("username", tu.getUserName());
        return "mail/addaccounts";
    }

    @RequestMapping("saveaccount")
    public String save(HttpServletRequest request, AoaMailnumber mail, @SessionAttribute("userId") Long userId) {

        if (StringUtils.isEmpty(mail.getMailUserName())) {
            request.setAttribute("errormess", "昵称不能为空");
            return "forward:/addaccount";
        }
        if (StringUtils.isEmpty(mail.getMailAccount())) {
            request.setAttribute("errormess", "账号不能为空");
            return "forward:/addaccount";
        }
        if (StringUtils.isEmpty(mail.getPassword())) {
            request.setAttribute("errormess", "密码不能为空");
            return "forward:/addaccount";
        }
        if (!mail.getMailAccount().matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            request.setAttribute("errormess", "请正确填写账号");
            return "forward:/addaccount";
        }
        if (!mail.getMailAccount().matches(".*@qq.com") && !mail.getMailAccount().matches(".*@163.com")){
            request.setAttribute("errormess", "只支持163邮箱和QQ邮箱");
            return "forward:/addaccount";
        }
        request.setAttribute("mail", mail);
        if (Objects.isNull(mail.getMailNumberId())) {
            mail.setMailNumUserId(userId);
            mail.setMailCreateTime(new Date());
            mservice.saveAcount(mail);
        } else {
            mservice.saveAcount(mail);
        }
        request.setAttribute("success", "执行成功！");


        return "forward:/addaccount";
    }

    @RequestMapping("mail")
    public String index(@SessionAttribute("userId") Long userId, Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {

        //查找未读邮件
        List<AoaMailReciver> noreadlist = mservice.findByReadAndDelAndReciverId(false, false, userId);
        //查找创建了但是却没有发送的邮件
        List<AoaInMailList> nopushlist = mservice.findByPushAndDelAndMailUserid(false, false, userId);
        //查找发件条数
        List<AoaInMailList> pushlist = mservice.findByPushAndDelAndMailUserid(true, false, userId);
        //查找收件箱删除的邮件条数
        List<AoaMailReciver> rubbish = mservice.findByDelAndReciverId(true, userId);
        //分页及查找
        PageHelper.startPage(page, size);
        List<AoaMailReciver> pagelist = mservice.recive(userId, null, "收件箱", null);
        PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
        List<Map<String, Object>> maillist = mservice.mail(pagelist);

        model.addAttribute("page", pageInfo);
        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("noread", noreadlist.size());
        model.addAttribute("nopush", nopushlist.size());
        model.addAttribute("push", pushlist.size());
        model.addAttribute("rubbish", rubbish.size());
        model.addAttribute("mess", "收件箱");
        model.addAttribute("sort", "&title=收件箱");
        return "mail/mail";
    }

    @RequestMapping("amail")
    public String index3(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {


        PageHelper.startPage(page, size);
        List<AoaMailReciver> pagelist = null;
        List<AoaInMailList> pagemail = null;
        List<Map<String, Object>> maillist = null;
        String title = req.getParameter("title");
        String baseKey = req.getParameter("val");
        String type = req.getParameter("type");
        if (("收件箱").equals(title)) {
            //分页及查找
            pagelist = mservice.recive(userId, baseKey, title, type);
            maillist = mservice.mail(pagelist);
        } else if (("发件箱").equals(title)) {
            pagemail = mservice.inmail(userId, baseKey, title, type);
            maillist = mservice.maillist(pagemail);
        } else if (("草稿箱").equals(title)) {
            pagemail = mservice.inmail(userId, baseKey, title, type);
            maillist = mservice.maillist(pagemail);
        } else {
            //垃圾箱
            //分页及查找
            pagelist = mservice.recive(userId, baseKey, title, type);
            maillist = mservice.mail(pagelist);

        }

        if (!Objects.isNull(pagelist)) {
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);
        } else {
            PageInfo<AoaInMailList> pageInfo = new PageInfo<>(pagemail);
            model.addAttribute("page", pageInfo);

        }
        model.addAttribute("sort", "&title=" + title);
        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);
        return "mail/allmail";


    }

    @RequestMapping("mailtitle")
    public String serch(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<AoaMailReciver> pagelist = null;
        List<AoaInMailList> pagemail = null;
        List<Map<String, Object>> maillist = null;
        String title = req.getParameter("title");
        String baseKey = req.getParameter("val");
        String type = req.getParameter("type");
        if (("收件箱").equals(title)) {
            //分页及查找
            pagelist = mservice.recive(userId, baseKey, title, type);
            maillist = mservice.mail(pagelist);
        } else if (("发件箱").equals(title)) {
            pagemail = mservice.inmail(userId, baseKey, title, type);
            maillist = mservice.maillist(pagemail);
        } else if (("草稿箱").equals(title)) {
            pagemail = mservice.inmail(userId, baseKey, title, type);
            maillist = mservice.maillist(pagemail);
        } else {
            //垃圾箱
            //分页及查找
            pagelist = mservice.recive(userId, baseKey, title, type);
            maillist = mservice.mail(pagelist);

        }

        if (!Objects.isNull(pagelist)) {
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);
        } else {
            PageInfo<AoaInMailList> pageInfo = new PageInfo<>(pagemail);
            model.addAttribute("page", pageInfo);

        }
        model.addAttribute("sort", "&title=" + title);
        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);
        return "mail/mailbody";


    }

    /**
     * 查看邮件
     *
     * @ CreateDate    :  2020/8/18 15:36
     * @ parm          :
     * @ return        :
     */
    @RequestMapping("smail")
    public String index4(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model) {

        //邮件id
        Long id = Long.parseLong(req.getParameter("id"));
        //title
        String title = req.getParameter("title");
        //找到中间表信息
        if (("收件箱").equals(title) || ("垃圾箱").equals(title)) {
            AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, id);
            mailr.setIsRead(1);
            mservice.save(mailr);
        }

        //找到该邮件信息
        AoaInMailList mail = mservice.findOneInmail(id);
        String filetype = null;
        if (!Objects.isNull(mail.getMailFileId())) {
            AoaAttachmentList aoaAttachmentList = fileService.findByAttachmentId(mail.getMailFileId());
            String filepath = aoaAttachmentList.getAttachmentPath();
            if (aoaAttachmentList.getAttachmentType().startsWith("image")) {

                filetype = "img";
            } else {
                filetype = "appli";

            }
            model.addAttribute("filepath", filepath);
            model.addAttribute("filetype", filetype);
        }

        AoaUser pushuser = userService.findOne(mail.getMailInPushUserId());
        model.addAttribute("pushname", pushuser.getUserName());
        model.addAttribute("mail", mail);
        model.addAttribute("mess", title);
        model.addAttribute("file", mail.getMailFileId());
        return "mail/seemail";
    }

    @RequestMapping("wmail")
    public String index2(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {

        //得到编辑过来的id
        String id = null;
        if (!StringUtil.isEmpty(request.getParameter("id"))) {
            id = request.getParameter("id");
        }
        //回复那边过来的
        String huifu = null;

        if (!StringUtil.isEmpty(id)) {
            Long lid = Long.parseLong(id);
            //找到这条邮件
            AoaInMailList mail = mservice.findOneInmail(lid);
            if (!StringUtil.isEmpty(request.getParameter("huifu"))) {
                huifu = request.getParameter("huifu");
                model.addAttribute("title", huifu + mail.getMailTitle());
                model.addAttribute("content", mail.getContent());

            } else {
                model.addAttribute("title", mail.getMailTitle());
                model.addAttribute("content", mail.getContent());
            }
            model.addAttribute("status", statusService.findOne(mail.getMailStatusId()));
            model.addAttribute("type", typeService.findOne(mail.getMailType()));
            model.addAttribute("id", "回复");

        } else {

            List<AoaTypeList> typelist = typeService.findByTypeModel("aoa_in_mail_list");
            List<AoaStatusList> statuslist = statusService.findByStatusModel("aoa_in_mail_list");
            model.addAttribute("typelist", typelist);
            model.addAttribute("statuslist", statuslist);
            model.addAttribute("id", "新发");

        }
        //查看该用户所创建的有效邮箱账号
        List<AoaMailnumber> mailnum = mservice.findByStatusAndMailUserId(1L, userId);
        us.getAllUser(page, size, model);
        model.addAttribute("mailnum", mailnum);

        return "mail/wirtemail";
    }

    @RequestMapping("pushmail")
    public String push(@RequestParam("file") MultipartFile file, HttpServletRequest request, AoaInMailList mail, @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException {

        if (StringUtils.isEmpty(mail.getMailTitle())) {
            request.setAttribute("errormess", "邮件标题不能为空");
            return "redirect:/mail";
        }
        if (StringUtils.isEmpty(mail.getContent())) {
            request.setAttribute("errormess", "邮件类容不能为空");
            return "redirect:/mail";
        }

        String name = null;
        AoaAttachmentList attaid = null;
        StringTokenizer st = null;
        AoaMailnumber number = null;
        if (!StringUtil.isEmpty(request.getParameter("fasong"))) {
            name = request.getParameter("fasong");
        }
        if (!StringUtil.isEmpty(name)) {
            if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                attaid = fileService.upload(file, userId, "mail","atta");
            }
            //发送成功
            mail.setMailPush(1);

        } else {
            //存草稿
            mail.setMailPush(0);
            mail.setInReceiver(null);
        }
        if (attaid != null)
            mail.setMailFileId(attaid.getAttachmentId());
        mail.setMailCreateTime(new Date());
        mail.setMailInPushUserId(userId);
        mail.setMailDel(0);
        mail.setMailStar(0);

        Long inmail = Long.parseLong(request.getParameter("inmail"));
        if (inmail != 0) {
            number = mservice.findOne(inmail);
            mail.setMailNumberId(inmail);
        }

        AoaInMailList imail = mservice.saveMail(mail);
        if (!StringUtil.isEmpty(name)) {
            if (mservice.isContainChinese(mail.getInReceiver())) {
                // 分割任务接收人
                StringTokenizer st2 = new StringTokenizer(mail.getInReceiver(), ";");
                while (st2.hasMoreElements()) {
                    AoaUser reciver = userService.findByname(st2.nextToken());
                    AoaMailReciver mreciver = new AoaMailReciver();
                    mreciver.setMailId(imail.getMailId());
                    mreciver.setMailReciverId(reciver.getUserId());
                    mreciver.setIsRead(0);
                    mreciver.setIsStar(0);
                    mreciver.setIsDel(0);
                    mservice.save(mreciver);
                }
            } else {
                if (mail.getInReceiver().contains(";")) {
                    st = new StringTokenizer(mail.getInReceiver(), ";");
                } else {
                    st = new StringTokenizer(mail.getInReceiver(), "；");
                }

                while (st.hasMoreElements()) {
                    if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                        mservice.pushmail(number.getMailAccount(), number.getPassword(), st.nextToken(), number.getMailUserName(), mail.getMailTitle(),
                                mail.getContent(), attaid.getAttachmentPath(), attaid.getAttachmentName());

                    } else {

                        mservice.pushmail(number.getMailAccount(), number.getPassword(), st.nextToken(), number.getMailUserName(), mail.getMailTitle(),
                                mail.getContent(), null, null);
                    }
                }
            }
        }
        return "redirect:/mail";
    }


    @RequestMapping("alldelete")
    public String delete(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size) {
        String title = req.getParameter("title");
        List<AoaMailReciver> pagelist = null;
        List<AoaInMailList> pagemail = null;
        List<Map<String, Object>> maillist = null;
        //得到删除id
        String ids = req.getParameter("ids");
        if (("收件箱").equals(title)) {
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该用户联系邮件的中间记录
                AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));
                if (!Objects.isNull(mailr)) {
                    //把删除的字段改为1
                    mailr.setIsDel(1);
                    mservice.save(mailr);
                } else {
                    return "redirect:/notlimit";
                }

            }
            PageHelper.startPage(page, size);
            pagelist = mservice.recive(userId, null, title, null);
            maillist = mservice.mail(pagelist);

        } else if (("发件箱").equals(title)) {

            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该邮件
                AoaInMailList inmail = mservice.findByMailUseridAndMailId(userId, Long.parseLong(st.nextToken()));

                if (!Objects.isNull(inmail)) {
                    //把删除的字段改为1
                    inmail.setMailDel(1);
                    mservice.updateMail(inmail);
                } else {
                    return "redirect:/notlimit";
                }
                PageHelper.startPage(page, size);
                pagemail = mservice.inmail(userId, null, title, null);
                maillist = mservice.maillist(pagemail);
            }
        } else if (("草稿箱").equals(title)) {

            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该邮件
                AoaInMailList inmail = mservice.findByMailUseridAndMailId(userId, Long.parseLong(st.nextToken()));
                if (!Objects.isNull(inmail)) {
                    mservice.deleteImail(inmail);
                } else {
                    return "redirect:/notlimit";
                }
            }
            PageHelper.startPage(page, size);
            pagemail = mservice.inmail(userId, null, title, null);
            maillist = mservice.maillist(pagemail);
        } else {

            //垃圾箱
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                Long mailid = Long.parseLong(st.nextToken());
                //查看中间表关于这条邮件的del字段
                List<Integer> dellist = mservice.findbyMailId(mailid);

                //判断中间表中关于这条邮件是否还有del字段为false的
                if (dellist.contains(0)) {
                    AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, mailid);
                    if (!Objects.isNull(mailr)) {
                        mservice.deleteReci(mailr);
                    } else {
                        return "redirect:/notlimit";
                    }
                } else {
                    AoaInMailList imail = mservice.findOneInmail(mailid);
                    //判断这条邮件的del字段是为true
                    if (imail.getMailDel().equals(1)) {
                        List<AoaMailReciver> mreciver = mservice.findByMailId(mailid);
                        //循环删除关于这条邮件的所有中间表信息
                        for (AoaMailReciver mailreciver : mreciver) {
                            mservice.deleteReci(mailreciver);
                        }
                        mservice.deleteImail(imail);
                    } else {
                        //这条邮件的del字段为false，则删除中间表信息
                        AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, mailid);
                        if (!Objects.isNull(mailr)) {
                            mservice.deleteReci(mailr);
                        } else {
                            return "redirect:/notlimit";
                        }
                    }
                }
            }
            PageHelper.startPage(page, size);
            pagelist = mservice.recive(userId, null, title, null);
            maillist = mservice.mail(pagelist);
        }
        if (!Objects.isNull(pagelist)) {
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);

        } else {
            PageInfo<AoaInMailList> pageInfo = new PageInfo<>(pagemail);
            model.addAttribute("page", pageInfo);


        }

        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);
        return "mail/mailbody";

    }

    @RequestMapping("watch")
    public String watch(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {

        String title = req.getParameter("title");
        String ids = req.getParameter("ids");
        List<AoaMailReciver> pagelist = null;
        List<Map<String, Object>> maillist = null;

        if (("收件箱").equals(title)) {
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该用户联系邮件的中间记录
                AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));
                if (mailr.getIsRead().equals(0)) {

                    mailr.setIsRead(1);
                }

                mservice.save(mailr);
            }
            PageHelper.startPage(page, size);
            //分页及查找
            pagelist = mservice.recive(userId, null, title, null);
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);

        } else {
            //垃圾箱
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该用户联系邮件的中间记录
                AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));
                if (mailr.getIsRead().equals(0)) {
                    mailr.setIsRead(1);
                }
                mservice.save(mailr);
            }
            PageHelper.startPage(page, size);
            //分页及查找
            pagelist = mservice.recive(userId, null, title, null);
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);
        }
        maillist = mservice.mail(pagelist);

        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);
        return "mail/mailbody";
    }

    /**
     * 批量标星
     */
    @RequestMapping("star")
    public String star(@SessionAttribute("userId") Long userId, Model model, HttpServletRequest req,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size) {

        String title = req.getParameter("title");
        String ids = req.getParameter("ids");
        List<AoaMailReciver> pagelist = null;
        List<AoaInMailList> pagemail = null;
        List<Map<String, Object>> maillist = null;

        if (("收件箱").equals(title)) {
            StringTokenizer st = new StringTokenizer(ids, ",");

            while (st.hasMoreElements()) {

                //找到该用户联系邮件的中间记录
                AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));

                if (mailr.getIsStar().equals(0)) {
                    mailr.setIsStar(1);
                } else {
                    mailr.setIsStar(0);
                }
                mservice.save(mailr);

            }
            //分页及查找
            PageHelper.startPage(page, size);
            pagelist = mservice.recive(userId, null, title, null);
            maillist = mservice.mail(pagelist);
        } else if (("发件箱").equals(title)) {
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该邮件
                AoaInMailList inmail = mservice.findByMailUseridAndMailId(userId, Long.parseLong(st.nextToken()));
                if (inmail.getMailStar().equals(0)) {
                    inmail.setMailStar(1);
                } else {
                    inmail.setMailStar(0);
                }
                mservice.updateMail(inmail);
            }
            PageHelper.startPage(page, size);
            pagemail = mservice.inmail(userId, null, title, null);
            maillist = mservice.maillist(pagemail);
        } else if (("草稿箱").equals(title)) {
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该邮件
                AoaInMailList inmail = mservice.findByMailUseridAndMailId(userId, Long.parseLong(st.nextToken()));
                if (inmail.getMailStar().equals(0)) {
                    inmail.setMailStar(1);
                } else {
                    inmail.setMailStar(0);
                }
                mservice.updateMail(inmail);
            }
            PageHelper.startPage(page, size);
            pagemail = mservice.inmail(userId, null, title, null);
            maillist = mservice.maillist(pagemail);
        } else {
            //垃圾箱
            StringTokenizer st = new StringTokenizer(ids, ",");
            while (st.hasMoreElements()) {
                //找到该用户联系邮件的中间记录
                AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));
                if (mailr.getIsStar().equals(0)) {
                    mailr.setIsStar(1);
                } else {
                    mailr.setIsStar(0);
                }
                mservice.save(mailr);
            }
            //分页及查找
            PageHelper.startPage(page, size);
            pagelist = mservice.recive(userId, null, title, null);
            maillist = mservice.mail(pagelist);
        }

        if (!Objects.isNull(pagelist)) {
            PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
            model.addAttribute("page", pageInfo);

        } else {
            PageInfo<AoaInMailList> pageInfo = new PageInfo<>(pagemail);
            model.addAttribute("page", pageInfo);


        }
        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);
        return "mail/mailbody";
    }

    @RequestMapping("dele")
    public String edit(HttpServletRequest request, @SessionAttribute("userId") Long userId) {
        //得到账号id
        Long accountid = Long.parseLong(request.getParameter("id"));
        AoaMailnumber mail = mservice.findOne(accountid);
        if (mail.getMailNumUserId().equals(userId)) {
            mservice.dele(accountid);
        } else {
            return "redirect:/notlimit";
        }
        return "redirect:/accountmanage";
    }

    @RequestMapping("refresh")
    public String refresh(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        //查找用户
        String title = req.getParameter("title");
        List<AoaMailReciver> pagelist = null;
        List<Map<String, Object>> maillist = null;
        //得到恢复删除id
        String ids = req.getParameter("ids");

        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreElements()) {
            //找到该用户联系邮件的中间记录
            AoaMailReciver mailr = mservice.findbyReciverIdAndmailId(userId, Long.parseLong(st.nextToken()));
            if (!Objects.isNull(mailr)) {
                mailr.setIsDel(0);
                mservice.save(mailr);
            } else {
                return "redirect:/notlimit";
            }
        }
        //分页及查找
        PageHelper.startPage(page, size);
        pagelist = mservice.recive(userId, null, title, null);
        maillist = mservice.mail(pagelist);

        PageInfo<AoaMailReciver> pageInfo = new PageInfo<>(pagelist);
        model.addAttribute("page", pageInfo);
        model.addAttribute("maillist", maillist);
        model.addAttribute("url", "mailtitle");
        model.addAttribute("mess", title);

        return "mail/mailbody";

    }
}
