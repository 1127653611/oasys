package cn.oasys.web.model.dao.process;


import cn.oasys.web.model.pojo.process.AoaProcessList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AoaProcessListMapper {

    void insertSelective(AoaProcessList pro);

    void updateByPrimaryKeySelective(AoaProcessList pro);

    List<AoaProcessList> findByuserId(@Param("uid") Long userId, @Param("key") String key);

    AoaProcessList findOne(Long proid);

    List<AoaProcessList> findlastthree(Long userId);
}