package cn.oasys.web.service.impl.note;

import cn.oasys.web.model.dao.note.AoaCatalogMapper;
import cn.oasys.web.model.dao.note.AoaNoteListMapper;
import cn.oasys.web.model.pojo.note.AoaCatalog;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.service.inter.note.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    @Autowired
    private AoaNoteListMapper aoaNoteListMapper;
    @Autowired
    private AoaCatalogMapper aoaCatalogMapper;

    @Override
    public List<AoaCatalog> findcatauser(Long userid) {
        return aoaCatalogMapper.findcatauser(userid);
    }

    @Override
    public List<AoaNoteList> sortpage(int page, String baseKey, long userid, Long isCollected, Long catalogId, Long typeId, Object type, Object status, Object time) {
        System.out.println("page:" + page);
        System.out.println("basekey:" + baseKey);
        System.out.println("userid:" + userid);
        System.out.println("isCollected:" + isCollected);
        System.out.println("catalogId:" + catalogId);
        System.out.println("typeId:" + typeId);
        System.out.println("type:" + type);
        System.out.println("status:" + status);
        System.out.println("time:" + time);
        if (StringUtils.isEmpty(catalogId)) {
            if (StringUtils.isEmpty(baseKey)) {
                return aoaNoteListMapper.findOrderByOnce(userid, type, status, time,typeId);
            } else {
                return aoaNoteListMapper.findBytitleOrderByOnce("%" + baseKey + "%", userid, type, status, time,typeId);
            }
        } else {
            if (StringUtils.isEmpty(baseKey)) {
                return aoaNoteListMapper.findByCatalogIdOrderByOnce(catalogId, userid, type, status, time,typeId);
            } else {
                return aoaNoteListMapper.findByCatalogIdAndtitleOrderByOnce("%" + baseKey + "%", catalogId, userid, type, status, time,typeId);
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
}
