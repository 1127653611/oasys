package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.dao.user.AoaDeptMapper;
import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import cn.oasys.web.service.inter.user.UserService;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaPositionMapper aoaPositionMapper;
    @Autowired
    private AoaRoleMapper aoaRoleMapper;
    @Autowired
    private AoaDeptMapper aoaDeptMapper;
    @Override
    public AoaUser findOneUser(String username, String password) {
        return aoaUserMapper.findOneUserByname(username,password);
    }

    @Override
    public AoaUser findOne(Long id) {
        return aoaUserMapper.findOneById(id);
    }

    @Override
    public List<AoaUserLog> findLogByUser(long userid) {
        return aoaUserMapper.findLogByUser(userid);
    }

    @Override
    public List<AoaUser> findByDept(long dept) {
        return aoaUserMapper.findByDept(dept);
    }

    @Override
    public void save(Long positionid, Long changedeptid, Long userid) {
        aoaUserMapper.update(positionid,changedeptid,userid);
    }

    @Override
    public List<AoaUser> findByidLock(int islock) {
        return aoaUserMapper.findByidLock(islock);
    }

    @Override
    public void saveUser(AoaUser user, boolean isbackpassword) throws PinyinException {
        AoaDept dept = aoaDeptMapper.findOne(user.getDeptId());
        AoaPosition position = aoaPositionMapper.findOne(user.getPositionId());
        AoaRole role = aoaRoleMapper.findOne(user.getRoleId());
        if (role.getRoleValue()==1){
            user.setSuperman(1);
        }else {
            user.setSuperman(0);
        }
        if(user.getUserId()==null){
            String pinyin= PinyinHelper.convertToPinyinString(user.getUserName(), "", PinyinFormat.WITHOUT_TONE);
            user.setPinyin(pinyin);
            user.setFatherId(dept.getDeptmanager());
            user.setPassword("123456");
            aoaUserMapper.sava(user);
        }else {
            user.setFatherId(dept.getDeptmanager());
            if(isbackpassword){
                user.setPassword("123456");
            }
            aoaUserMapper.updateSelective(user);
        }

    }

    @Override
    public List<AoaUser> findByPosition(Long positionId) {
        return aoaUserMapper.findByPosition(positionId);
    }

    @Override
    public List<AoaUser> findByidLockLike(int islock,String key) {
        return aoaUserMapper.findByidLockLike(islock,key);
    }

    @Override
    public List<AoaUser> findByRole(long rolrid) {
        return aoaUserMapper.findByRole(rolrid);
    }


}
