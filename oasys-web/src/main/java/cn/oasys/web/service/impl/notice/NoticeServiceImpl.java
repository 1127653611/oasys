package cn.oasys.web.service.impl.notice;

import cn.oasys.web.model.dao.notice.AoaNoticeListMapper;
import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.service.inter.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaStatusListMapper aoaStatusListMapper;
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Autowired
    private AoaNoticeListMapper aoaNoticeListMapper;
    @Autowired
    private AoaNoticeUserRelationMapper aoaNoticeUserRelationMapper;
    @Override
    public Integer findByReadAndUserId(Boolean read, Long id) {
        return aoaNoticeUserRelationMapper.findByReadAndUserId(read,id);
    }

    @Override
    public List<AoaNoticeList> findByUserId(Long userId) {
        return aoaNoticeListMapper.findByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> fengZhuang(List<AoaNoticeList> noticelist) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < noticelist.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            result.put("noticeId", noticelist.get(i).getNoticeId());
            result.put("typename", aoaTypeListMapper.findname(noticelist.get(i).getTypeId()));
            result.put("statusname", aoaStatusListMapper.findname(noticelist.get(i).getStatusId()));
            result.put("statuscolor", aoaStatusListMapper.findcolor(noticelist.get(i).getStatusId()));
            result.put("title", noticelist.get(i).getTitle());
            result.put("noticeTime", noticelist.get(i).getNoticeTime());
            result.put("top", noticelist.get(i).getIsTop());
            result.put("url", noticelist.get(i).getUrl());
            result.put("username", aoaUserMapper.findOneById(noticelist.get(i).getUserId()).getUserName());
            result.put("deptname", aoaUserMapper.findOneById(noticelist.get(i).getUserId()).getDept().getDeptName());
            list.add(result);
        }
        return list;

    }
}
