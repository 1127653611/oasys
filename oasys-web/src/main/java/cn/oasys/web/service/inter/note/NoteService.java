package cn.oasys.web.service.inter.note;

import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.note.AoaReceiverNote;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.List;
import java.util.Set;

public interface NoteService {
    List<AoaCatalog> findcatauser(Long userid);

    List<AoaNoteList> sortpage( String baseKey, long userid, Long isCollected, Long catalogId, Long typeId, String type, String status, String time);

    AoaCatalog findOneCatelog(long ca);

    AoaNoteList findOne(Long nid);

    List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc1(Long userId);

    void save(AoaNoteList note2, Set<Long> userss);

    void updatecollect(Long collect, Long noteid);

    AoaReceiverNote finduserid(long noteid, long realuserId);

    void delete(long noteid);

    void deleteRe(AoaReceiverNote u,AoaNoteList aoaNoteList);

    List<String> findcataname(Long userid);

    void savecata(AoaCatalog aoaCatalog);

    List<AoaNoteList> findByCatalogId(long catalogid);

    void deletecata(long catalogid);

    void saveNotePaper(AoaNotepaper npaper);
}
