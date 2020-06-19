package cn.oasys.web.service.impl.process;

import cn.oasys.web.model.dao.process.AoaSubjectMapper;
import cn.oasys.web.model.pojo.process.AoaSubject;
import cn.oasys.web.service.inter.process.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private AoaSubjectMapper aoaSubjectMapper;
    @Override
    public List<AoaSubject> findByParentId(long l) {
        return aoaSubjectMapper.findByParentId(l);
    }

    @Override
    public List<AoaSubject> findByParentIdNot(long l) {
        return aoaSubjectMapper.findByParentIdNot(l);
    }
}
