package cn.oasys.web.model.dao.process;

import cn.oasys.web.model.pojo.process.AoaEvection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AoaEvectionMapper {

    int insertSelective(AoaEvection record);

    int updateByPrimaryKeySelective(AoaEvection record);

    AoaEvection findByProId(Long process);
}