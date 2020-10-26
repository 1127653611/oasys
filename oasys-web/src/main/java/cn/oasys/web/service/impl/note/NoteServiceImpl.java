package cn.oasys.web.service.impl.note;

import cn.oasys.web.model.dao.note.AoaCatalogMapper;
import cn.oasys.web.model.dao.note.AoaNoteListMapper;
import cn.oasys.web.model.dao.note.AoaNotepaperMapper;
import cn.oasys.web.model.dao.note.AoaReceiverNoteMapper;
import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.note.AoaReceiverNote;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.note.NoteService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    @Autowired
    private AoaReceiverNoteMapper aoare;
    @Autowired
    private AoaNotepaperMapper aoaNotepaperMapper;
    @Autowired
    private AoaNoteListMapper aoaNoteListMapper;
    @Autowired
    private AoaCatalogMapper aoaCatalogMapper;

    @Override
    public List<AoaCatalog> findcatauser(Long userid) {
        return aoaCatalogMapper.findcatauser(userid);
    }

    @Override
    public List<AoaNoteList> sortpage(String baseKey, long userid, Long isCollected, Long catalogId, Long typeId, String type, String status, String time) {

        if (StringUtils.isEmpty(catalogId)) {
            if (StringUtils.isEmpty(baseKey)) {
                return aoaNoteListMapper.findOrderByOnce(null, userid, type, status, time, typeId, isCollected);
            } else {
                return aoaNoteListMapper.findOrderByOnce("%" + baseKey + "%", userid, type, status, time, typeId, isCollected);
            }
        } else {
            if (StringUtils.isEmpty(baseKey)) {
                return aoaNoteListMapper.findByCatalogIdAndtitleOrderByOnce(null, catalogId, userid, type, status, time, typeId, isCollected);
            } else {
                return aoaNoteListMapper.findByCatalogIdAndtitleOrderByOnce("%" + baseKey + "%", catalogId, userid, type, status, time, typeId, isCollected);
            }
        }
    }

    @Override
    public AoaCatalog findOneCatelog(long ca) {
        return aoaCatalogMapper.findOne(ca);
    }

    @Override
    public AoaNoteList findOne(Long nid) {
        return aoaNoteListMapper.finfOne(nid);
    }

    @Override
    public List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc(Long userId) {
        return aoaNotepaperMapper.findByUserIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc1(Long userId) {
        return aoaNotepaperMapper.findByUserIdOrderByCreateTimeDesc1(userId);
    }

    @Override
    public void save(AoaNoteList note2, Set<Long> userss) {
        if (StringUtils.isEmpty(note2.getNoteId())) {
            aoaNoteListMapper.insertSelective(note2);
            aoare.insert(note2.getNoteId(), userss);
        } else {
            aoaNoteListMapper.updateByPrimaryKeySelective(note2);
            List<AoaReceiverNote> aoaReceiverNotes = aoare.findByNoteId(note2.getNoteId());
            for (AoaReceiverNote aoaReceiverNote : aoaReceiverNotes
            ) {
                if (userss.contains(aoaReceiverNote.getUserId())) {
                    userss.remove(aoaReceiverNote.getUserId());
                } else {
                    aoare.delete(aoaReceiverNote.getNoteId());
                }

            }
            if (!userss.isEmpty()) {
                aoare.insert(note2.getNoteId(), userss);
            }
        }

    }

    @Override
    public void updatecollect(Long collect, Long noteid) {
        aoaNoteListMapper.updatecollect(collect,noteid);
    }

    @Override
    public AoaReceiverNote finduserid(long noteid, long realuserId) {
        return aoare.finduserid(noteid,realuserId);
    }

    @Override
    public void delete(long noteid) {
        aoare.delete(noteid);
        aoaNoteListMapper.delete(noteid);
    }

    @Override
    public void deleteRe(AoaReceiverNote u,AoaNoteList aoaNoteList) {
        List<AoaReceiverNote> aoaReceiverNotes=aoare.findByNoteId(aoaNoteList.getNoteId());
        if (aoaReceiverNotes.size()>1){
            aoare.deleteOne(u.getId());
        }else {
            delete(aoaNoteList.getNoteId());
        }
    }

    @Override
    public List<String> findcataname(Long userid) {
        return aoaCatalogMapper.findcataname(userid);
    }

    @Override
    public void savecata(AoaCatalog aoaCatalog) {
        aoaCatalogMapper.insertSelective(aoaCatalog);
    }

    @Override
    public List<AoaNoteList> findByCatalogId(long catalogid) {
        return aoaNoteListMapper.findByCatalogId(catalogid);
    }

    @Override
    public void deletecata(long catalogid) {
        aoaCatalogMapper.delete(catalogid);
    }
}
