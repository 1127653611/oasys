package cn.oasys.web.model.dao.system;

import cn.oasys.web.model.pojo.system.AoaSysMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaSysMenuMapper {
      List<AoaSysMenu> findbyparentxianall(@Param("id") Long id,
                                           @Param("roleid") Long roleid,
                                           @Param("bo") Boolean bo,
                                           @Param("le") Boolean le);
      AoaSysMenu findByChil(@Param("id") Long menuid,
                                           @Param("roleid") Long roleid,
                                           @Param("bo") Boolean bo,
                                           @Param("le") Boolean le);
      List<AoaSysMenu> findbyparentsxian(@Param("id") Long id,
                                         @Param("roleid") Long roleid,
                                         @Param("bo") Boolean bo,
                                         @Param("le") Boolean le);
      List<AoaSysMenu> findname(@Param("roleid") Long roleid,
                                         @Param("bo") Boolean bo,
                                         @Param("le") Boolean le,
                                         @Param("val")String val);
    @Select("select menu_id ,menu_grade , menu_icon , menu_name , menu_url ,parent_id , is_show , sort_id  from aoa_sys_menu  where parent_id = #{id} order by sort_id asc")
    List<AoaSysMenu> findByParentIdOrderBySortId(@Param("id") long l);

    @Select("select menu_id ,menu_grade , menu_icon , menu_name , menu_url ,parent_id , is_show , sort_id  from aoa_sys_menu  where parent_id<> #{id} order by sort_id asc")
    List<AoaSysMenu> findByParentIdNotOrderBySortId(@Param("id") long l);

    @Update("update aoa_sys_menu menu set menu.sort_id=(#{sortid} - #{num}) where menu.menu_id= #{menuid}")
    void changeSortId2(@Param("sortid") Integer sortId, @Param("num") Integer arithNum, @Param("menuid") Long menuId);

    @Update("update aoa_sys_menu menu set menu.sort_id=#{sortid} where menu.parent_id = #{parentid} and menu.sort_id=(#{sortid} - #{num})")
    void changeSortId(@Param("sortid") Integer sortId, @Param("num") Integer arithNum, @Param("parentid") Long parentId);

    @Select("select menu_id ,menu_grade , menu_icon , menu_name , menu_url ,parent_id , is_show , sort_id  from aoa_sys_menu where menu_id=#{id}")
    AoaSysMenu findOne(@Param("id") Long getId);

    void update(AoaSysMenu aoaSysMenu);

    Long sava(AoaSysMenu aoaSysMenu);

    @Delete("DELETE from aoa_sys_menu WHERE menu_id=#{id}")
    void deleteThis(@Param("id") Long menuId);
    @Select("select menu_id ,menu_grade , menu_icon , menu_name , menu_url ,parent_id , is_show , sort_id  from aoa_sys_menu where menu_name like #{name}")
    List<AoaSysMenu> findByMenuNameLike(@Param("name") String name);

    List<AoaSysMenu> findByRoleParentAll(@Param("id") Long id, @Param("roleid") Long roleid);

    List<AoaSysMenu> findByRoleChiledAll(@Param("id") Long id, @Param("roleid") Long roleid);
    @Select("select menu_id ,menu_grade , menu_icon , menu_name , menu_url ,parent_id , is_show , sort_id  from aoa_sys_menu ")
    List<AoaSysMenu> findAll();
}