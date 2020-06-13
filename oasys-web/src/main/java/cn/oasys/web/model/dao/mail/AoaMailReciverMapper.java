package cn.oasys.web.model.dao.mail;

import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaMailReciverMapper {
    List<AoaMailReciver> findByReadAndDelAndReciverId(@Param("read") boolean isread,
                                                      @Param("del") boolean isdel,
                                                      @Param("id") long id);
}