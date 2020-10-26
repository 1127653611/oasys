package cn.oasys.web.service.impl.mail;

import cn.oasys.web.model.dao.mail.AoaInMailListMapper;
import cn.oasys.web.model.dao.mail.AoaMailReciverMapper;
import cn.oasys.web.model.dao.mail.AoaMailnumberMapper;
import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.pojo.mail.AoaInMailList;
import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.model.pojo.mail.AoaMailnumber;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.service.inter.mail.MailService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Autowired
    private AoaStatusListMapper aoaStatusListMapper;
    @Autowired
    private AoaMailnumberMapper aoaMailnumberMapper;
    @Autowired
    private AoaMailReciverMapper aoaMailReciverMapper;
    @Autowired
    private AoaInMailListMapper aoaInMailListMapper;
    @Value("${img.rootpath}")
    private String rootpath;


    @Override
    public List<AoaMailReciver> findByReadAndDelAndReciverId(boolean isread, boolean isdel, long userid) {
        return aoaMailReciverMapper.findByReadAndDelAndReciverId(isread, isdel, userid);
    }

    @Override
    public List<AoaMailnumber> index(Long userId, String val, Model model) {
        List<AoaMailnumber> account = null;
        if (StringUtil.isEmpty(val)) {
            account = aoaMailnumberMapper.findByMailUserId(userId);
        } else if (("类型").equals(val)) {
            account = aoaMailnumberMapper.findByMailUserIdOrderByMailType(userId);
            model.addAttribute("sort", "&val=" + val);
        } else if (("状态").equals(val)) {
            account = aoaMailnumberMapper.findByMailUserIdOrderByStatus(userId);
            model.addAttribute("sort", "&val=" + val);
        } else if (("创建时间").equals(val)) {
            account = aoaMailnumberMapper.findByMailUserIdOrderByMailCreateTimeDesc(userId);
            model.addAttribute("sort", "&val=" + val);
        } else {
            //名字的模糊查询
            account = aoaMailnumberMapper.findByMailUserNameLikeAndMailUserId("%" + val + "%", userId);
            model.addAttribute("sort", "&val=" + val);
        }
        return account;
    }

    @Override
    public List<Map<String, Object>> up(List<AoaMailnumber> account) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < account.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            AoaStatusList status = aoaStatusListMapper.findOne(account.get(i).getStatus());
            result.put("accountid", account.get(i).getMailNumberId());
            result.put("typename", aoaTypeListMapper.findname(account.get(i).getMailType()));
            result.put("statusname", status.getStatusName());
            result.put("statuscolor", status.getStatusColor());
            result.put("accountname", account.get(i).getMailUserName());
            result.put("creattime", account.get(i).getMailCreateTime());
            list.add(result);
        }
        return list;
    }

    @Override
    public AoaMailnumber findOne(Long id) {
        return aoaMailnumberMapper.findOne(id);
    }

    @Override
    public void saveAcount(AoaMailnumber mail) {
        if (StringUtils.isEmpty(mail.getMailNumberId())) {
            aoaMailnumberMapper.insertSelective(mail);
        } else {
            aoaMailnumberMapper.updateByPrimaryKeySelective(mail);
        }
    }

    @Override
    public List<AoaInMailList> findByPushAndDelAndMailUserid(boolean push, boolean del, Long userId) {
        return aoaInMailListMapper.findByPushAndDelAndMailUserid(push, del, userId);
    }

    @Override
    public List<AoaMailReciver> findByDelAndReciverId(boolean del, Long userId) {
        return aoaMailReciverMapper.findByDelAndReciverId(del, userId);
    }

    @Override
    public List<AoaMailReciver> recive(Long userId, String val, String title, String type) {
        Long typeid = null;
        if (!StringUtils.isEmpty(type)) {
            typeid = aoaTypeListMapper.findByTypeModelAndTypeName("aoa_in_mail_list", type).getTypeId();
        }
        if (StringUtils.isEmpty(val)) {
            switch (title) {
                case "收件箱":
                    return aoaMailReciverMapper.findmailByObj(userId, false, null, typeid);
                case "垃圾箱":
                    return aoaMailReciverMapper.findmailByObj(userId, true, null, typeid);
                default:
                    return aoaMailReciverMapper.findmailByObj(userId, false, null, typeid);
            }
        } else {
            switch (title) {
                case "收件箱":
                    return aoaMailReciverMapper.findmailByObj(userId, false, "%" + val + "%", typeid);
                case "垃圾箱":
                    return aoaMailReciverMapper.findmailByObj(userId, true, "%" + val + "%", typeid);
                default:
                    return aoaMailReciverMapper.findmailByObj(userId, false, "%" + val + "%", typeid);
            }
        }
    }

    @Override
    public List<Map<String, Object>> mail(List<AoaMailReciver> maillist) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < maillist.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            String typename = aoaTypeListMapper.findname(maillist.get(i).getAoaInMailList().getMailType());
            AoaStatusList status = aoaStatusListMapper.findOne(maillist.get(i).getAoaInMailList().getMailStatusId());
            result.put("typename", typename);
            result.put("statusname", status.getStatusName());
            result.put("statuscolor", status.getStatusColor());
            result.put("star", maillist.get(i).getIsStar());
            result.put("read", maillist.get(i).getIsRead());
            result.put("time", maillist.get(i).getAoaInMailList().getMailCreateTime());
            result.put("reciver", maillist.get(i).getAoaInMailList().getInReceiver());
            result.put("title", maillist.get(i).getAoaInMailList().getMailTitle());
            result.put("mailid", maillist.get(i).getMailId());
            result.put("fileid", maillist.get(i).getAoaInMailList().getMailFileId());
            list.add(result);

        }
        return list;
    }

    @Override
    public List<AoaInMailList> inmail(Long userId, String key, String title, String type) {

        Long typeid = null;
        if (!StringUtils.isEmpty(type)) {
            typeid = aoaTypeListMapper.findByTypeModelAndTypeName("aoa_in_mail_list", type).getTypeId();
        }
        if (StringUtils.isEmpty(key)) {
            switch (title) {
                case "发件箱":

                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, null, typeid, true, false);

                case "草稿箱":
                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, null, typeid, false, false);
                default:
                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, null, typeid, true, false);
            }
        }else {
            switch (title) {
                case "发件箱":
                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, "%"+key+"%", typeid, true, false);

                case "草稿箱":
                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, "%"+key+"%", typeid, false, false);
                default:
                    return aoaInMailListMapper.findByObjOrderByMailCreateTimeDesc(userId, "%"+key+"%", typeid, true, false);
            }
        }
    }

    @Override
    public List<Map<String, Object>> maillist(List<AoaInMailList> maillist) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < maillist.size(); i++) {
            Map<String,Object> result=new HashMap<>();
            String typename=aoaTypeListMapper.findname(maillist.get(i).getMailType());
            AoaStatusList status=aoaStatusListMapper.findOne(maillist.get(i).getMailStatusId());
            result.put("typename", typename);
            result.put("statusname", status.getStatusName());
            result.put("statuscolor", status.getStatusColor());
            result.put("star", maillist.get(i).getMailStar());
            result.put("read", true);
            result.put("time", maillist.get(i).getMailCreateTime());
            result.put("reciver", maillist.get(i).getInReceiver());
            result.put("title", maillist.get(i).getMailTitle());
            result.put("mailid", maillist.get(i).getMailId());
            result.put("fileid", maillist.get(i).getMailFileId());
            list.add(result);

        }
        return list;
    }

    @Override
    public AoaInMailList findOneInmail(Long id) {
        return aoaInMailListMapper.findOne(id);
    }

    @Override
    public AoaMailReciver findbyReciverIdAndmailId(Long userId, Long id) {
        return aoaMailReciverMapper.findbyReciverIdAndmailId(userId,id);
    }

    @Override
    public void save(AoaMailReciver mailr) {
        if (StringUtils.isEmpty(mailr.getPkId())){
            aoaMailReciverMapper.insertSelective(mailr);
        }else {
            aoaMailReciverMapper.updateByPrimaryKeySelective(mailr);
        }
    }

    @Override
    public List<AoaMailnumber> findByStatusAndMailUserId(long l, Long userId) {
        return aoaMailnumberMapper.findByStatusAndMailUserId(l,userId);
    }

    @Override
    public AoaInMailList saveMail(AoaInMailList mail) {
         aoaInMailListMapper.insertSelective(mail);
         return mail;
    }

    @Override
    public boolean isContainChinese(String inReceiver) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(inReceiver);
        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void pushmail(String account, String password, String reciver, String name, String title, String content, String affix, String filename) {
        String file=null;
        if(!StringUtil.isEmpty(affix)){
            File root = new File(rootpath,affix);
            file=root.getAbsolutePath();
        }
        // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
        String myEmailAccount = account;
        String myEmailPassword = password;

        // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
        //qq  smtp.qq.com
        String myEmailSMTPHost = "smtp.163.com";

        // 收件人邮箱（替换为自己知道的有效邮箱）
        //  String receiveMailAccount = "1533047354@qq.com";

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // 开启 SSL 安全连接。
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);


        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message;
        try {
            message = createMimeMessage(session, myEmailAccount, reciver,name ,title, content,file,filename);

            // 4. 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错

            transport.connect(myEmailAccount, myEmailPassword);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 7. 关闭连接
            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void updateMail(AoaInMailList inmail) {
        aoaInMailListMapper.updateByPrimaryKeySelective(inmail);
    }

    @Override
    public AoaInMailList findByMailUseridAndMailId(Long userId, long parseLong) {
        return aoaInMailListMapper.findByMailUseridAndMailId(userId,parseLong);
    }

    @Override
    public void deleteImail(AoaInMailList inmail) {
        aoaInMailListMapper.delete(inmail.getMailId());
    }

    @Override
    public List<Integer> findbyMailId(Long mailid) {
        return aoaMailReciverMapper.findbyMailId(mailid);
    }

    @Override
    public void deleteReci(AoaMailReciver mailr) {
        aoaMailReciverMapper.delete(mailr.getPkId());
    }

    @Override
    public List<AoaMailReciver> findByMailId(Long mailid) {
        return aoaMailReciverMapper.findByMailId(mailid);
    }

    @Override
    public void dele(Long accountid) {
        aoaMailnumberMapper.dele(accountid);
    }

    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
                                                String name,String title,String content,String affix,String filename) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, name, "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));

        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");

        if(!StringUtil.isEmpty(affix)){

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(affix);//附件路径
            // 添加附件的内容
            messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            messageBodyPart.setFileName("=?GBK?B?"+ enc.encode(filename.getBytes()) + "?=");
            multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart,"text/html;charset=UTF-8");
        }else{
            // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
            message.setContent(content, "text/html;charset=UTF-8");
        }
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();



        return message;
    }
}
