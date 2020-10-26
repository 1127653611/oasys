package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaOvertime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaOvertimeMapper {

    int insertSelective(AoaOvertime record);

    int updateByPrimaryKeySelective(AoaOvertime record);

    AoaOvertime findByProId(Long process);
}