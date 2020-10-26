package cn.oasys.web.service.inter.notice;

import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    Integer findByReadAndUserId(Boolean read, Long id);

    List<AoaNoticeList> findByUserId(Long userId);

    public List<Map<String, Object>> fengZhuang(List<AoaNoticeList> noticelist);

    List<Map<String, Object>> findMyNoticeLimit(Long userId);

    List<AoaNoticeList> pageThis(Long userId, String baseKey, String type, String status, String time);

    AoaNoticeList findOne(Long noticeId);

    AoaNoticeUserRelation findOneRe(Long relationId);

    void saveRe(AoaNoticeUserRelation relation);

    void save(AoaNoticeList menu,Long userId);

    void deleteOne(Long noticeId);

    List<Map<String, Object>> findMyNotice(Long userId);

    List<Map<String, Object>> setList(List<Map<String, Object>> list);

    void saves(List<AoaNoticeUserRelation> nurs);

    AoaNoticeUserRelation findByUserIdAndNoticeId(Long userId, Long noticeId);

    void deleteRe(AoaNoticeUserRelation relation);

    List<Map<String, Object>> sortMyNotice(Long userId, String baseKey, String type, String status, String time);
}
