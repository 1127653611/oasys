package cn.oasys.web.model.dao.role;

import cn.oasys.web.model.pojo.role.AoaRolePowerList;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AoaRolePowerListMapper {
    @Insert("insert into aoa_role_power_list (is_show, menu_id, role_id) values (#{isShow}, #{menuId}, #{roleId})")
    void sava(AoaRolePowerList aoaRolePowerList);
    @Delete("DELETE from aoa_role_power_list WHERE menu_id=#{id}")
    void deleteThis(@Param("id") Long menuId);

    @Select("select pk_id,is_show , menu_id , role_id from aoa_role_power_list where role_id=#{roleid} and menu_id=#{menuid}")
    AoaRolePowerList findbyroleidandmenuid(@Param("roleid") Long roleid,@Param("menuid") Long menuid);

    @Update("update aoa_role_power_list set is_show=#{isshow} where pk_id=#{id}")
    void update(@Param("id") Long pkId, @Param("isshow") Object i);

    @Delete("DELETE from aoa_role_power_list WHERE role_id=#{id}")
    void deleteThisByRoleId(@Param("id") Long lid);
}