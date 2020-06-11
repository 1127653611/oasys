package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.user.AoaUser;

public interface UserService {
    public AoaUser findOneUser(String username,String password);
    public AoaUser findOne(Long id);
}
