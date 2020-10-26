package cn.oasys.web.service.impl.discuss;

import cn.oasys.web.common.Msg;
import cn.oasys.web.model.dao.discuss.*;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.discuss.*;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.discuss.DiscussService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.events.Comment;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private AoaLoveUserMapper aoaLoveUserMapper;
    @Autowired
    private AoaLoveDiscussUserMapper aoaLoveDiscussUserMapper;
    @Autowired
    private AoaCommentListMapper aoaCommentListMapper;
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaVoteTitleUserMapper aoaVoteTitleUserMapper;
    @Autowired
    private AoaVoteTitlesMapper aoaVoteTitlesMapper;
    @Autowired
    private AoaVoteListMapper aoaVoteListMapper;
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;

    @Autowired
    private AoaDiscussListMapper aoaDiscussListMapper;
    @Autowired
    private AoaReplyListMapper aoaReplyListMapper;

    @Override
    public List<AoaDiscussList> paging(String baseKey, Long userId ,int type,boolean superman) {
        if (type==3) {
            if (!StringUtils.isEmpty(baseKey)) {
                return aoaDiscussListMapper.findByTitleLike("%" + baseKey + "%");
            }
            return aoaDiscussListMapper.findAll();
        } else if (type==2){
            if (!StringUtils.isEmpty(baseKey)) {
                return aoaDiscussListMapper.findByUserAndTitleLike(userId, "%" + baseKey + "%");
            }
            return aoaDiscussListMapper.findByUser(userId);
        }else {
            if (superman) {
                if (!StringUtils.isEmpty(baseKey)) {
                    return aoaDiscussListMapper.findByTitleLike("%" + baseKey + "%");
                }
                return aoaDiscussListMapper.findAll();
            }else {
                return null;
            }
        }
    }

    @Override
    public List<Map<String, Object>> packaging(List<AoaDiscussList> list) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", list.get(i).getDiscussId());
            result.put("typeName", aoaTypeListMapper.findOneById(list.get(i).getTypeId()).getTypeName());
            result.put("userName", list.get(i).getAoaUser().getUserName());
            if (list.get(i).getAoaLoveDiscussUsers() == null) {
                result.put("likeNum", 0);
            } else {
                result.put("likeNum", list.get(i).getAoaLoveDiscussUsers().size());
            }
            result.put("commentsNum", getComments(list.get(i)));
            result.put("title", list.get(i).getTitle());
            result.put("createTime", list.get(i).getCreateTime());
            result.put("visitNum", list.get(i).getVisitNum());
            result.put("typecolor", aoaTypeListMapper.findOneById(list.get(i).getTypeId()).getTypeColor());
            listMap.add(result);
        }
        return listMap;
    }

    private int getComments(AoaDiscussList discuss) {
        int chatNum = 0;
        List<AoaReplyList> replyCols = discuss.getAoaReplyLists();
        if (replyCols.size() > 0) {
            Long[] replyLong = new Long[replyCols.size()];                            //用数组来结束所有回复表的id
            for (int i = 0; i < replyCols.size(); i++) {
                replyLong[i] = replyCols.get(i).getReplyId();
            }
            List<AoaCommentList> commentList = aoaCommentListMapper.findComments(replyLong);            //in 查找所有回复id的所有评论
            chatNum = commentList.size() + replyCols.size();
        }
        return chatNum;
    }

    @Override
    public void addOneDiscuss(Long id) {
        AoaDiscussList discuss = aoaDiscussListMapper.findOne(id);
        discuss.setVisitNum(discuss.getVisitNum() + 1);
        aoaDiscussListMapper.updateByPrimaryKeySelective(discuss);
    }

    @Override
    public AoaDiscussList findOne(Long id) {
        return aoaDiscussListMapper.findOne(id);
    }

    @Override
    public void voteServiceHandle(Model model, AoaUser user, AoaDiscussList discuss) {
        if (!Objects.isNull(discuss.getAoaVoteList())) {
            List<AoaVoteTitles> voteTitles = (List<AoaVoteTitles>) discuss.getAoaVoteList().getAoaVoteTitles();
            List<Map<String, Object>> voteTitlesList = new ArrayList<>();
            boolean isvote=false;
            for (int i = 0; i < voteTitles.size(); i++) {
                Map<String, Object> result = new HashMap<>();
                result.put("titleId", voteTitles.get(i).getTitleId());
                result.put("title", voteTitles.get(i).getTitle());
                result.put("users", voteTitles.get(i).getAoaVoteTitleUsers());
                result.put("color", voteTitles.get(i).getColor());
                result.put("count", voteTitles.get(i).getAoaVoteTitleUsers().size());
                result.put("countNum", aoaVoteTitleUserMapper.findByVoteId(discuss.getVoteId()).size());
                boolean contain=true;
                List<AoaVoteTitleUser> li=aoaVoteTitleUserMapper.findByVoteTitlesAndUser(voteTitles.get(i).getTitleId(), user.getUserId());
                if (li==null || li.isEmpty()){
                    contain=false;
                }else {
                    if (discuss.getAoaVoteList().getSelectone()==1) {
                        isvote = true;
                    }
                }
                result.put("contain", contain);
                voteTitlesList.add(result);
            }
            AoaVoteList vote = discuss.getAoaVoteList();
            Date date = new Date();
            if (date.getTime() < vote.getStartTime().getTime()) {
                model.addAttribute("dateType", 1);
            } else if (date.getTime() > vote.getEndTime().getTime()) {
                model.addAttribute("dateType", 2);
            } else {
                model.addAttribute("dateType", 3);
            }
            model.addAttribute("isvote",isvote);
            model.addAttribute("voteTitles", voteTitlesList);
            model.addAttribute("voteList", vote);
        }
    }


    @Override
    public void discussHandle(Model model, Long num, Long userId, int page, int size, Long selectType, Long selectSort) {

        String pa = null;
        if (!StringUtils.isEmpty(selectSort) && selectSort == 1) {
            pa = "DESC";
        } else {
            pa = "ASC";
        }

        AoaDiscussList discuss = aoaDiscussListMapper.findOne(num);
        AoaUser user = aoaUserMapper.findOneById(userId);

        List<AoaLoveDiscussUser> setUsers = discuss.getAoaLoveDiscussUsers();
        Boolean discussContain = false;
        for (AoaLoveDiscussUser aoaLoveDiscussUser : discuss.getAoaLoveDiscussUsers()) {
            if (aoaLoveDiscussUser.getUserId().equals(user.getUserId())) {
                discussContain = true;
            }
        }
        int discussLikeNum = discuss.getAoaLoveDiscussUsers().size();
        model.addAttribute("discussContain", discussContain);
        model.addAttribute("discussLikeNum", discussLikeNum);
        model.addAttribute("setUsers", setUsers);
        AoaUser user2 = null;
        if (!StringUtils.isEmpty(selectType)) {
            model.addAttribute("selectType", selectType);
            user2 = aoaUserMapper.findOneById(selectType);
        }
        PageHelper.startPage(page, size);
        List<AoaReplyList> replyPage = null;
        if (!StringUtils.isEmpty(selectType)) {
            replyPage = aoaReplyListMapper.findByDiscussAndUser(num, user2.getUserId(), pa);
        } else {
            replyPage = aoaReplyListMapper.findByDiscuss(num, pa);                //根据讨论id找到所有的回复表
        }
        PageInfo<AoaReplyList> pageInfo = new PageInfo<>(replyPage);
        List<AoaReplyList> replyCols = discuss.getAoaReplyLists();
        ;
        List<Map<String, Object>> replys = this.replyPackaging(replyPage, userId);        //对回复表字段进行封装，主要是为了获取到评论数
        if (replyCols.size() > 0) {
            Long[] replyLong = new Long[replyCols.size()];                            //用数组来结束所有回复表的id
            for (int i = 0; i < replyCols.size(); i++) {
                replyLong[i] = replyCols.get(i).getReplyId();
            }
            List<AoaCommentList> commentList = aoaCommentListMapper.findComments(replyLong);            //in 查找所有回复id的所有评论
            List<Map<String, Object>> commentMap = this.commentPackaging(commentList);    //对评论字段进行封装
            model.addAttribute("commentList", commentMap);
            int chatNum = commentList.size() + replyCols.size();
            model.addAttribute("chatNum", chatNum);
        } else {
            model.addAttribute("chatNum", 0);
        }
        model.addAttribute("replyList", replys);
        model.addAttribute("discuss", discuss);
        model.addAttribute("page", pageInfo);
        model.addAttribute("user", discuss.getAoaUser());
    }

    @Override
    public void likeThisFun(Long userId, Long replyId, String type, Model model, String rightNum) {
        if (type.equals("discuss")) {
            AoaDiscussList aoaDiscussList = aoaDiscussListMapper.findOne(replyId);
            boolean islike = false;
            for (AoaLoveDiscussUser user : aoaDiscussList.getAoaLoveDiscussUsers()) {
                if (user.getUserId().equals(userId)) {
                    islike = true;
                }
            }
            if (islike) {
                aoaLoveDiscussUserMapper.deleteUser(userId, replyId);
            } else {
                aoaLoveDiscussUserMapper.insert(userId, replyId);
            }
            AoaUser user = aoaUserMapper.findOneById(userId);
            AoaDiscussList discuss=aoaDiscussListMapper.findOne(replyId);
            List<AoaLoveDiscussUser> setUsers = discuss.getAoaLoveDiscussUsers();
            Boolean discussContain = false;
            for (AoaLoveDiscussUser aoaLoveDiscussUser : discuss.getAoaLoveDiscussUsers()) {
                if (aoaLoveDiscussUser.getUserId().equals(user.getUserId())) {
                    discussContain = true;
                }
            }
            List<AoaReplyList> replyCols = discuss.getAoaReplyLists();
            if (replyCols.size() > 0) {
                Long[] replyLong = new Long[replyCols.size()];                            //用数组来结束所有回复表的id
                for (int i = 0; i < replyCols.size(); i++) {
                    replyLong[i] = replyCols.get(i).getReplyId();
                }
                List<AoaCommentList> commentList = aoaCommentListMapper.findComments(replyLong);            //in 查找所有回复id的所有评论
                List<Map<String, Object>> commentMap = this.commentPackaging(commentList);    //对评论字段进行封装
                model.addAttribute("commentList", commentMap);
                int chatNum = commentList.size() + replyCols.size();
                model.addAttribute("chatNum", chatNum);
            } else {
                model.addAttribute("chatNum", 0);
            }
            int discussLikeNum = discuss.getAoaLoveDiscussUsers().size();
            model.addAttribute("discussContain", discussContain);
            model.addAttribute("discussLikeNum", discussLikeNum);
            model.addAttribute("setUsers", setUsers);
            model.addAttribute("discuss", discuss);

        } else if (type.equals("reply")) {
            AoaReplyList replyList = aoaReplyListMapper.findOne(replyId);
            boolean islike = false;
            for (AoaLoveUser aoaLoveUser : replyList.getAoaLoveUsers()) {
                if (aoaLoveUser.getUserId().equals(userId)) {
                    islike = true;
                }
            }
            if (islike) {
                aoaLoveUserMapper.deleteUser(userId, replyId);
            } else {
                aoaLoveUserMapper.insert(userId, replyId);
            }
            AoaReplyList reply = aoaReplyListMapper.findOne(replyId);
            List<AoaLoveUser> users = reply.getAoaLoveUsers();
            if (users == null) {
                model.addAttribute("contain", false);
                model.addAttribute("likenum", 0);
            } else {
                boolean contain = false;
                for (AoaLoveUser aoaLoveUser : users) {
                    if (aoaLoveUser.getUserId().equals(userId)) {
                        contain = true;
                    }
                }
                model.addAttribute("contain", contain);
                model.addAttribute("likeNum", users.size());
            }
            model.addAttribute("rightNum", rightNum);
            model.addAttribute("comments", aoaCommentListMapper.findByReoly(replyId).size());
            model.addAttribute("reply", reply);
            model.addAttribute("users", users);

        }
    }

    @Override
    public Msg saveReply(Long discussId, String module, String comment,Long userId) {
        AoaDiscussList dis=null;
        Long num = null;
        if ("discuss".equals(module)) {
            num = discussId;
            aoaReplyListMapper.insertSelective(new AoaReplyList(null,new Date(),num,userId, comment));
        } else {
            AoaReplyList replyyy = aoaReplyListMapper.findOne(discussId);
            num = replyyy.getDiscussId();
            aoaCommentListMapper.insertSelective(new AoaCommentList(null,comment,new Date(),userId,discussId));
        }
        return Msg.success();
    }

    @Override
    public void saveDiscuss(AoaDiscussList discussList, AoaVoteList voteList, Set<AoaVoteTitles> voteTitles,boolean ischange) {
        if (ischange){
            if (voteList!=null){
                aoaVoteListMapper.updateByPrimaryKeySelective(voteList);
            }
            aoaDiscussListMapper.updateByPrimaryKeySelective(discussList);
        }else {
            if (voteTitles!=null && !voteTitles.isEmpty()){
                aoaVoteListMapper.insertSelective(voteList);

                for (AoaVoteTitles aoaVoteTitles:voteTitles) {
                    aoaVoteTitles.setVoteId(voteList.getVoteId());
                }
                aoaVoteTitlesMapper.insertSelectives(voteTitles);
                discussList.setVoteId(voteList.getVoteId());
            }
            aoaDiscussListMapper.insertSelective(discussList);
        }
    }

    @Override
    public List<AoaVoteTitleUser> findByVoteTitlesAndUser(Long titleId, Long userId) {
        return aoaVoteTitleUserMapper.findByVoteTitlesAndUser(titleId,userId);
    }

    @Override
    public void saveVotetittleUser(AoaVoteTitleUser aoaVoteTitleUser) {
        aoaVoteTitleUserMapper.insertSelective(aoaVoteTitleUser);
    }

    @Override
    public AoaReplyList findReply(Long discussId) {
        return aoaReplyListMapper.findOne(discussId);
    }

    @Override
    public void deleteReply(Long discussId) {
        aoaReplyListMapper.delete(discussId);
    }

    @Override
    public void deleteCommentByReply(Long discussId) {
        aoaCommentListMapper.deleteByReply(discussId);
    }

    @Override
    public AoaCommentList findConment(Long discussId) {
        return aoaCommentListMapper.findone(discussId);
    }

    @Override
    public void deleteComment(Long discussId) {
        aoaCommentListMapper.delete(discussId);
    }

    @Override
    public void deleteDiscuss(AoaDiscussList discuss) {

        List<AoaReplyList> aoaReplyLists=discuss.getAoaReplyLists();
        if (aoaReplyLists.size()>0){
            Long[] replyLong = new Long[aoaReplyLists.size()];                            //用数组来结束所有回复表的id
            for (int i = 0; i < aoaReplyLists.size(); i++) {
                replyLong[i] = aoaReplyLists.get(i).getReplyId();
            }
            aoaCommentListMapper.deleteByReplys(replyLong);
            aoaLoveUserMapper.deleteByReply(replyLong);
        }
        AoaVoteList aoaVoteList=discuss.getAoaVoteList();

        aoaReplyListMapper.deleteBydiscussId(discuss.getDiscussId());
        aoaLoveDiscussUserMapper.deleteBydiscuss(discuss.getDiscussId());
        aoaDiscussListMapper.delete(discuss.getDiscussId());
        if (aoaVoteList!=null){
            aoaVoteTitleUserMapper.delete(aoaVoteList.getVoteId());
            aoaVoteTitlesMapper.delete(aoaVoteList.getVoteId());
            aoaVoteListMapper.delete(aoaVoteList.getVoteId());
        }
    }

    @Override
    public void deleteLoveByreply(Long discussId) {
        aoaLoveUserMapper.deleteLoveByreply(discussId);
    }

    @Override
    public Long count() {
        return aoaDiscussListMapper.count();
    }

    private List<Map<String, Object>> commentPackaging(List<AoaCommentList> commentList) {
        List<Map<String, Object>> commentMap = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("commentId", commentList.get(i).getCommentId());
            map.put("comment", commentList.get(i).getComment());
            map.put("time", commentList.get(i).getTime());
            map.put("user", commentList.get(i).getAoaUser());
            map.put("reply", commentList.get(i).getReplyId());
            commentMap.add(map);
        }
        return commentMap;
    }

    private List<Map<String, Object>> replyPackaging(List<AoaReplyList> replyList, Long userId) {

        List<Map<String, Object>> replyMap = new ArrayList<>();
        for (int i = 0; i < replyList.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            if (replyList.get(i) == null || replyList.get(i).getAoaLoveUsers() == null) {
                result.put("contain", false);
                result.put("likenum", 0);
            } else {
                boolean contain = false;
                for (AoaLoveUser aoaLoveUser : replyList.get(i).getAoaLoveUsers()) {
                    if (aoaLoveUser.getUserId().equals(userId)) {
                        contain = true;
                    }
                }
                result.put("contain", contain);
                result.put("likenum", replyList.get(i).getAoaLoveUsers().size());
            }
            result.put("count", replyList.get(i).getAoaCommentLists().size());
            result.put("replyLikeUsers", replyList.get(i).getAoaLoveUsers());

            result.put("replyId", replyList.get(i).getReplyId());
            result.put("replayTime", replyList.get(i).getReplayTime());
            result.put("content", replyList.get(i).getContent());
            result.put("user", replyList.get(i).getAoaUser());
            result.put("discuss", replyList.get(i).getAoaDiscussList());
            replyMap.add(result);
        }
        return replyMap;
    }

}
