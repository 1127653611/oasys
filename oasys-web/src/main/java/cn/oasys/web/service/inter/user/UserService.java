package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import com.github.stuxuhai.jpinyin.PinyinException;

import java.util.List;

public interface UserService {
    AoaUser findOneUser(String username, String password);

    AoaUser findOne(Long id);

    List<AoaUserLog> findLogByUser(long userid);

    List<AoaUser> findByDept(long dept);


    void save(Long positionid, Long changedeptid, Long userid);

    List<AoaUser> findByidLock(int islock);

    void saveUser(AoaUser user, boolean isbackpassword) throws PinyinException;

    List<AoaUser> findByPosition(Long positionId);

    List<AoaUser> findByidLockLike(int islock, String key);

    List<AoaUser> findByRole(long rolrid);

    List<AoaUser> findAll();

    List<AoaUser> findAllByLike(String key);

    List<AoaUser> findAllBySort();

    List<AoaUser> findByPinyinLike(String s);

    List<AoaUser> findUsersByBaseKey(String s);

    List<AoaUser> findUsersByBaseKeyAndPinyinLike(String s, String s1);

    List<AoaUser> findmyemployuser(String baseKey, Long userid);

    AoaUser findByname(String name);

    List<AoaUser> findByFatherId(Long userId);

    void update(AoaUser users);
}
