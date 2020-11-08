package cn.oasys.web.service.inter.discuss;

import cn.oasys.web.common.Msg;
import cn.oasys.web.model.pojo.discuss.*;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DiscussService {

    List<AoaDiscussList> paging( String baseKey, Long userId,int type,boolean superman,String type1, String time, String visitnum);

    List<Map<String, Object>>  packaging(List<AoaDiscussList> list);

    void addOneDiscuss(Long id);

    AoaDiscussList findOne(Long id);

    void voteServiceHandle(Model model, AoaUser user, AoaDiscussList discuss);

   void discussHandle(Model model, Long num, Long userId, int page, int size, Long selecttype, Long selectsort);

    void likeThisFun(Long userId,Long replyId,String type,Model model,String rigthNum);


    Msg saveReply(Long discussId, String model, String comment,Long userId);

    void saveDiscuss(AoaDiscussList discussList, AoaVoteList voteList, Set<AoaVoteTitles> voteTitles,boolean ischange);

    List<AoaVoteTitleUser> findByVoteTitlesAndUser(Long titleId, Long userId);

    void saveVotetittleUser(AoaVoteTitleUser aoaVoteTitleUser);

    AoaReplyList findReply(Long discussId);

    void deleteReply(Long discussId);

    void deleteCommentByReply(Long discussId);

    AoaCommentList findConment(Long discussId);

    void deleteComment(Long discussId);

    void deleteDiscuss(AoaDiscussList discuss);

    void deleteLoveByreply(Long discussId);

    Long count();
}
