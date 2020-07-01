package cn.oasys.web.service.inter.discuss;

import cn.oasys.web.model.pojo.discuss.AoaDiscussList;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface DiscussService {

    List<AoaDiscussList> paging( String baseKey, Long userId);

    List<Map<String, Object>>  packaging(List<AoaDiscussList> list);

    void addOneDiscuss(Long id);

    AoaDiscussList findOne(Long id);

    void voteServiceHandle(Model model, AoaUser user, AoaDiscussList discuss);

    void setDiscussMess(Model model, Long id, Long userId, int page, int size);
}
