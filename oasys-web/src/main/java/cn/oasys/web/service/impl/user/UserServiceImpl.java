package cn.oasys.web.service.impl.user;

import cn.oasys.web.common.Utils.MD5Util;
import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.dao.user.AoaDeptMapper;
import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.dao.user.AoaUserLogMapper;
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
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AoaUserLogMapper aoaUserLogMapper;
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
        return aoaUserMapper.findOneUserByname(username, password);
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
        aoaUserMapper.update(positionid, changedeptid, userid);
    }

    @Override
    public List<AoaUser> findByidLock(int islock) {
        return aoaUserMapper.findByidLock(islock);
    }

    @Override
    public void saveUser(AoaUser user, boolean isbackpassword) throws PinyinException {
        AoaDept dept = aoaDeptMapper.findOne(user.getDeptId());
        AoaRole role = aoaRoleMapper.findOne(user.getRoleId());
        if (role.getRoleValue() == 1) {
            user.setSuperman(1);
        } else {
            user.setSuperman(0);
        }
        if (user.getUserId() == null) {
            String pinyin = PinyinHelper.convertToPinyinString(user.getUserName(), "", PinyinFormat.WITHOUT_TONE);
            user.setPinyin(pinyin);
            user.setFatherId(dept.getDeptmanager());
            user.setPassword(MD5Util.getMD5String("123456"));
            aoaUserMapper.sava(user);
        } else {
            user.setFatherId(dept.getDeptmanager());
            if (isbackpassword) {
                user.setPassword(MD5Util.getMD5String("123456"));
            }
            aoaUserMapper.updateSelective(user);
        }

    }

    @Override
    public List<AoaUser> findByPosition(Long positionId) {
        return aoaUserMapper.findByPosition(positionId);
    }

    @Override
    public List<AoaUser> findByidLockLike(int islock, String key) {
        return aoaUserMapper.findByidLockLike(islock, key);
    }

    @Override
    public List<AoaUser> findByRole(long rolrid) {
        return aoaUserMapper.findByRole(rolrid);
    }

    @Override
    public List<AoaUser> findAll() {
        return aoaUserMapper.findAll();
    }

    @Override
    public List<AoaUser> findAllByLike(String key) {
        return aoaUserMapper.findAllByLike(key);
    }

    @Override
    public List<AoaUser> findAllBySort() {
        return aoaUserMapper.findAllBySort();
    }

    @Override
    public List<AoaUser> findByPinyinLike(String s) {
        return aoaUserMapper.findByPinyinLike(s);
    }

    @Override
    public List<AoaUser> findUsersByBaseKey(String s) {
        return aoaUserMapper.findAllByLike(s);
    }

    @Override
    public List<AoaUser> findUsersByBaseKeyAndPinyinLike(String key, String p) {
        return aoaUserMapper.findUsersByBaseKeyAndPinyinLike(key, p);
    }

    @Override
    public List<AoaUser> findmyemployuser(String baseKey, Long userid) {
        if (!StringUtils.isEmpty(baseKey)) {
            // 模糊查询
            return aoaUserMapper.findbyFatherId("%" + baseKey + "%", userid);
        } else {
            return aoaUserMapper.findbyFatherId(null, userid);
        }
    }

    @Override
    public AoaUser findByname(String name) {
        return aoaUserMapper.findByname(name);
    }

    @Override
    public List<AoaUser> findByFatherId(Long userId) {
        return aoaUserMapper.findbyFatherId(null, userId);
    }

    @Override
    public void update(AoaUser users) {
        aoaUserMapper.updateSelective(users);
    }

    @Override
    public List<AoaUserLog> ulogpaging(String basekey, long userid, String time) {
        if (StringUtils.isEmpty(basekey)) {
            return aoaUserLogMapper.findByUser(null, userid, time);
        } else {
            return aoaUserLogMapper.findByUser("%" + basekey + "%", userid, time);
        }

    }

    @Override
    public void savelog(AoaUserLog uLog) {
        aoaUserLogMapper.insertSelective(uLog);
    }

    @Override
    public AoaUserLog findByUserlaset(long l) {
        return aoaUserLogMapper.findLast(l);
    }

}
