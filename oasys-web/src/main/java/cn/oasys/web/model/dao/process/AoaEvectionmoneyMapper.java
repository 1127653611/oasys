package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaEvectionmoney;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaEvectionmoneyMapper {

    int insertSelective(AoaEvectionmoney record);

    int updateByPrimaryKeySelective(AoaEvectionmoney record);

    AoaEvectionmoney findByProId(Long process);
}