package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.user.AoaUserLoginRecordMapper;
import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import cn.oasys.web.service.inter.user.UserLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class UserLoginRecordServiceImpl implements UserLoginRecordService {
    @Autowired
    private AoaUserLoginRecordMapper aoaUserLoginRecordMapper;
    @Override
    public Integer save(AoaUserLoginRecord aoaUserLoginRecord) {
        return aoaUserLoginRecordMapper.insertSelective(aoaUserLoginRecord);
    }

    @Override
    public List<AoaUserLoginRecord> getAll(long userid,String basekey) {
        if (StringUtils.isEmpty(basekey)) {
            return aoaUserLoginRecordMapper.getAllById(userid);
        }else {
            return aoaUserLoginRecordMapper.getAllByIdAndkey(userid,"%"+basekey+"%");
        }
    }

    @Override
    public Integer countlog(String format) {
        return aoaUserLoginRecordMapper.countlog(format);
    }


}
