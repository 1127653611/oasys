package cn.oasys.web.model.dao.user;


import cn.oasys.web.model.pojo.user.AoaDept;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface AoaDeptMapper {
    @Select("select dept_id , dept_addr , dept_fax , dept_name , dept_tel , deptmanager , email from aoa_dept ")
    List<AoaDept> findAll();
    @Select("select dept_id , dept_addr , dept_fax , dept_name , dept_tel , deptmanager , email from aoa_dept where dept_id=#{id}")
    AoaDept findOne(@Param("id") Long deptId);

    void save(AoaDept dept);

    void update(AoaDept dept);
    @Delete("delete from aoa_dept where dept_id=#{id}")
    void deleteById(@Param("id") Long deletedeptid);

    @Update("UPDATE  aoa_dept SET deptmanager=#{manage} WHERE dept_id=#{id}")
    void updateManage(@Param("id") Long deptid, @Param("manage") Long newmanageid);
}