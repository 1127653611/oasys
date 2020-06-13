package cn.oasys.web.model.dao.system;

import cn.oasys.web.model.pojo.system.AoaSysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaSysMenuMapper {
      List<AoaSysMenu> findbyparentxianall(@Param("id") Long id,
                                           @Param("roleid") Long roleid,
                                           @Param("bo") Boolean bo,
                                           @Param("le") Boolean le);
      List<AoaSysMenu> findbyparentsxian(@Param("id") Long id,
                                         @Param("roleid") Long roleid,
                                         @Param("bo") Boolean bo,
                                         @Param("le") Boolean le);
}