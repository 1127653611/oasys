package cn.oasys.web.service.impl.notice;

import cn.oasys.web.model.dao.notice.AoaNoticeListMapper;
import cn.oasys.web.model.dao.notice.AoaNoticeUserRelationMapper;
import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.note.AoaNoteList;
import cn.oasys.web.model.pojo.note.AoaNotepaper;
import cn.oasys.web.model.pojo.notice.AoaNoticeList;
import cn.oasys.web.model.pojo.notice.AoaNoticeUserRelation;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
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
        return aoaNoticeUserRelationMapper.findByReadAndUserId(read, id);
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

    @Override
    public List<Map<String, Object>> findMyNoticeLimit(Long userId) {
        return aoaNoticeListMapper.findMyNoticeLimit(userId);
    }

    @Override
    public List<AoaNoticeList> pageThis(Long userId, String baseKey, String type, String status, String time) {
        if (StringUtils.isEmpty(baseKey)) {
            return aoaNoticeListMapper.findByUserIdOrDerByObj(userId, null, type, status, time);
        } else {
            return aoaNoticeListMapper.findByUserIdOrDerByObj(userId, "%" + baseKey + "%", type, status, time);
        }

    }

    @Override
    public AoaNoticeList findOne(Long noticeId) {
        return aoaNoticeListMapper.findOne(noticeId);
    }

    @Override
    public AoaNoticeUserRelation findOneRe(Long relationId) {
        return aoaNoticeUserRelationMapper.findOne(relationId);
    }

    @Override
    public void saveRe(AoaNoticeUserRelation relation) {
        if (StringUtils.isEmpty(relation.getRelatinId())) {
            aoaNoticeUserRelationMapper.insertSelective(relation);
        } else {
            aoaNoticeUserRelationMapper.updateByPrimaryKeySelective(relation);
        }
    }

    @Override
    public void save(AoaNoticeList menu, Long userId) {
        if (StringUtils.isEmpty(menu.getNoticeId())) {
            menu.setNoticeTime(new Date());
            menu.setUserId(userId);
            aoaNoticeListMapper.insertSelective(menu);
            AoaUser user = aoaUserMapper.findOneById(userId);
            List<AoaUser> userList = aoaUserMapper.findbyFatherId(null, user.getFatherId());
            for (AoaUser user1 : userList) {
                aoaNoticeUserRelationMapper.insertSelective(new AoaNoticeUserRelation(0, menu.getNoticeId(), user1.getUserId()));
            }
        } else {
            menu.setModifyTime(new Date());
            aoaNoticeListMapper.updateByPrimaryKeySelective(menu);
        }
    }

    @Override
    public void deleteOne(Long noticeId) {
        AoaNoticeList notice = aoaNoticeListMapper.findOne(noticeId);
        List<AoaNoticeUserRelation> relationList = aoaNoticeUserRelationMapper.findByNoticeId(noticeId);
        aoaNoticeUserRelationMapper.delete(relationList);
        aoaNoticeListMapper.delete(noticeId);
    }

    @Override
    public List<Map<String, Object>> findMyNotice(Long userId) {
        return aoaNoticeListMapper.findMyNotice(userId);
    }

    @Override
    public List<Map<String, Object>> setList(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            AoaStatusList aoaStatusList = aoaStatusListMapper.findOne((Long) map.get("status_id"));
            AoaTypeList aoaTypeList = aoaTypeListMapper.findOneById((Long) map.get("type_id"));
            AoaUser user = aoaUserMapper.findOneById((Long) map.get("user_id"));
            map.put("status", aoaStatusList.getStatusName());
            map.put("type", aoaTypeList.getTypeName());
            map.put("statusColor", aoaStatusList.getStatusColor());
            map.put("userName", user.getUserName());
            map.put("deptName", user.getDept().getDeptName());
            map.put("contain", this.isForward((Long) map.get("relatin_notice_id"), (Long) map.get("relatin_user_id")));
        }
        return list;
    }

    @Override
    public void saves(List<AoaNoticeUserRelation> nurs) {
        for (AoaNoticeUserRelation aoaNoticeUserRelation:nurs){
            aoaNoticeUserRelationMapper.insertSelective(aoaNoticeUserRelation);
        }
    }

    @Override
    public AoaNoticeUserRelation findByUserIdAndNoticeId(Long userId, Long noticeId) {
        return aoaNoticeUserRelationMapper.findByUserIdAndNoticeId(userId,noticeId);
    }

    @Override
    public void deleteRe(AoaNoticeUserRelation relation) {
        aoaNoticeUserRelationMapper.deleteOne(relation.getRelatinId());
    }

    @Override
    public List<Map<String, Object>> sortMyNotice(Long userId, String baseKey, String type, String status, String time) {
        if (StringUtils.isEmpty(baseKey)) {
            return aoaNoticeListMapper.sortMyNotice(userId, null, type, status, time);
        }else {
            return aoaNoticeListMapper.sortMyNotice(userId, "%"+baseKey+"%", type, status, time);
        }
    }



    private int isForward(Long noticeId, Long userId) {
        int count = 1;
        if (aoaUserMapper.findbyFatherId(null, userId).size() > 0) {
            List<AoaUser> users = aoaUserMapper.findbyFatherId(null, userId);
            if (aoaNoticeUserRelationMapper.findByNoticeId(noticeId) != null) {
                List<AoaNoticeUserRelation> nul = aoaNoticeUserRelationMapper.findByNoticeId(noticeId);
                for (AoaNoticeUserRelation noticeUserRelation : nul) {
                    for (AoaUser aoaUser : users) {
                        if (aoaUser.getUserId()==userId){
                            continue;
                        }
                        if (aoaUser.getUserId() == noticeUserRelation.getRelatinUserId()) {
                            count = 2;
                        }
                    }
                }
                if (count != 2) {
                    count = 3;
                }
            }

        }
        return count;
    }
}
