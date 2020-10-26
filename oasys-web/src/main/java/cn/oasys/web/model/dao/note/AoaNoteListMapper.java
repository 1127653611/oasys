package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaNoteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaNoteListMapper {

    List<AoaNoteList> findOrderByOnce(@Param("key")String key,@Param("id") long userid, @Param("type") String type, @Param("status") String status, @Param("time") String time,@Param("tid")Long typeId,@Param("co")Long iscolect);

    List<AoaNoteList> findByCatalogIdAndtitleOrderByOnce(@Param("key") String s,@Param("cid") Long catalogId, @Param("id") long userid, @Param("type") String type, @Param("status") String status, @Param("time") String time,@Param("tid")Long typeId,@Param("co")Long iscolect);

    AoaNoteList finfOne(Long nid);

    void insertSelective(AoaNoteList note2);

    void updateByPrimaryKeySelective(AoaNoteList note2);

    void updatecollect(@Param("co") Long collect, @Param("nid") Long noteid);

    void delete(long noteid);

    List<AoaNoteList> findByCatalogId(long catalogid);
}