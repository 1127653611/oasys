package cn.oasys.web;

import cn.oasys.web.common.Common;
import cn.oasys.web.common.Utils.MD5Util;
import cn.oasys.web.exception.NameErrorException;
import cn.oasys.web.model.dao.discuss.AoaDiscussListMapper;
import cn.oasys.web.model.dao.mail.AoaMailReciverMapper;
import cn.oasys.web.model.dao.note.AoaDirectorUsersMapper;
import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.dao.role.AoaRolePowerListMapper;
import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import cn.oasys.web.model.pojo.system.AoaSysMenu;
import cn.oasys.web.service.inter.mail.MailService;
import cn.oasys.web.service.inter.process.ProcessService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
    @Autowired
    private AoaRolePowerListMapper aoaRolePowerListMapper;
    @Autowired
    private AoaRoleMapper aoaRoleMapper;
    @Autowired
    private ProcessService processService;

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
    public void tyes() throws ClassNotFoundException, SQLException, ParseException, NameErrorException {
       throw new NameErrorException("dsadas");
    }
}
