package cn.oasys.web.model.dao.note;


import cn.oasys.web.model.pojo.note.AoaCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaCatalogMapper {
    @Select("select catalog_id, catalog_name, parent_id, cata_user_id from aoa_catalog where cata_user_id=#{id}")
    List<AoaCatalog> findcatauser(Long userid);

    @Select("select catalog_id, catalog_name, parent_id, cata_user_id from aoa_catalog where catalog_id=#{id}")
    AoaCatalog findOne(long ca);
}