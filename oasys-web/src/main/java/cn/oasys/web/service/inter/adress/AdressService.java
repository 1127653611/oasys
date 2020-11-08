package cn.oasys.web.service.inter.adress;

import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaDirector;
import cn.oasys.web.model.pojo.note.AoaDirectorUsers;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdressService {
    Set<String> findCatalogNameByUser(Long userId);

    List<AoaDirectorUsers> findByUserAndShareuserNotNullAndHandle(Long userId, int i);

    List<Map<String, Object>> allDirector(Long userId, String alph, String outtype, String baseKey);

    List<Map<String, Object>> fengzhaung(List<Map<String, Object>> directors);

    AoaDirectorUsers findOneDerectorUser(Long did);

    void updatedirector(AoaDirector director, AoaDirectorUsers directorUser);

    void insertdirector(AoaDirector director, AoaDirectorUsers directorUser);

    List<AoaDirectorUsers> findByCatalogNameAndUser(String oldtypename, Long userId);

    void savaList(List<AoaDirectorUsers> dus);

    void saveDerectorUser(AoaDirectorUsers dc);

    void updatedirectorUser(AoaDirectorUsers du);

    AoaAttachmentList findAtachment(Integer imagePath);

    List<AoaDirectorUsers> findByDirector(Long directorId);


    void deleteDerectorUser(Long directorUsersId);

    void deleteDirector(Long directorId);

    AoaDirectorUsers findByDirectorAndUser(Long directorId, Long userId);

    void insertlist(List<AoaDirectorUsers> dus);

    List<AoaDirectorUsers> findByShareuser(Long userId);

    List<AoaDirectorUsers> findBaseKey(String s, Long userId);

    List<AoaDirectorUsers> findByUserAndShareuserNotNull(Long userId);

    List<AoaDirectorUsers> findBaseKeyShare(String s, Long userId);

    Long count();
}
