package cn.oasys.web.model.dao.mail;

import cn.oasys.web.model.pojo.mail.AoaInMailList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaInMailListMapper {
    AoaInMailList findOne(long id);

    List<AoaInMailList> findByPushAndDelAndMailUserid(@Param("push") boolean push, @Param("del") boolean del, @Param("uid") Long userId);

    List<AoaInMailList> findByObjOrderByMailCreateTimeDesc(@Param("uid") Long userId, @Param("key") String key, @Param("type") Long typeid, @Param("push") boolean push, @Param("del") boolean del);

    void insertSelective(AoaInMailList mail);

    void updateByPrimaryKeySelective(AoaInMailList inmail);

    AoaInMailList findByMailUseridAndMailId(@Param("uid") Long userId, @Param("mid") long parseLong);

    void delete(Long mailId);
}