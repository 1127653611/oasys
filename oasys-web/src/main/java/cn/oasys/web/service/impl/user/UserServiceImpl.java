package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Override
    public AoaUser findOneUser(String username, String password) {
        return aoaUserMapper.findOneUserByname(username,password);
    }

    @Override
    public AoaUser findOne(Long id) {
        return aoaUserMapper.findOneById(id);
    }
}
