package cn.oasys.web.model.dao.mail;

import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaMailReciverMapper {

    List<AoaMailReciver> findByReadAndDelAndReciverId(@Param("read") boolean isread, @Param("del") boolean isdel, @Param("id") long id);

    List<AoaMailReciver> findByDelAndReciverId(@Param("del") boolean del, @Param("uid") Long userId);

    List<AoaMailReciver> findmailByObj(@Param("uid") Long userId,  @Param("del") boolean isdel, @Param("key") String key, @Param("type") Long typeid);

    AoaMailReciver findbyReciverIdAndmailId(@Param("rid") Long userId, @Param("mid") Long id);

    void updateByPrimaryKeySelective(AoaMailReciver mailr);

    void insertSelective(AoaMailReciver mailr);

    @Select("select is_del from aoa_mail_reciver where mail_id = #{id}")
    List<Integer> findbyMailId(Long mailid);

    @Delete("delete from aoa_mail_reciver where pk_id =#{id}")
    void delete(Long pkId);

    List<AoaMailReciver> findByMailId(Long mailid);
}