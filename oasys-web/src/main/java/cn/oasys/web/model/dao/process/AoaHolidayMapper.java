package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaHoliday;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaHolidayMapper {

    int insertSelective(AoaHoliday record);

    int updateByPrimaryKeySelective(AoaHoliday record);

    AoaHoliday findByProId(Long process);
}