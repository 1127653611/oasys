package cn.oasys.web.service.inter.process;

import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.process.*;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProcessService {
    List<AoaSubject> findByParentId(long l);

    List<AoaSubject> findByParentIdNot(long l);

    void index6(Model model, Long userId, int page, int size);

    void writefile(HttpServletResponse response, File file);

    void index5(AoaProcessList pro, String val, AoaUser lu, MultipartFile filePath, String userName) throws IOException;

    void saveburse(AoaProcessList pro, AoaBursement bu, List<AoaDetailsburse> mm);

    void saveReview(AoaUser reuser, AoaProcessList pro);

    void saveEvection(AoaProcessList pro, AoaEvection eve);

    void index8(AoaProcessList pro, String val, AoaUser lu, String userName);


    void saveOvertime(AoaProcessList pro, AoaOvertime eve);

    void saveRegular(AoaProcessList pro, AoaRegular eve);

    void saveHoliday(AoaProcessList pro, AoaHoliday eve);

    void saveResign(AoaProcessList pro, AoaResign eve);

    List<AoaProcessList> findByuserId(Long userId,String key);

    AoaProcessList findOne(Long proid);

    Map<String, Object> index3(String name, AoaUser user, String typename, AoaProcessList process);

    String index4(String typename, Model model,Long proid);

    List<AoaReviewed> index(AoaUser user, String key,Model model);

    List<AoaProcessList> findlastthree(Long userId);

    List<Map<String, Object>> index2(List<AoaReviewed> pagelist, AoaUser user);

    AoaReviewed findByProIdAndUserId(Long processId, Long userId);

    void findPro(Model model, Long processId, String typename,Long userid);

    List<Map<String, Object>> indexx(AoaProcessList process);

    void save(Long proId, Long userId, AoaReviewed reviewed, AoaProcessList pro, AoaUser user2);

    void updatereview(AoaReviewed re);

    void updatePro(AoaProcessList pro);

    void insertAtt(AoaAttendsList attend);

    void uodateObj(String typename, AoaProcessList pro, AoaUser shen, AoaUser me, AoaReviewed reviewed);
}
