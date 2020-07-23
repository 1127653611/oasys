package cn.oasys.web.service.inter.note;

import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;

import java.util.List;

public interface NoteService {
    List<AoaCatalog> findcatauser(Long userid);

    List<AoaNoteList> sortpage(int page, String baseKey, long userid, Long isCollected, Long catalogId, Long typeId, Object type, Object status, Object time);

    AoaCatalog findOneCatelog(long ca);

    AoaNoteList findOne(Long nid);
}
