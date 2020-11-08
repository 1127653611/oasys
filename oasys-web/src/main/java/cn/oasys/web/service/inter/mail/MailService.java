package cn.oasys.web.service.inter.mail;

import cn.oasys.web.model.pojo.mail.AoaInMailList;
import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.model.pojo.mail.AoaMailnumber;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface MailService {
    List<AoaMailReciver> findByReadAndDelAndReciverId(boolean isread,boolean isdel,long userid);

    List<AoaMailnumber> index(Long userId,String val, Model model);

    List<Map<String, Object>> up(List<AoaMailnumber> pagelist);

    AoaMailnumber findOne(Long id);

    void saveAcount(AoaMailnumber mail);

    List<AoaInMailList> findByPushAndDelAndMailUserid(boolean push, boolean del, Long userId);

    List<AoaMailReciver> findByDelAndReciverId(boolean del, Long userId);

    List<AoaMailReciver> recive(Long userId, String val, String title,String type);

    List<Map<String, Object>> mail(List<AoaMailReciver> pagelist);

    List<AoaInMailList> inmail(Long userId, String key, String title,String type);

    List<Map<String, Object>> maillist(List<AoaInMailList> pagemail);

    AoaInMailList findOneInmail(Long id);

    AoaMailReciver findbyReciverIdAndmailId(Long userId, Long id);

    void save(AoaMailReciver mailr);

    List<AoaMailnumber> findByStatusAndMailUserId(long l, Long userId);

    AoaInMailList saveMail(AoaInMailList mail);

    boolean isContainChinese(String inReceiver);

    void pushmail(String account,String password,String reciver,
                         String name,String title,String content,String affix,String filename);

    void updateMail(AoaInMailList inmail);

    AoaInMailList findByMailUseridAndMailId(Long userId, long parseLong);

    void deleteImail(AoaInMailList inmail);

    List<Integer> findbyMailId(Long mailid);

    void deleteReci(AoaMailReciver mailr);

    List<AoaMailReciver> findByMailId(Long mailid);

    void dele(Long accountid);
}
