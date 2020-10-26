package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaRegular;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaRegularMapper {

    int insertSelective(AoaRegular record);

    int updateByPrimaryKeySelective(AoaRegular record);

    AoaRegular findByProId(Long process);
}