package cn.oasys.web.service.inter.user;

import cn.oasys.web.model.pojo.user.AoaUserLoginRecord;

import java.util.List;

public interface UserLoginRecordService {
    public Integer save(AoaUserLoginRecord aoaUserLoginRecord);

    List<AoaUserLoginRecord> getAll(long userid);
}
