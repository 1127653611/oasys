package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.service.inter.user.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private AoaPositionMapper aoaPositionMapper;

    @Override
    public List<AoaPosition> findByDeptidAndNameNotLike(long l,String name) {
        return aoaPositionMapper.findByDeptidAndNameNotLike(l,name);
    }

    @Override
    public List<AoaPosition> findByDeptid(Long deptid) {
        return aoaPositionMapper.findByDeptid(deptid);
    }

    @Override
    public List<AoaPosition> findByDeptidAndNameLike(Long deptid, String s) {
        return aoaPositionMapper.findByDeptidAndNameLike(deptid,s);
    }

    @Override
    public List<AoaPosition> findAll() {
        return aoaPositionMapper.findAll();
    }

    @Override
    public AoaPosition findOne(Long positionid) {
        return aoaPositionMapper.findOne(positionid);
    }

    @Override
    public void save(AoaPosition position) {
        if (position.getPositionId()==null){
            aoaPositionMapper.add(position);
        }else {
            aoaPositionMapper.update(position);
        }
    }

    @Override
    public void delete(Long positionid) {
        aoaPositionMapper.deleteByPositionId(positionid);
    }
}
