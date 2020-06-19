package cn.oasys.web.model.dao.mail;

import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaMailReciverMapper {
    @Select("select COUNT(pk_id) from aoa_mail_reciver where is_read=#{read} and is_del=#{del} and mail_reciver_id=#{id}")
    int findByReadAndDelAndReciverId(@Param("read") boolean isread, @Param("del") boolean isdel, @Param("id") long id);
}