package cn.oasys.web.model.dao.user;

import cn.oasys.web.model.pojo.user.AoaPosition;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaPositionMapper {
    @Insert("insert into aoa_position (deptid, describtion, level, name) values (#{deptid}, #{describtion}, #{level}, #{name})")
    void add(AoaPosition aoaPosition);
    @Delete("delete from aoa_position where deptid=#{id}")
    void deleteByDeptId(@Param("id") Long deletedeptid);

    @Select("select position_id ,deptid , describtion , level , name  from aoa_position  where deptid=#{id} and name not like #{name}")
    List<AoaPosition> findByDeptidAndNameNotLike(@Param("id") long l,@Param("name") String name);

    @Select("select position_id ,deptid , describtion , level , name  from aoa_position  where deptid=#{id} ")
    List<AoaPosition> findByDeptid(Long deptid);

    @Select("select position_id ,deptid , describtion , level , name  from aoa_position  where deptid=#{id} and name  like #{name}")
    List<AoaPosition> findByDeptidAndNameLike(@Param("id") Long deptid, @Param("name") String s);

    @Select("select position_id ,deptid , describtion , level , name  from aoa_position")
    List<AoaPosition> findAll();

    @Select("select position_id ,deptid , describtion , level , name  from aoa_position  where position_id=#{id}")
    AoaPosition findOne(@Param("id") Long positionid);

    void update(AoaPosition position);

    @Delete("delete from aoa_position where position_id=#{id}")
    void deleteByPositionId(@Param("id") Long positionid);
}