package cn.oasys.web.service.impl.system;

import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.service.inter.system.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Override
    public List<AoaTypeList> findAll() {
        return aoaTypeListMapper.findAll();
    }

    @Override
    public AoaTypeList findOneById(long typeid) {
        return aoaTypeListMapper.findOneById(typeid);
    }

    @Override
    @Transactional
    public void save(AoaTypeList aoaTypeList) {
        if (aoaTypeList.getTypeId()==null){
            aoaTypeListMapper.save(aoaTypeList);
        }else {
            aoaTypeListMapper.update(aoaTypeList);
        }
    }

    @Override
    public void deleteType(Long typeId) {
        aoaTypeListMapper.deleteType(typeId);
    }

    @Override
    public List<AoaTypeList> findByTypeNameLikeOrTypeModelLike(String name) {
        return aoaTypeListMapper.findByTypeNameLikeOrTypeModelLike(name);
    }

    @Override
    public List<AoaTypeList> findByTypeModel(String aoa_bursement) {
        return aoaTypeListMapper.findByTypeModel(aoa_bursement);
    }
}
