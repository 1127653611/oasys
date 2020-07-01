package cn.oasys.web;

import cn.oasys.web.model.dao.discuss.AoaDiscussListMapper;
import cn.oasys.web.model.dao.mail.AoaMailReciverMapper;
import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.dao.system.AoaSysMenuMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class OasysApplicationTest {
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
    public void userTset() {
        System.out.println(aoaDiscussListMapper.findByTitleLike("%f%"));
    }
}
