package cn.oasys.web.model.dao.notice;

import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaNoticeListMapper {
    @Select("select notice_id , content , modify_time ,notice_time ,status_id , title ,is_top , type_id , url , user_id  from aoa_notice_list" +
            " where user_id=#{id} order by is_top desc, modify_time desc ")
    List<AoaNoticeList> findByUserId(@Param("id") Long userId);

}
