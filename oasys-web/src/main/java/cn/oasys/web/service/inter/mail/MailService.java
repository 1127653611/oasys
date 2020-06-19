package cn.oasys.web.service.inter.mail;

import cn.oasys.web.model.pojo.mail.AoaMailReciver;

import java.util.List;

public interface MailService {
    public int findByReadAndDelAndReciverId(boolean isread,boolean isdel,long userid);
}
