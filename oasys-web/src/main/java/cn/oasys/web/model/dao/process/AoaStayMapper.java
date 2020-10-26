package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaStay;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaStayMapper {

    int insertSelective(AoaStay record);

    int updateByPrimaryKeySelective(AoaStay record);

    List<AoaStay> findByEvemoney(Long evectionmoneyId);
}