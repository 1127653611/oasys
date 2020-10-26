package cn.oasys.web.model.dao.system;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AoaStatusListMapper {
    @Select("select status_id , status_color , status_model , status_name , sort_precent , sort_value  from aoa_status_list ")
    List<AoaStatusList> findAll();

    @Select("select status_id , status_color , status_model , status_name , sort_precent , sort_value  from aoa_status_list where status_id=#{id}")
    AoaStatusList findOne(@Param("id") Long statusid);

    @Insert("INSERT INTO aoa_status_list (status_color, status_model, status_name, sort_value, sort_precent) VALUES (#{statusColor},#{statusModel},#{statusName},#{sortValue},#{sortPrecent})")
    void save(AoaStatusList aoaStatusList);

    void update(AoaStatusList aoaStatusList);

    @Delete("DELETE FROM aoa_status_list WHERE status_id=#{id}")
    void deleteStatus(@Param("id") Long statusId);

    @Select("select status_id ,status_color , status_model , status_name , sort_precent , sort_value  from aoa_status_list  where status_name like #{name} or status_model like #{name}")
    List<AoaStatusList> findByStatusNameLikeOrStatusModelLike(@Param("name") String name);

    @Select("select status_name from aoa_status_list where status_id=#{id}")
    Object findname(@Param("id") Long statusId);

    @Select("select status_color from aoa_status_list where status_id=#{id}")
    Object findcolor(Long statusId);

    @Select("select status_id ,status_color , status_model , status_name , sort_precent , sort_value  from aoa_status_list where status_model=#{model}")
    List<AoaStatusList> findByStatusModel(@Param("model") String inform);

    @Select("select status_id ,status_color , status_model , status_name , sort_precent , sort_value  from aoa_status_list where status_model=#{model} and status_name =#{key}")
    AoaStatusList findByStatusModelAndStatusName(@Param("model") String model, @Param("key") String val);
}