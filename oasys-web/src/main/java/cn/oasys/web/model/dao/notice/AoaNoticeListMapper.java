package cn.oasys.web.model.dao.notice;

import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AoaNoticeListMapper {
    @Select("select notice_id , content , modify_time ,notice_time ,status_id , title ,is_top , type_id , url , user_id  from aoa_notice_list" +
            " where user_id=#{id} order by is_top desc, modify_time desc ")
    List<AoaNoticeList> findByUserId(@Param("id") Long userId);

    List<Map<String, Object>> findMyNoticeLimit(Long userId);

    List<AoaNoticeList> findByUserIdOrDerByObj(@Param("uid") Long userId, @Param("key") String baseKey, @Param("type") String type, @Param("status") String status, @Param("time") String time);

    AoaNoticeList findOne(Long noticeId);

    void insertSelective(AoaNoticeList menu);

    void updateByPrimaryKeySelective(AoaNoticeList menu);

    void delete(Long noticeId);

    List<Map<String, Object>> findMyNotice(Long userId);

    List<Map<String, Object>> sortMyNotice(@Param("uid") Long userId, @Param("key") String baseKey, @Param("type") String type, @Param("status") String status, @Param("time") String time);
}
