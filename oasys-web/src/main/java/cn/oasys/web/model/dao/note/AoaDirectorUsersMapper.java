package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaDirectorUsers;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface AoaDirectorUsersMapper {
    Set<String> findCatalogNameByUser(Long userId);

    List<AoaDirectorUsers> findByUserAndShareuserNotNullAndHandle(@Param("id") Long userId, @Param("han") int i);

    List<Map<String, Object>> allDirector(@Param("userId") Long userId,@Param("pinyin") String pinyin,@Param("outtype") String outtype,@Param("baseKey") String baseKey);

    AoaDirectorUsers findOne(Long directorId);

    void updateByPrimaryKeySelective(AoaDirectorUsers directorUser);

    void insertSelective(AoaDirectorUsers directorUser);

    List<AoaDirectorUsers> findByCatalogNameAndUser(@Param("oldname") String oldtypename, @Param("uid") Long userId);

    void savalist(@Param("dus") List<AoaDirectorUsers> dus);

    void insertlist(@Param("dus") List<AoaDirectorUsers> dus);

    List<AoaDirectorUsers> findByDirector(Long directorId);

    @Delete("delete from aoa_director_users where director_users_id=#{id}")
    void deleteDerectorUser(Long directorUsersId);

    AoaDirectorUsers findByDirectorAndUser(@Param("did") Long directorId, @Param("uid") Long userId);

    List<AoaDirectorUsers> findByShareuser(Long userId);

    List<AoaDirectorUsers> findBaseKey(@Param("key") String s, @Param("id") Long userId);

    List<AoaDirectorUsers> findByUserAndShareuserNotNull(@Param("id") Long userId);

    List<AoaDirectorUsers> findBaseKeyShare(@Param("key") String s, @Param("id") Long userId);

//
//    AoaDirectorUsers findUserByDirectorAndUser(@Param("did") Long directorId, @Param("uid") Long userId);
}