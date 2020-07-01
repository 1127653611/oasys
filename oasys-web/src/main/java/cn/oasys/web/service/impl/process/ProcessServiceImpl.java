package cn.oasys.web.service.impl.process;

import cn.oasys.web.model.dao.process.AoaSubjectMapper;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaDeptMapper;
import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.process.AoaSubject;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.process.ProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private AoaDeptMapper aoaDeptMapper;
    @Autowired
    private AoaPositionMapper aoaPositionMapper;
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaSubjectMapper aoaSubjectMapper;
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Override
    public List<AoaSubject> findByParentId(long l) {
        return aoaSubjectMapper.findByParentId(l);
    }

    @Override
    public List<AoaSubject> findByParentIdNot(long l) {
        return aoaSubjectMapper.findByParentIdNot(l);
    }

    @Override
    public void index6(Model model, Long userId, int page, int size) {
        AoaUser lu=aoaUserMapper.findOneById(userId);
        List<AoaTypeList> harrylist=aoaTypeListMapper.findByTypeModel("aoa_process_list");
        PageHelper.startPage(page,size);
        List<AoaUser> userlist=aoaUserMapper.findAll();
        PageInfo<AoaUser> pageuser=new PageInfo<>(userlist);
        // 查询部门表
        Iterable<AoaDept> deptlist = aoaDeptMapper.findAll();
        // 查职位表
        Iterable<AoaPosition> poslist = aoaPositionMapper.findAll();
        model.addAttribute("page", pageuser);
        model.addAttribute("emplist", userlist);
        model.addAttribute("deptlist", deptlist);
        model.addAttribute("poslist", poslist);
        model.addAttribute("url", "names");
        model.addAttribute("username", lu.getUserName());
        model.addAttribute("harrylist", harrylist);

    }
}
