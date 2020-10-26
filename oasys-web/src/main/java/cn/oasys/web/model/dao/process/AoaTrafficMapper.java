package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaTraffic;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaTrafficMapper {

    int insertSelective(AoaTraffic record);

    int updateByPrimaryKeySelective(AoaTraffic record);

    List<AoaTraffic> findByEvection(Long evectionmoneyId);
}