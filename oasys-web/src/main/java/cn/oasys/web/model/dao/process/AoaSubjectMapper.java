package cn.oasys.web.model.dao.process;


import cn.oasys.web.model.pojo.process.AoaSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaSubjectMapper {
    @Select("select  subject_id, name, parent_id from aoa_subject where parent_id <> #{id}")
    List<AoaSubject> findByParentIdNot(@Param("id") long l);

    @Select("select  subject_id, name, parent_id from aoa_subject where parent_id = #{id}")
    List<AoaSubject> findByParentId(@Param("id") long l);
}