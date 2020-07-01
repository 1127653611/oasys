package cn.oasys.web.service.inter.notice;

import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    Integer findByReadAndUserId(Boolean read, Long id);

    List<AoaNoticeList> findByUserId(Long userId);

    public List<Map<String, Object>> fengZhuang(List<AoaNoticeList> noticelist);
}
