package cn.oasys.web.model.dao.note;


import cn.oasys.web.model.pojo.note.AoaNotepaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AoaNotepaperMapper {

    List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<AoaNotepaper> findByUserIdOrderByCreateTimeDesc1(Long userId);

    void insertSelective(AoaNotepaper npaper);
}