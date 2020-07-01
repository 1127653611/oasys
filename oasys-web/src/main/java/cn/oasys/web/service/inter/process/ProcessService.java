package cn.oasys.web.service.inter.process;

import cn.oasys.web.model.pojo.process.AoaSubject;
import org.springframework.ui.Model;

import java.util.List;

public interface ProcessService {
    List<AoaSubject> findByParentId(long l);

    List<AoaSubject> findByParentIdNot(long l);

    void index6(Model model, Long userId, int page, int size);
}
