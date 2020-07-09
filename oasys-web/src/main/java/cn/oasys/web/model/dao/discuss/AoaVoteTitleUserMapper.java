package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaVoteTitleUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AoaVoteTitleUserMapper {
    public List<AoaVoteTitleUser> findAoaVoteTitleUserBytitleId(Long id);

    @Select("select  vote_title_user_id, vote_id, user_id, title_id from aoa_vote_title_user where vote_id=#{id}")
    List<AoaVoteTitleUser> findByVoteId(Long id);

    @Select("select  vote_title_user_id, vote_id, user_id, title_id from aoa_vote_title_user where title_id=#{id} and user_id=#{uid}")
    List<AoaVoteTitleUser> findByVoteTitlesAndUser(@Param("id") Long titleId, @Param("uid") Long userId);

    void insertSelective(AoaVoteTitleUser aoaVoteTitleUser);

    @Delete("delete from aoa_vote_title_user where vote_id=#{id}")
    void delete(Long voteId);
}