package cn.oasys.web;

import cn.oasys.web.common.Utils.MD5Util;
import cn.oasys.web.model.dao.discuss.AoaDiscussListMapper;
import cn.oasys.web.model.dao.mail.AoaMailReciverMapper;
import cn.oasys.web.model.dao.note.AoaDirectorUsersMapper;
import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.user.UserService;
import com.mysql.cj.xdevapi.Collection;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@SpringBootTest
public class OasysApplicationTest {
    @Value("${user.avatar.rootpath}")
    private String userAvatarRootpath;
    @Autowired
    private AoaDirectorUsersMapper aoaDirectorUsersMapper;
    @Autowired
    private AoaDiscussListMapper aoaDiscussListMapper;
    @Autowired
    private AoaNoticeUserRelationMapper aoaNoticeUserRelationMapper;
    @Autowired
    private AoaSysMenuMapper aoaSysMenuMapper;
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaMailReciverMapper aoaMailReciverMapper;
    @Autowired
    private MailService mailService;

    @Test
    public void oasysytest() throws InterruptedException, SQLException, IOException, XMLParserException, InvalidConfigurationException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("MG.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
    @Test
    public void tyes() throws ClassNotFoundException, SQLException {
        System.out.println(MD5Util.getMD5String("123456"));
        System.out.println(MD5Util.getMD5String("123456"));
        System.out.println(MD5Util.getMD5String("123456"));

    }
}
