package cn.oasys.web.model.dao.mail;


import cn.oasys.web.model.pojo.mail.AoaMailnumber;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaMailnumberMapper {

    List<AoaMailnumber> findByMailUserId(Long userId);

    List<AoaMailnumber> findByMailUserIdOrderByMailType(Long userId);

    List<AoaMailnumber> findByMailUserIdOrderByStatus(Long userId);

    List<AoaMailnumber> findByMailUserIdOrderByMailCreateTimeDesc(Long userId);

    List<AoaMailnumber> findByMailUserNameLikeAndMailUserId(@Param("key") String val, @Param("uid") Long userId);

    AoaMailnumber findOne(Long id);

    void insertSelective(AoaMailnumber mail);

    void updateByPrimaryKeySelective(AoaMailnumber mail);

    List<AoaMailnumber> findByStatusAndMailUserId(@Param("status") long l, @Param("uid") Long userId);

    @Delete("delete from aoa_mailnumber where mail_number_id = #{id}")
    void dele(Long accountid);
}