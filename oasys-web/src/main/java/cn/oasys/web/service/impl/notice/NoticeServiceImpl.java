package cn.oasys.web.service.impl.notice;

import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.service.inter.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private AoaNoticeUserRelationMapper aoaNoticeUserRelationMapper;
    @Override
    public Integer findByReadAndUserId(Boolean read, Long id) {
        return aoaNoticeUserRelationMapper.findByReadAndUserId(read,id);
    }
}
