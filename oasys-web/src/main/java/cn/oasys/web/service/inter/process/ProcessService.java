package cn.oasys.web.service.inter.process;

import cn.oasys.web.model.pojo.process.AoaSubject;

import java.util.List;

public interface ProcessService {
    List<AoaSubject> findByParentId(long l);

    List<AoaSubject> findByParentIdNot(long l);
}
