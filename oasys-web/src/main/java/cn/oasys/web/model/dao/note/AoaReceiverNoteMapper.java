package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaReceiverNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AoaReceiverNoteMapper {

    void insert(@Param("nid") Long noteId, @Param("us") Set<Long> userss);

    List<AoaReceiverNote> findByNoteId(Long noteId);

    void delete(Long noteId);

    AoaReceiverNote finduserid(@Param("id") long noteid, @Param("uid") long realuserId);

    void deleteOne(Integer id);
}