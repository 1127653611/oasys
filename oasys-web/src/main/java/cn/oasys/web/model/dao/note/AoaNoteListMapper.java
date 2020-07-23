package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaNoteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaNoteListMapper {

    List<AoaNoteList> findOrderByOnce(@Param("id") long userid, @Param("type") Object type, @Param("status") Object status, @Param("time") Object time,@Param("tid")Long typeId);

    List<AoaNoteList> findBytitleOrderByOnce(@Param("key") String key, @Param("id") long userid, @Param("type") Object type, @Param("status") Object status, @Param("time") Object time,@Param("tid")Long typeId);

    List<AoaNoteList> findByCatalogIdOrderByOnce(@Param("cid") Long catalogId, @Param("id") long userid, @Param("type") Object type, @Param("status") Object status, @Param("time") Object time,@Param("tid")Long typeId);

    List<AoaNoteList> findByCatalogIdAndtitleOrderByOnce(@Param("key") String s,@Param("cid") Long catalogId, @Param("id") long userid, @Param("type") Object type, @Param("status") Object status, @Param("time") Object time,@Param("tid")Long typeId);

    AoaNoteList finfOne(Long nid);
}