package cn.oasys.web.service.impl.user;

import cn.oasys.web.model.dao.user.AoaUserLoginRecordMapper;
import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;
import cn.oasys.web.service.inter.user.UserLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLoginRecordServiceImpl implements UserLoginRecordService {
    @Autowired
    private AoaUserLoginRecordMapper aoaUserLoginRecordMapper;
    @Override
    public Integer save(AoaUserLoginRecord aoaUserLoginRecord) {
        return aoaUserLoginRecordMapper.insertSelective(aoaUserLoginRecord);
    }
}
