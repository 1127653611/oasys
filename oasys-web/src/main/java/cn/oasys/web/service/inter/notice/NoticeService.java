package cn.oasys.web.service.inter.notice;

import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NoticeService {
    List<AoaNoticeUserRelation> findByReadAndUserId(Boolean read, Long id);
}
