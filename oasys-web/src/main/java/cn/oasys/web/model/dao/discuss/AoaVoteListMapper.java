package cn.oasys.web.model.dao.discuss;

import cn.oasys.web.model.pojo.discuss.AoaVoteList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaVoteListMapper {
    AoaVoteList findById(Long id);

    void updateByPrimaryKeySelective(AoaVoteList voteList);

    void insertSelective(AoaVoteList voteList);

    @Delete("delete from aoa_vote_list where vote_id=#{id}")
    void delete(Long voteId);
}