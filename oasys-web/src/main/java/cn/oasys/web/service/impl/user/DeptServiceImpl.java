package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.user.AoaDeptMapper;
import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.service.inter.user.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private AoaPositionMapper aoaPositionMapper;
    @Autowired
    private AoaDeptMapper aoaDeptMapper;

    @Override
    public List<AoaDept> findAll() {
        return aoaDeptMapper.findAll();
    }

    @Override
    public AoaDept findOne(Long deptId) {
        return aoaDeptMapper.findOne(deptId);
    }

    @Override
    public void save(AoaDept dept) {
        if (dept.getDeptId()==null){
            aoaDeptMapper.save(dept);
            aoaPositionMapper.add(new AoaPosition("经理",dept.getDeptId()));
            aoaPositionMapper.add(new AoaPosition("文员",dept.getDeptId()));
        }else {
            aoaDeptMapper.update(dept);
        }
    }

    @Override
    public void delete(Long deletedeptid) {
       aoaPositionMapper.deleteByDeptId(deletedeptid);
       aoaDeptMapper.deleteById(deletedeptid);
    }

    @Override
    public void updateManage(Long deptid, Long newmanageid) {
        aoaDeptMapper.updateManage(deptid,newmanageid);
    }
}
