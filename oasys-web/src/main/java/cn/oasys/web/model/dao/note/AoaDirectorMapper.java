package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaDirector;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AoaDirectorMapper {

    AoaDirector findOne(Long did);

    void updateByPrimaryKeySelective(AoaDirector director);

    void insertSelective(AoaDirector director);

    @Delete("delete from aoa_director where director_id =#{id}")
    void deleteDirector(Long directorId);
}