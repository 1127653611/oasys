package cn.oasys.web.model.dao.process;


import cn.oasys.web.model.pojo.process.AoaBursement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AoaBursementMapper {

    void insertSelective(AoaBursement bu);

    AoaBursement findByProId(Long process);

    void updateByPrimaryKeySelective(AoaBursement bu);
}