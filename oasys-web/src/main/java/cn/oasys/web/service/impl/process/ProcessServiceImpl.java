package cn.oasys.web.service.impl.process;

import cn.oasys.web.model.dao.attendce.AoaAttendsListMapper;
import cn.oasys.web.model.dao.process.*;
import cn.oasys.web.model.dao.system.AoaStatusListMapper;
import cn.oasys.web.model.pojo.attendce.AoaAttendsList;
import cn.oasys.web.model.pojo.process.*;
import cn.oasys.web.model.dao.system.AoaTypeListMapper;
import cn.oasys.web.model.dao.user.AoaDeptMapper;
import cn.oasys.web.model.dao.user.AoaPositionMapper;
import cn.oasys.web.model.dao.user.AoaUserMapper;
import cn.oasys.web.model.pojo.note.AoaAttachmentList;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaDept;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.adress.AdressService;
import cn.oasys.web.service.inter.process.ProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private AoaTrafficMapper aoaTrafficMapper;
    @Autowired
    private AoaStayMapper aoaStayMapper;
    @Autowired
    private AoaEvectionmoneyMapper aoaEvectionmoneyMapper;
    @Autowired
    private AoaRegularMapper aoaRegularMapper;
    @Autowired
    private AoaHolidayMapper aoaHolidayMapper;
    @Autowired
    private AoaOvertimeMapper aoaOvertimeMapper;
    @Autowired
    private AoaResignMapper aoaResignMapper;
    @Autowired
    private AoaEvectionMapper aoaEvectionMapper;
    @Autowired
    private AoaReviewedMapper aoaReviewedMapper;
    @Autowired
    private AoaBursementMapper aoaBursementMapper;
    @Autowired
    private AoaDetailsburseMapper aoaDetailsburseMapper;
    @Autowired
    private AoaProcessListMapper aoaProcessListMapper;
    @Autowired
    private AdressService adressService;
    @Autowired
    private AoaDeptMapper aoaDeptMapper;
    @Autowired
    private AoaPositionMapper aoaPositionMapper;
    @Autowired
    private AoaAttendsListMapper aoaAttendsListMapper;
    @Autowired
    private AoaUserMapper aoaUserMapper;
    @Autowired
    private AoaSubjectMapper aoaSubjectMapper;
    @Autowired
    private AoaTypeListMapper aoaTypeListMapper;
    @Autowired
    private AoaStatusListMapper aoaStatusListMapper;
    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

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
        AoaUser lu = aoaUserMapper.findOneById(userId);
        List<AoaTypeList> harrylist = aoaTypeListMapper.findByTypeModel("aoa_process_list");
        PageHelper.startPage(page, size);
        List<AoaUser> userlist = aoaUserMapper.findbyFatherId(null, lu.getFatherId());
        PageInfo<AoaUser> pageuser = new PageInfo<>(userlist);
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
    public void writefile(HttpServletResponse response, File file) {
        ServletOutputStream sos = null;
        FileInputStream aa = null;
        try {
            aa = new FileInputStream(file);
            sos = response.getOutputStream();
            // 读取文件问字节码
            byte[] data = new byte[(int) file.length()];
            IOUtils.readFully(aa, data);
            // 将文件流输出到浏览器
            IOUtils.write(data, sos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                sos.close();
                aa.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void index5(AoaProcessList pro, String val, AoaUser lu, MultipartFile filePath, String userName) throws IOException {
        pro.setTypeName(val);
        pro.setApplyTime(new Date());
        pro.setProcessUserId(lu.getUserId());
        pro.setStatusId(23L);
        pro.setShenuser(userName);
        pro.setIsChecked(0);
        AoaAttachmentList attaid = null;
        if (!StringUtil.isEmpty(filePath.getOriginalFilename())) {
            attaid = adressService.upload(filePath, lu.getUserId(), "aoa_bursement");
            pro.setProFileId(attaid.getAttachmentId());
        }
    }

    @Override
    public void index8(AoaProcessList pro, String val, AoaUser lu, String userName) {
        pro.setTypeName(val);
        pro.setApplyTime(new Date());
        pro.setProcessUserId(lu.getUserId());
        pro.setStatusId(23L);
        pro.setShenuser(userName);
    }


    @Override
    public void saveburse(AoaProcessList pro, AoaBursement bu, List<AoaDetailsburse> mm) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        bu.setProId(pro.getProcessId());
        aoaBursementMapper.insertSelective(bu);
        for (AoaDetailsburse adb : bu.getDetails()) {
            adb.setBursmentId(bu.getBursementId());
            aoaDetailsburseMapper.insertSelective(adb);
        }
    }

    @Override
    public void saveReview(AoaUser reuser, AoaProcessList pro) {
        AoaReviewed revie = new AoaReviewed();
        revie.setUserId(reuser.getUserId());
        revie.setStatusId(23L);
        revie.setProId(pro.getProcessId());
        aoaReviewedMapper.insertSelective(revie);
    }

    @Override
    public void saveOvertime(AoaProcessList pro, AoaOvertime eve) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        eve.setProId(pro.getProcessId());
        aoaOvertimeMapper.insertSelective(eve);
    }

    @Override
    public void saveRegular(AoaProcessList pro, AoaRegular eve) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        eve.setProId(pro.getProcessId());
        aoaRegularMapper.insertSelective(eve);
    }

    @Override
    public void saveHoliday(AoaProcessList pro, AoaHoliday eve) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        eve.setProId(pro.getProcessId());
        aoaHolidayMapper.insertSelective(eve);
    }

    @Override
    public void saveResign(AoaProcessList pro, AoaResign eve) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        eve.setProId(pro.getProcessId());
        aoaResignMapper.insertSelective(eve);
    }

    @Override
    public List<AoaProcessList> findByuserId(Long userId, String key) {
        if (StringUtils.isEmpty(key)) {
            return aoaProcessListMapper.findByuserId(userId, null);
        } else {
            return aoaProcessListMapper.findByuserId(userId, "%" + key + "%");
        }

    }

    @Override
    public AoaProcessList findOne(Long proid) {
        return aoaProcessListMapper.findOne(proid);
    }

    @Override
    public Map<String, Object> index3(String name, AoaUser user, String typename, AoaProcessList process) {
        Map<String, Object> result = new HashMap<>();
        String harryname = aoaTypeListMapper.findname(process.getDeeplyId());
        result.put("proId", process.getProcessId());
        result.put("harryname", harryname);
        result.put("processName", process.getProcessName());
        result.put("processDescribe", process.getProcessDes());
        if (("审核").equals(name)) {
            result.put("username", process.getUser().getUserName());//提单人员
            result.put("deptname", process.getUser().getDept().getDeptName());//部门
        } else if (("申请").equals(name)) {
            result.put("username", user.getUserName());
            result.put("deptname", process.getUser().getDept().getDeptName());
        }
        result.put("applytime", process.getApplyTime());
        if (!Objects.isNull(process.getProFileId())) {
            result.put("file", process.getAoaAttachmentList());
        } else {
            result.put("file", "file");
        }
        result.put("name", name);
        result.put("typename", process.getTypeName());
        result.put("startime", process.getStartTime());
        result.put("endtime", process.getEndTime());
        result.put("tianshu", process.getProcseeDays());
        result.put("statusid", process.getStatusId());
        if (process.getProFileId() != null) {
            result.put("filepath", process.getAoaAttachmentList().getAttachmentPath());
            if (process.getAoaAttachmentList().getAttachmentType().startsWith("image")) {
                result.put("filetype", "img");
            } else {
                result.put("filetype", "appli");
            }
        }
        return result;
    }


    @Override
    public String index4(String typename, Model model, Long process) {
        if (("费用报销").equals(typename)) {
            AoaUser audit = null;
            AoaBursement bu = aoaBursementMapper.findByProId(process);
            AoaUser prove = aoaUserMapper.findOneById(bu.getUserName());//证明人
            if (!Objects.isNull(bu.getOperationName())) {
                audit = aoaUserMapper.findOneById(bu.getOperationName());//最终审核人
            }
            List<AoaDetailsburse> detaillist = aoaDetailsburseMapper.findByBurs(bu.getBursementId());
            String type = aoaTypeListMapper.findname(bu.getTypeId());
            String money = numbertocn(bu.getAllMoney());
            model.addAttribute("prove", prove);
            model.addAttribute("audit", audit);
            model.addAttribute("type", type);
            model.addAttribute("bu", bu);
            model.addAttribute("money", money);
            model.addAttribute("detaillist", detaillist);
            return "process/serch";
        } else if (("出差费用").equals(typename)) {
            Double staymoney = 0.0;
            Double tramoney = 0.0;
            AoaEvectionmoney emoney = aoaEvectionmoneyMapper.findByProId(process);

            String money = numbertocn(emoney.getMoney());
            List<AoaStay> staylist = aoaStayMapper.findByEvemoney(emoney.getEvectionmoneyId());
            for (AoaStay stay : staylist) {
                staymoney += stay.getStayMoney();
            }
            List<AoaTraffic> tralist = aoaTrafficMapper.findByEvection(emoney.getEvectionmoneyId());
            for (AoaTraffic traffic : tralist) {
                tramoney += traffic.getTrafficMoney();
            }
            model.addAttribute("staymoney", staymoney);
            model.addAttribute("tramoney", tramoney);
            model.addAttribute("allmoney", money);
            model.addAttribute("emoney", emoney);
            model.addAttribute("staylist", staylist);
            model.addAttribute("tralist", tralist);
            return "process/evemonserch";
        } else if (("出差申请").equals(typename)) {
            AoaEvection eve = aoaEvectionMapper.findByProId(process);
            model.addAttribute("eve", eve);
            return "process/eveserach";
        } else if (("加班申请").equals(typename)) {
            AoaOvertime eve = aoaOvertimeMapper.findByProId(process);
            String type = aoaTypeListMapper.findname(eve.getTypeId());
            model.addAttribute("eve", eve);
            model.addAttribute("type", type);
            return "process/overserch";
        } else if (("请假申请").equals(typename)) {
            AoaHoliday eve = aoaHolidayMapper.findByProId(process);
            String type = aoaTypeListMapper.findname(eve.getTypeId());
            model.addAttribute("eve", eve);
            model.addAttribute("type", type);
            return "process/holiserch";
        } else if (("转正申请").equals(typename)) {
            AoaRegular eve = aoaRegularMapper.findByProId(process);
            model.addAttribute("eve", eve);

            return "process/reguserch";
        } else if (("离职申请").equals(typename)) {
            AoaResign eve = aoaResignMapper.findByProId(process);
            model.addAttribute("eve", eve);
            return "process/resserch";
        } else {
            return null;
        }
    }

    @Override
    public List<AoaReviewed> index(AoaUser user, String key,Model model) {
        if (StringUtils.isEmpty(key)){
            return aoaReviewedMapper.findByUserIdAndSort(null,user.getUserId(),false);
        }else {
            return aoaReviewedMapper.findByUserIdAndSort("%"+key+"%",user.getUserId(),false);
        }
    }

    @Override
    public List<AoaProcessList> findlastthree(Long userId) {
        return aoaProcessListMapper.findlastthree(userId);
    }

    @Override
    public List<Map<String, Object>> index2(List<AoaReviewed> prolist, AoaUser user) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < prolist.size(); i++) {
            String harryname=aoaTypeListMapper.findname(prolist.get(i).getAoaProcessList().getDeeplyId());
            AoaStatusList status=aoaStatusListMapper.findOne(prolist.get(i).getStatusId());
            Map<String, Object> result=new HashMap<>();
            result.put("typename", prolist.get(i).getAoaProcessList().getTypeName());
            result.put("title", prolist.get(i).getAoaProcessList().getProcessName());
            result.put("pushuser", prolist.get(i).getAoaProcessList().getUser().getUserName());
            result.put("applytime",  prolist.get(i).getAoaProcessList().getApplyTime());
            result.put("harry", harryname);
            result.put("statusname", status.getStatusName());
            result.put("statuscolor", status.getStatusColor());
            result.put("proid", prolist.get(i).getProId());
            list.add(result);

        }
        return list;
    }

    @Override
    public AoaReviewed findByProIdAndUserId(Long processId, Long userId) {
        return aoaReviewedMapper.findByProIdAndUserId(processId,userId);
    }

    @Override
    public void findPro(Model model, Long processId, String typename,Long userid) {
        if(("费用报销").equals(typename)){
            AoaBursement bu=aoaBursementMapper.findByProId(processId);
            model.addAttribute("bu", bu);

        }else if(("出差费用").equals(typename)){
            AoaEvectionmoney emoney=aoaEvectionmoneyMapper.findByProId(processId);
            model.addAttribute("bu", emoney);
        }else if(("转正申请").equals(typename)||("离职申请").equals(typename)){
            AoaUser zhuan=aoaUserMapper.findOneById(userid);
            model.addAttribute("position", zhuan);
        }
    }

    @Override
    public List<Map<String, Object>> indexx(AoaProcessList process) {
        List<Map<String,Object>> relist=new ArrayList<>();
        List<AoaReviewed> revie=aoaReviewedMapper.findByReviewedTimeNotNullAndProId(process.getProcessId());
        for (int i = 0; i <revie.size(); i++) {
            Map<String, Object> result=new HashMap<>();
            AoaUser u=aoaUserMapper.findOneById(revie.get(i).getUserId());
            AoaPosition po=u.getPosition();
            AoaStatusList status=aoaStatusListMapper.findOne(revie.get(i).getStatusId());
            result.put("poname", po.getName());
            result.put("username", u.getUserName());
            result.put("retime",revie.get(i).getReviewedTime());
            result.put("restatus",status.getStatusName());
            result.put("statuscolor",status.getStatusColor());
            result.put("des", revie.get(i).getAdvice());
            result.put("img",u.getImgPath());
            result.put("positionid",u.getPositionId());
            relist.add(result);
        }
        return relist;
    }

    @Override
    public void save(Long proId, Long userId, AoaReviewed reviewed, AoaProcessList pro, AoaUser user2) {
        AoaReviewed re=aoaReviewedMapper.findByProIdAndUserId(proId,userId);
        re.setAdvice(reviewed.getAdvice());
        re.setStatusId(reviewed.getStatusId());
        re.setReviewedTime(new Date());
        re.setStatusId(reviewed.getStatusId());
        aoaReviewedMapper.updateByPrimaryKeySelective(re);


        AoaReviewed re2=new AoaReviewed();
        re2.setProId(proId);
        re2.setUserId(user2.getUserId());
        re2.setStatusId(23L);
        aoaReviewedMapper.insertSelective(re2);

        pro.setShenuser(pro.getShenuser()+";"+user2.getUserName());
        pro.setStatusId(24L);//改变主表的状态
        aoaProcessListMapper.updateByPrimaryKeySelective(pro);
    }

    @Override
    public void updatereview(AoaReviewed re) {
        aoaReviewedMapper.updateByPrimaryKeySelective(re);
    }

    @Override
    public void updatePro(AoaProcessList pro) {
        aoaProcessListMapper.updateByPrimaryKeySelective(pro);
    }

    @Override
    public void insertAtt(AoaAttendsList attend) {
        aoaAttendsListMapper.insertSelective(attend);
    }

    @Override
    public void uodateObj(String typename, AoaProcessList pro, AoaUser shen, AoaUser me, AoaReviewed reviewed) {
        if (("费用报销").equals(typename)) {
            AoaBursement bu = aoaBursementMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                bu.setManagerAdvice(reviewed.getAdvice());
                aoaBursementMapper.updateByPrimaryKeySelective(bu);
            }
            if (me.getPositionId() == 5) {
                bu.setFinancialAdvice(reviewed.getAdvice());
                bu.setBurseTime(new Date());
                bu.setOperationName(me.getUserId());
                aoaBursementMapper.updateByPrimaryKeySelective(bu);
            }
        } else if (("出差费用").equals(typename)) {
            AoaEvectionmoney emoney = aoaEvectionmoneyMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                emoney.setManagerAdvice(reviewed.getAdvice());
                aoaEvectionmoneyMapper.updateByPrimaryKeySelective(emoney);
            }
            if (me.getPositionId()== 5) {
                emoney.setFinancialAdvice(reviewed.getAdvice());
                aoaEvectionmoneyMapper.updateByPrimaryKeySelective(emoney);
            }
        } else if (("出差申请").equals(typename)) {
            AoaEvection ev = aoaEvectionMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                ev.setManagerAdvice(reviewed.getAdvice());
                aoaEvectionMapper.updateByPrimaryKeySelective(ev);
            }
            if (me.getPositionId().equals(7L)) {
                ev.setPersonnelAdvice(reviewed.getAdvice());
                aoaEvectionMapper.updateByPrimaryKeySelective(ev);
            }
        } else if (("加班申请").equals(typename)) {
            AoaOvertime over = aoaOvertimeMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                over.setManagerAdvice(reviewed.getAdvice());
                aoaOvertimeMapper.updateByPrimaryKeySelective(over);
            }
            if (me.getPositionId().equals(7L)) {
                over.setPersonnelAdvice(reviewed.getAdvice());
                aoaOvertimeMapper.updateByPrimaryKeySelective(over);
            }
        } else if (("请假申请").equals(typename)) {
            AoaHoliday over = aoaHolidayMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                over.setManagerAdvice(reviewed.getAdvice());
                aoaHolidayMapper.updateByPrimaryKeySelective(over);
            }
            if (me.getPositionId().equals(7L)) {
                over.setPersonnelAdvice(reviewed.getAdvice());
                aoaHolidayMapper.updateByPrimaryKeySelective(over);
            }
        } else if (("转正申请").equals(typename)) {
            AoaRegular over = aoaRegularMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {
                over.setManagerAdvice(reviewed.getAdvice());
                aoaRegularMapper.updateByPrimaryKeySelective(over);
            }
            if (me.getPositionId().equals(7L)) {
                over.setPersonnelAdvice(reviewed.getAdvice());
                aoaRegularMapper.updateByPrimaryKeySelective(over);
            }
        } else if (("离职申请").equals(typename)) {

            AoaResign over = aoaResignMapper.findByProId(pro.getProcessId());
            if (shen.getFatherId().equals(me.getUserId())) {

                over.setManagerAdvice(reviewed.getAdvice());
                aoaResignMapper.updateByPrimaryKeySelective(over);
            }
            if (me.getPositionId() == 5) {
                over.setPersonnelAdvice(reviewed.getAdvice());
                aoaResignMapper.updateByPrimaryKeySelective(over);
            } else if (me.getPositionId().equals(7L)) {
                over.setFinancialAdvice(reviewed.getAdvice());
                aoaResignMapper.updateByPrimaryKeySelective(over);
            }
        }
    }


    private String numbertocn(Double money) {
        BigDecimal numberOfMoney = new BigDecimal(money);
        String s = number2CNMontrayUnit(numberOfMoney);
        return s.toString();
    }

    private String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }


    @Override
    public void saveEvection(AoaProcessList pro, AoaEvection eve) {
        if (StringUtils.isEmpty(pro.getProcessId())) {
            aoaProcessListMapper.insertSelective(pro);
        } else {
            aoaProcessListMapper.updateByPrimaryKeySelective(pro);
        }
        eve.setProId(pro.getProcessId());
        aoaEvectionMapper.insertSelective(eve);
    }


}
