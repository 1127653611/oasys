package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaLoveDiscussUser;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaLoveDiscussUserMapper {
    List<AoaLoveDiscussUser>findUsersByDiscussId(Long id);

    @Delete("delete from aoa_love_discuss_user where discuss_id=#{disid} and user_id=#{uid}")
    void deleteUser(@Param("uid") Long userId, @Param("disid") Long replyId);

    @Insert("insert into aoa_love_discuss_user (discuss_id,user_id) values (#{disid},#{uid})")
    void insert(@Param("uid") Long userId, @Param("disid") Long replyId);

    @Delete("delete from aoa_love_discuss_user where discuss_id=#{disid}")
    void deleteBydiscuss(Long discussId);
}