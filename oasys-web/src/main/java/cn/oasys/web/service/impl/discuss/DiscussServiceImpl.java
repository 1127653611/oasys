package cn.oasys.web.service.impl.discuss;

import cn.oasys.web.model.dao.discuss.*;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.discuss.*;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.discuss.DiscussService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.xml.stream.events.Comment;
import java.util.*;

@Service
public class DiscussServiceImpl implements DiscussService {
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
    public List<AoaDiscussList> paging(String baseKey, Long userId) {
        if (StringUtils.isEmpty(userId)) {
            if (!StringUtils.isEmpty(baseKey)) {
                return aoaDiscussListMapper.findByTitleLike("%" + baseKey + "%");
            }
            return aoaDiscussListMapper.findAll();
        } else {
            AoaUser user = aoaUserMapper.findOneById(userId);
            if (user.getSuperman() == 1) {
                if (!StringUtils.isEmpty(baseKey)) {
                    return aoaDiscussListMapper.findByTitleLike("%" + baseKey + "%");
                }
                return aoaDiscussListMapper.findAll();
            } else {
                if (!StringUtils.isEmpty(baseKey)) {
                    return aoaDiscussListMapper.findByUserAndTitleLike(userId, "%" + baseKey + "%");
                }
                return aoaDiscussListMapper.findByUser(user.getUserId());
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
            if(list.get(i).getAoaLoveDiscussUsers()==null){
                result.put("likeNum", 0);
            }else{
                result.put("likeNum", list.get(i).getAoaLoveDiscussUsers().size());
            }
            result.put("commentsNum",1);
            result.put("title", list.get(i).getTitle());
            result.put("createTime", list.get(i).getCreateTime());
            result.put("visitNum", list.get(i).getVisitNum());
            result.put("typecolor", aoaTypeListMapper.findOneById(list.get(i).getTypeId()).getTypeColor());
            listMap.add(result);
        }
        return listMap;
    }

    @Override
    public void addOneDiscuss(Long id) {
        AoaDiscussList discuss=aoaDiscussListMapper.findOne(id);
        discuss.setVisitNum(discuss.getVisitNum()+1);
       aoaDiscussListMapper.updateByPrimaryKeySelective(discuss);
    }

    @Override
    public AoaDiscussList findOne(Long id) {
        return aoaDiscussListMapper.findOne(id);
    }

    @Override
    public void voteServiceHandle(Model model, AoaUser user, AoaDiscussList discuss) {
        if(!Objects.isNull(discuss.getAoaVoteList())){
            List<AoaVoteTitles> voteTitles= (List<AoaVoteTitles>) discuss.getAoaVoteList().getAoaVoteTitles();
            List<Map<String, Object>> voteTitlesList=new ArrayList<>();
            for (int i = 0; i < voteTitles.size(); i++) {
                Map<String, Object> result=new HashMap<>();
                result.put("titleId", voteTitles.get(i).getTitleId());
                result.put("title", voteTitles.get(i).getTitle());
                result.put("users", voteTitles.get(i).getAoaVoteTitleUsers());
                result.put("color", voteTitles.get(i).getColor());
                result.put("count", voteTitles.get(i).getAoaVoteTitleUsers().size());
                result.put("countNum", aoaVoteTitleUserMapper.findByVoteId(discuss.getVoteId()).size());
                result.put("contain",!Objects.isNull(aoaVoteTitleUserMapper.findByVoteTitlesAndUser(voteTitles.get(i).getTitleId(), user.getUserId())));
                voteTitlesList.add(result);
            }
            AoaVoteList vote=discuss.getAoaVoteList();
            Date date=new Date();
            if(date.getTime()<vote.getStartTime().getTime()){
                model.addAttribute("dateType", 1);
            }else if(date.getTime()>vote.getEndTime().getTime()){
                model.addAttribute("dateType", 2);
            }else{
                model.addAttribute("dateType", 3);
            }
            model.addAttribute("voteTitles", voteTitlesList);
            model.addAttribute("voteList", vote);
        }
    }

    @Override
    public void setDiscussMess(Model model, Long id, Long userId, int page, int size) {
        discussHandle(model, id, userId, page, size,null,null);
    }

    public void discussHandle(Model model, Long num, Long userId, int page, int size,Long selectType,Long selectSort) {
        String pa=null;
        if(!StringUtils.isEmpty(selectSort)&& selectSort==1){
            pa="DESC";
        }else{
            pa="ASC";
        }

        AoaDiscussList discuss=aoaDiscussListMapper.findOne(num);
        AoaUser user=aoaUserMapper.findOneById(userId);
        List<AoaUser> setUsers=null;
        Boolean discussContain=false;
        for (AoaLoveDiscussUser aoaLoveDiscussUser:discuss.getAoaLoveDiscussUsers()){
            if (aoaLoveDiscussUser.getUserId()==user.getUserId()){
                discussContain=true;
            }
            setUsers.add(aoaLoveDiscussUser.getAoaUser());
        }
        int discussLikeNum=discuss.getAoaLoveDiscussUsers().size();
        model.addAttribute("discussContain", discussContain);
        model.addAttribute("discussLikeNum", discussLikeNum);
        model.addAttribute("setUsers", setUsers);
        //这句是关键代码，从数据库拿到所有数据，也进行排序，只要在这进行判断
        PageHelper.startPage(page,size);
        List<AoaReplyList> replyPage = null;
        if(!StringUtils.isEmpty(selectType)){
            AoaUser user2=aoaUserMapper.findOneById(selectType);
            replyPage=aoaReplyListMapper.findByDiscussAndUser(num, user2.getUserId(), pa);
        }else{
            replyPage=aoaReplyListMapper.findByDiscuss(num,pa);				//根据讨论id找到所有的回复表
        }
        PageInfo<AoaReplyList> pageInfo =new PageInfo<>(replyPage);
        List<AoaReplyList> replyCols= (List<AoaReplyList>) discuss.getAoaReplyLists();;
        List<Map<String, Object>> replys=this.replyPackaging(replyPage,userId);		//对回复表字段进行封装，主要是为了获取到评论数
        if(replyCols.size()>0){
            Long[] replyLong=new Long[replyCols.size()];							//用数组来结束所有回复表的id
            for (int i = 0; i < replyCols.size(); i++) {
                replyLong[i]=replyCols.get(i).getReplyId();
            }
            List<AoaCommentList> commentList=aoaCommentListMapper.findComments(replyLong);			//in 查找所有回复id的所有评论
            List<Map<String, Object>> commentMap=this.commentPackaging(commentList);	//对评论字段进行封装
            model.addAttribute("commentList", commentMap);
            int chatNum=commentList.size()+replyCols.size();
            model.addAttribute("chatNum", chatNum);
        }		model.addAttribute("replyList", replys);
        model.addAttribute("discuss", discuss);
        model.addAttribute("page", pageInfo);
        model.addAttribute("user", discuss.getAoaUser());
    }

    private List<Map<String, Object>> commentPackaging(List<AoaCommentList> commentList) {
        return null;
    }

    private List<Map<String, Object>> replyPackaging(List<AoaReplyList> replyPage, Long userId) {
        return null;
    }

}
