package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaLoveUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaLoveUserMapper {
    List<AoaLoveUser> findUsersByReptyId(Long id);

    @Delete("delete from aoa_love_user where reply_id=#{rid} and user_id=#{uid}")
    void deleteUser(@Param("uid") Long userId, @Param("rid") Long replyId);

    @Insert("insert into aoa_love_user (reply_id,user_id) values (#{rid},#{uid})")
    void insert(@Param("uid") Long userId, @Param("rid") Long replyId);

    @Delete("delete from aoa_love_user where reply_id=#{rid}")
    void deleteLoveByreply(Long discussId);

    void deleteByReply(@Param("lon") Long[] replyLong);
}