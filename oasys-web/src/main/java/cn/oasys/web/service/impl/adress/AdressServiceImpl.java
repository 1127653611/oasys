package cn.oasys.web.service.impl.adress;

import cn.oasys.web.model.dao.note.AoaAttachmentListMapper;
import cn.oasys.web.model.dao.note.AoaDirectorMapper;
import cn.oasys.web.model.dao.note.AoaDirectorUsersMapper;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaDirector;
import cn.oasys.web.model.pojo.note.AoaDirectorUsers;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.adress.AdressService;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdressServiceImpl implements AdressService {
    @Autowired
    private AoaDirectorMapper aoaDirectorMapper;
    @Autowired
    private AoaAttachmentListMapper aoaAttachmentListMapper;
    @Autowired
    private AoaDirectorUsersMapper aoaDirectorUsersMapper;

    @Override
    public Set<String> findCatalogNameByUser(Long userId) {
        return aoaDirectorUsersMapper.findCatalogNameByUser(userId);
    }

    @Override
    public List<AoaDirectorUsers> findByUserAndShareuserNotNullAndHandle(Long userId, int i) {
        return aoaDirectorUsersMapper.findByUserAndShareuserNotNullAndHandle(userId, i);
    }

    @Override
    public List<Map<String, Object>> allDirector(Long userId, String alph, String outtype, String baseKey) {

        return aoaDirectorUsersMapper.allDirector(userId, alph, outtype, baseKey);
    }

    @Override
    public List<Map<String, Object>> fengzhaung(List<Map<String, Object>> addressList) {
        List<Map<String, Object>> adds = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            result.put("director_users_id", addressList.get(i).get("director_users_id"));
            result.put("director_id", addressList.get(i).get("director_id"));
            result.put("user_id", addressList.get(i).get("user_id"));
            result.put("catelog_name", addressList.get(i).get("catelog_name"));
            result.put("companyname", addressList.get(i).get("companyname"));
            result.put("user_name", addressList.get(i).get("user_name"));
            result.put("sex", addressList.get(i).get("sex"));
            result.put("phone_number", addressList.get(i).get("phone_number"));
            result.put("email", addressList.get(i).get("email"));
//			!Objects.isNull(atDao.findOne(d.getAttachment()))
            if (addressList.get(i).get("image_path") != null) {
                if (aoaAttachmentListMapper.findOne(Long.parseLong((addressList.get(i).get("image_path") + "")))!= null) {
                    result.put("image_path", aoaAttachmentListMapper.findOne(Long.parseLong((addressList.get(i).get("image_path") + ""))).getAttachmentPath());
                } else {
                    result.put("image_path", null);
                }
            } else {
                result.put("image_path", null);
            }
            adds.add(result);
        }
        return adds;
    }

    @Override
    public AoaDirectorUsers findOneDerectorUser(Long did) {
        return aoaDirectorUsersMapper.findOne(did);
    }

    @Override
    public void updatedirector(AoaDirector director, AoaDirectorUsers directorUser) {
        aoaDirectorMapper.updateByPrimaryKeySelective(director);
        aoaDirectorUsersMapper.updateByPrimaryKeySelective(directorUser);
    }

    @Override
    public void insertdirector(AoaDirector director, AoaDirectorUsers directorUser) {
        aoaDirectorMapper.insertSelective(director);
        directorUser.setDirectorId(director.getDirectorId());
        aoaDirectorUsersMapper.insertSelective(directorUser);
    }

    @Override
    public List<AoaDirectorUsers> findByCatalogNameAndUser(String oldtypename, Long userId) {
        return aoaDirectorUsersMapper.findByCatalogNameAndUser(oldtypename, userId);
    }

    @Override
    public void savaList(List<AoaDirectorUsers> dus) {
        if (dus != null && dus.size() > 0) {
            aoaDirectorUsersMapper.savalist(dus);
        }
    }

    @Override
    public void insertlist(List<AoaDirectorUsers> dus) {
        if (dus != null && dus.size() > 0) {
            aoaDirectorUsersMapper.insertlist(dus);
        }
    }

    @Override
    public List<AoaDirectorUsers> findByShareuser(Long userId) {
        return aoaDirectorUsersMapper.findByShareuser(userId);
    }

    @Override
    public List<AoaDirectorUsers> findBaseKey(String s, Long userId) {
        return aoaDirectorUsersMapper.findBaseKey(s, userId);
    }

    @Override
    public List<AoaDirectorUsers> findByUserAndShareuserNotNull(Long userId) {
        return aoaDirectorUsersMapper.findByUserAndShareuserNotNull(userId);
    }

    @Override
    public List<AoaDirectorUsers> findBaseKeyShare(String s, Long userId) {
        return aoaDirectorUsersMapper.findBaseKeyShare(s, userId);
    }

    @Override
    public Long count() {
        return aoaDirectorMapper.count();
    }

    @Override
    public void saveDerectorUser(AoaDirectorUsers dc) {
        aoaDirectorUsersMapper.insertSelective(dc);
    }

    @Override
    public void updatedirectorUser(AoaDirectorUsers du) {
        aoaDirectorUsersMapper.updateByPrimaryKeySelective(du);
    }

    @Override
    public AoaAttachmentList findAtachment(Integer imagePath) {
        return aoaAttachmentListMapper.findOne(imagePath);
    }

    @Override
    public List<AoaDirectorUsers> findByDirector(Long directorId) {
        return aoaDirectorUsersMapper.findByDirector(directorId);
    }

    @Override
    public void deleteDerectorUser(Long directorUsersId) {
        aoaDirectorUsersMapper.deleteDerectorUser(directorUsersId);
    }

    @Override
    public void deleteDirector(Long directorId) {
        aoaDirectorMapper.deleteDirector(directorId);
    }

    @Override
    public AoaDirectorUsers findByDirectorAndUser(Long directorId, Long userId) {
        return aoaDirectorUsersMapper.findByDirectorAndUser(directorId, userId);
    }


}
