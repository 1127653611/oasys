package cn.oasys.web.model.dao.user;

import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.model.pojo.user.AoaUserLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.security.Key;
import java.util.List;

@Mapper
public interface AoaUserMapper {
    public AoaUser findOneUserByname(@Param("username") String userName, @Param("password") String password);

    public AoaUser findOneById(@Param("id") Long id);

    @Select("SELECT * from aoa_user_log where aoa_user_log.user_id=#{id} ORDER BY aoa_user_log.log_time DESC LIMIT 0,13")
    public List<AoaUserLog> findLogByUser(@Param("id") long userid);

    List<AoaUser> findByDept(@Param("id") long dept);

    @Update("update aoa_user set dept_id=#{did}, position_id=#{pid}  where user_id=#{uid}")
    void update(@Param("pid") Long positionid, @Param("did") Long changedeptid, @Param("uid") Long userid);


    List<AoaUser> findByidLock(@Param("islock") int islock);

    void sava(AoaUser user);

    void updateSelective(AoaUser user);

    List<AoaUser> findByPosition(@Param("id") Long positionId);

    List<AoaUser> findByidLockLike(@Param("islock") int islock, @Param("key") String Key);

    List<AoaUser> findByRole(@Param("roleid") long rolrid);

    List<AoaUser> findAll();

    List<AoaUser> findAllByLike(@Param("key") String key);

    List<AoaUser> findAllBySort();

    List<AoaUser> findByPinyinLike(String s);

    List<AoaUser> findUsersByBaseKeyAndPinyinLike(@Param("key") String key, @Param("p") String p);

    List<AoaUser> findbyFatherId(@Param("key") String key, @Param("uid") Long userid);

    AoaUser findByname(String name);
}