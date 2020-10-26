package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaResign;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface AoaResignMapper {

    int insertSelective(AoaResign record);

    int updateByPrimaryKeySelective(AoaResign record);

    AoaResign findByProId(Long process);
}