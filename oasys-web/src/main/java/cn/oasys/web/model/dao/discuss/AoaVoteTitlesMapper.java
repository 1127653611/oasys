package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaVoteTitles;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
@Mapper
public interface AoaVoteTitlesMapper {

    int deleteByPrimaryKey(Long titleId);

    int insertSelective(AoaVoteTitles record);

    AoaVoteTitles selectByPrimaryKey(Long titleId);

    int updateByPrimaryKeySelective(AoaVoteTitles record);

    List<AoaVoteTitles> findTitlesByVoteId(Long voteId);

    void insertSelectives(@Param("voteTitles") Set<AoaVoteTitles> voteTitles);

    @Delete("delete from aoa_vote_titles where vote_id =#{id}")
    void delete(Long voteId);
}