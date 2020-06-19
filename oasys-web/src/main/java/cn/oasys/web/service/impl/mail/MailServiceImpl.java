package cn.oasys.web.service.impl.mail;

import cn.oasys.web.model.dao.mail.AoaMailReciverMapper;
import cn.oasys.web.model.pojo.mail.AoaMailReciver;
import cn.oasys.web.service.inter.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private AoaMailReciverMapper aoaMailReciverMapper;
    @Override
    public int findByReadAndDelAndReciverId(boolean isread, boolean isdel, long userid) {
        return aoaMailReciverMapper.findByReadAndDelAndReciverId(isread,isdel,userid);
    }
}
