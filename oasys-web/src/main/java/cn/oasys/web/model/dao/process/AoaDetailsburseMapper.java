package cn.oasys.web.model.dao.process;


import cn.oasys.web.model.pojo.process.AoaDetailsburse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaDetailsburseMapper {

    void insertSelective(AoaDetailsburse adb);

    List<AoaDetailsburse> findByBurs(Long bursementId);
}