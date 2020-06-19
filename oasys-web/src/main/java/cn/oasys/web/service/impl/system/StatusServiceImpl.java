package cn.oasys.web.service.impl.system;

import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.service.inter.system.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    private AoaStatusListMapper aoaStatusListMapper;
    @Override
    public List<AoaStatusList> findAll() {
        return aoaStatusListMapper.findAll();
    }

    @Override
    public AoaStatusList findOne(Long statusid) {
        return aoaStatusListMapper.findOne(statusid);
    }

    @Override
    public void save(AoaStatusList aoaStatusList) {
        if (aoaStatusList.getStatusId()==null){
            aoaStatusListMapper.save(aoaStatusList);
        }else {
            aoaStatusListMapper.update(aoaStatusList);
        }
    }

    @Override
    public void deleteStatus(Long statusId) {
        aoaStatusListMapper.deleteStatus(statusId);
    }

    @Override
    public List<AoaStatusList> findByStatusNameLikeOrStatusModelLike(String name) {
        return aoaStatusListMapper.findByStatusNameLikeOrStatusModelLike(name);
    }
}
