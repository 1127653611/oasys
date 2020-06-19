package cn.oasys.web.model.dao.role;

import cn.oasys.web.model.pojo.role.AoaRole;
import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaRoleMapper {
    @Select(" select role_id,role_name , role_value from aoa_role_")
    List<AoaRole> findAll();
    @Select("select role_id,role_name , role_value from aoa_role_ where role_id=#{roleid}")
    AoaRole findOne(@Param("roleid") Long roleId);

    @Insert("insert into aoa_role_ (role_name,role_value) values(#{roleName},#{roleValue})")
    @SelectKey(statement = "select last_insert_id()",before = false,keyProperty = "roleId",resultType=java.lang.Long.class)
    void insert(AoaRole role);

    @Update({"<script>" +
                "update  aoa_role_ " +
                "<set> " +
                    "<if test='#{roleName}!=null'> role_name = #{roleName},</if> " +
                    "<if test='#{roleValue}!=null'> role_value =#{roleValue},</if> " +
                "</set>" +
                "where role_id=#{roleId}"+
            "</script>"})
    void update(AoaRole role);

    @Delete("DELETE from aoa_role_ where role_id=#{id}")
    void deleteThis(@Param("id") Long lid);

    @Select(" select role_id,role_name , role_value from aoa_role_ where role_name like #{key}")
    List<AoaRole> findAllByLike(@Param("key") String key);
}