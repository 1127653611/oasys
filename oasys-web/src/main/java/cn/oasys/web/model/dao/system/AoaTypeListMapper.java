package cn.oasys.web.model.dao.system;

import cn.oasys.web.model.pojo.system.AoaTypeList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaTypeListMapper {
    @Select("select type_id  , type_color  , type_model  , type_name ,sort_value  from aoa_type_list ")
    List<AoaTypeList> findAll();

    @Select("select type_id  , type_color  , type_model  , type_name ,sort_value  from aoa_type_list where type_id=#{id}")
    AoaTypeList findOneById(@Param("id")long id);

    @Update("UPDATE aoa_type_list SET type_color=#{type.typeColor},type_model=#{type.typeModel},type_name=#{type.typeName},sort_value=#{type.sortValue} WHERE type_id=#{type.typeId}")
    void update(@Param("type") AoaTypeList aoaTypeList);

    @Delete("DELETE FROM aoa_type_list WHERE type_id=#{id}")
    void deleteType(@Param("id") Long typeId);

    @Insert("INSERT INTO aoa_type_list (type_id,type_color,type_model,type_name,sort_value) VALUES(#{type.typeId},#{type.typeColor},#{type.typeModel},#{type.typeName},#{type.sortValue})")
    void save(@Param("type") AoaTypeList aoaTypeList);

    @Select("select type_id  , type_color  , type_model  , type_name ,sort_value  from aoa_type_list where type_name like #{name} or type_model like #{name} ")
    List<AoaTypeList> findByTypeNameLikeOrTypeModelLike(@Param("name") String name);

    @Select("select type_id  , type_color  , type_model  , type_name ,sort_value  from aoa_type_list where type_model=${model} ")
    List<AoaTypeList> findByTypeModel(@Param("model") String aoa_bursement);
}