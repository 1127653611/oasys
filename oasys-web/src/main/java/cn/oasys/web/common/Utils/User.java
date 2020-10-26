package cn.oasys.web.common.Utils;

import cn.oasys.web.model.dao.discuss.AoaDiscussListMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ Description   :  TODO
 * @ Author        :  wang
 * @ CreateDate    :  2020/10/23 8:14
 */
@Configuration
public class User {
    @Autowired
    private AoaUserMapper aoaUserMapper;

    public void getAllUser(int page, int size, Model model) {
        PageHelper.startPage(page, size);
        List<AoaUser> userlist = aoaUserMapper.findAll();
        PageInfo<AoaUser> userPageInfo = new PageInfo<>(userlist);
        model.addAttribute("page", userPageInfo);
        model.addAttribute("emplist", userlist);
        model.addAttribute("url", "names");
    }
}
