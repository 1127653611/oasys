package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaVoteTitles;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;
@Mapper
public interface AoaVoteTitlesMapper {

    int deleteByPrimaryKey(Long titleId);

    int insertSelective(AoaVoteTitles record);

    AoaVoteTitles selectByPrimaryKey(Long titleId);

    int updateByPrimaryKeySelective(AoaVoteTitles record);

    Set<AoaVoteTitles> findTitlesByVoteId(Long voteId);

}