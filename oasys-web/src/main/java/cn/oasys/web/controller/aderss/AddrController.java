package cn.oasys.web.controller.aderss;

import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.note.AoaDirector;
import cn.oasys.web.model.pojo.note.AoaDirectorUsers;
import cn.oasys.web.model.pojo.user.AoaUser;
import cn.oasys.web.service.inter.adress.AdressService;
import cn.oasys.web.service.inter.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RequestMapping("/")
@Controller
public class AddrController {
    @Autowired
    private AdressService adressService;
    @Autowired
    private UserService userService;
    @RequestMapping("addrmanage")
    public String addrmanage(@SessionAttribute("userId") Long userId, Model model,
                             @RequestParam(value="page",defaultValue="0") int page,
                             @RequestParam(value="size",defaultValue="10") int size) {
        Set<String> catalogs= adressService.findCatalogNameByUser(userId);
        PageHelper.startPage(page,size);
        List<AoaUser> users=userService.findAllBySort();
        PageInfo<AoaUser> pageInfo=new PageInfo<>(users);
        List<AoaDirectorUsers> nothandles=adressService.findByUserAndShareuserNotNullAndHandle(userId,0);
        model.addAttribute("count", nothandles.size());
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("users", users);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "inaddresspaging");
        return "address/addrmanage";
    }
    @RequestMapping("inaddresspaging")
    public String inAddress(@RequestParam(value="page",defaultValue="0") int page,Model model,
                            @RequestParam(value="size",defaultValue="10") int size,
                            @RequestParam(value="baseKey",required=false) String baseKey,
                            @RequestParam(value="alph",defaultValue="ALL") String alph){
        PageHelper.startPage(page,size);
        List<AoaUser> userspage=null;
        if(StringUtils.isEmpty(baseKey)){
            if("ALL".equals(alph)){
                userspage=userService.findAll();
            }else{
                userspage=userService.findByPinyinLike(alph.toLowerCase()+"%");
            }
        }else{
            if("ALL".equals(alph)){
                userspage=userService.findUsersByBaseKey("%"+baseKey+"%");
            }else{
                userspage=userService.findUsersByBaseKeyAndPinyinLike("%"+baseKey+"%", alph.toLowerCase()+"%");
            }
        }
        PageInfo<AoaUser> pageInfo=new PageInfo<>(userspage);
        if(!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey", baseKey);
        }

        model.addAttribute("users", userspage);
        model.addAttribute("page", pageInfo);
        model.addAttribute("url", "inaddresspaging");
        return "address/inaddrss";
    }
    @RequestMapping("inmessshow")
    public String inMessShow(Model model,@RequestParam(value="userId")Long userId){
        AoaUser user=userService.findOne(userId);
        model.addAttribute("user", user);
        return "address/inmessshow";
    }
    @RequestMapping("addaddress")
    public String addAddress(HttpServletRequest req, @RequestParam(value="did",required=false) Long did, HttpSession session,
                             @SessionAttribute("userId") Long userId, Model model){
        Set<String> calogs=adressService.findCatalogNameByUser(userId);
        model.addAttribute("calogs", calogs);
        if(!StringUtils.isEmpty(did)){
            AoaDirectorUsers director=adressService.findOneDerectorUser(did);
            if(Objects.isNull(director)|| !Objects.equals(director.getUserId(), userId)){
                return "redirect:/notlimit";
            }
            AoaDirector dr=director.getAoaDirector();
            model.addAttribute("director", dr);
            model.addAttribute("du", director);
        }
        return "address/addressedit";
    }
    @RequestMapping("outaddresspaging")
    public String outAddress(@RequestParam(value="pageNum",defaultValue="1") int page,Model model,
                             @RequestParam(value="baseKey",required=false) String baseKey,
                             @RequestParam(value="outtype",required=false) String outtype,
                             @RequestParam(value="alph",defaultValue="ALL") String alph,
                             @SessionAttribute("userId") Long userId
    ){
        PageHelper.startPage(page, 10);
        List<Map<String, Object>> directors=adressService.allDirector(userId, alph, outtype, baseKey);
        List<Map<String, Object>> adds=adressService.fengzhaung(directors);
        PageInfo<Map<String, Object>> pageinfo=new PageInfo<>(directors);
        if(!StringUtils.isEmpty(outtype)){
            model.addAttribute("outtype", outtype);
        }
        PageHelper.startPage(0,10);
        List<AoaUser> users=userService.findAll();
        PageInfo<AoaUser> userspage=new PageInfo<>(users);
        model.addAttribute("modalurl", "modalpaging");
        model.addAttribute("modalpage", userspage);
        model.addAttribute("users", users);
        model.addAttribute("userId", userId);
        model.addAttribute("baseKey", baseKey);
        model.addAttribute("directors", adds);
        model.addAttribute("page", pageinfo);
        model.addAttribute("url", "outaddresspaging");
        return "address/outaddrss";
    }
    @RequestMapping("modalpaging")
    public String modalPaging(@RequestParam(value="page",defaultValue="0") int page,Model model,
                              @RequestParam(value="size",defaultValue="10") int size,
                              @RequestParam(value="baseKey",required=false) String baseKey){
        PageHelper.startPage(page, size);
        List<AoaUser> users=null;
        if(!StringUtils.isEmpty(baseKey)){
            users=userService.findAllByLike("%"+baseKey+"%");
            model.addAttribute("baseKey", baseKey);
        }else{
            users=userService.findAll();
        }
        PageInfo<AoaUser> userspage=new PageInfo<>(users);
        model.addAttribute("modalurl", "modalpaging");
        model.addAttribute("modalpage", userspage);
        model.addAttribute("users", users);
        return "address/shareuser";
    }
    @RequestMapping("savaaddress")
    public String savaAddress(AoaDirector director, AoaDirectorUsers directorUser, @RequestParam("file") MultipartFile file,
                              Model model, @SessionAttribute("userId") Long userId, HttpServletRequest req) throws PinyinException, IllegalStateException, IOException {
        String pinyin=PinyinHelper.convertToPinyinString(director.getUserName(), "", PinyinFormat.WITHOUT_TONE);
        director.setPinyin(pinyin);
        if(file.getSize()>0){
            if (!file.getOriginalFilename().matches(".*(.jpg|.gif|.bmp|.png)$")){
                req.setAttribute("errormess","图片格式不对");
                return "forward:/addaddress?did="+directorUser.getDirectorUsersId();
            }
            AoaAttachmentList aoaAttachmentList=adressService.upload(file, userId,"aoa_bursement");
            director.setImagePath(Integer.parseInt(aoaAttachmentList.getAttachmentId()+""));
        }
        directorUser.setIsHandle(1);
        if (!StringUtils.isEmpty(directorUser.getDirectorUsersId())){
            adressService.updatedirector(director,directorUser);
        }else {
            director.setUserId(userId);
            directorUser.setUserId(userId);
            adressService.insertdirector(director,directorUser);
        }
        req.setAttribute("success","成功");
        return "address/addressedit";
    }
    @RequestMapping("changetypename")
    public String changeTypeName(@RequestParam(value="typename")String typename,
                                 @RequestParam(value="oldtypename")String oldtypename,
                                 @SessionAttribute("userId") Long userId
    ){
        List<AoaDirectorUsers> dus=adressService.findByCatalogNameAndUser(oldtypename, userId);
        for (AoaDirectorUsers directorUser : dus) {
            directorUser.setCatelogName(typename);
        }
        adressService.savaList(dus);
        return "redirect:/outaddresspaging";

    }
    @RequestMapping("addtypename")
    public String addTypename(@RequestParam(value="typename",required=false) String typename,
                              @RequestParam(value="oldtypename",required=false) String oldtypename,
                              @SessionAttribute("userId") Long userId,Model model,HttpServletRequest request){
        if (oldtypename!=null){
            List<AoaDirectorUsers> dus=adressService.findByCatalogNameAndUser(oldtypename, userId);
            for (AoaDirectorUsers directorUser : dus) {
                directorUser.setCatelogName(typename);
            }
            adressService.savaList(dus);
        }
        if(oldtypename==null){
            if (StringUtils.isEmpty(typename)|| typename==""){
                request.setAttribute("errormess","名字不能为空");
                return "address/addtypename";
            }
            AoaDirectorUsers dc=new AoaDirectorUsers(typename,userId);
            adressService.saveDerectorUser(dc);
        }
        Set<String> catalogs= adressService.findCatalogNameByUser(userId);
        model.addAttribute("catalogs", catalogs);
        return "address/addtypename";
    }
    @RequestMapping("deletetypename")
    public String deletetypename(@RequestParam(value="typename") String typename,@SessionAttribute("userId") Long userId){
        List<AoaDirectorUsers> dus=adressService.findByCatalogNameAndUser(typename, userId);
        for (AoaDirectorUsers directorUser : dus) {
            directorUser.setCatelogName(null);
        }
        adressService.savaList(dus);
        return "redirect:/outaddresspaging";
    }@RequestMapping("changethistype")
    public @ResponseBody
    Boolean changeType(@RequestParam(value="did")Long did, @SessionAttribute("userId") Long userId, @RequestParam(value="catalog")String catalog){
        AoaDirectorUsers du=adressService.findOneDerectorUser(did);
        du.setCatelogName(catalog);
        adressService.updatedirectorUser(du);
        return true;
    }

    @RequestMapping("outmessshow")
    public String outMessShow(Model model,@RequestParam("director") Long director,@SessionAttribute("userId") Long userId){

        AoaDirectorUsers du=adressService.findOneDerectorUser(director);
        if(Objects.isNull(du)){
            return "redirect:/notlimit";
        }
        AoaDirector d=du.getAoaDirector();
        if(d.getImagePath()!=null){
            model.addAttribute("imgpath", adressService.findAtachment(d.getImagePath()).getAttachmentPath());

        }else{
            model.addAttribute("imgpath", null);
        }
        model.addAttribute("d", d);
        return "address/outmessshow";
    }
    @RequestMapping("deletedirector")
    public String deleteDirector(@SessionAttribute("userId") Long userId,Model model,@RequestParam(value="did",required=false) Long did){
        AoaDirectorUsers du=adressService.findOneDerectorUser(did);
        List<AoaDirectorUsers> dires=adressService.findByDirector(du.getDirectorId());
        if(!Objects.equals(du.getUserId(), userId)){
            return "redirect:/notlimit";
        }
        List<AoaDirectorUsers> dus=adressService.findByCatalogNameAndUser(du.getCatelogName(), userId);
        if(dus.size()>1){
            adressService.deleteDerectorUser(du.getDirectorUsersId());
            if(dires.size()==1){
                adressService.deleteDirector(du.getDirectorId());
            }
        }else{
            /*当size=1时，说明这是最后一位拥有*/
            du.setDirectorId(null);
            adressService.updatedirectorUser(du);
            if(dires.size()==1){
                adressService.deleteDirector(du.getDirectorId());
            }

        }

        return "redirect:/outaddresspaging";
    }

    @RequestMapping("shareother")
    public String shareOther(@SessionAttribute("userId")Long userId,@RequestParam(value="directors[]") Long[] directors,
                             Model model,
                             @RequestParam(value="sharedirector") Long sharedirector){
        AoaDirectorUsers aoaDirectorUsers=adressService.findOneDerectorUser(sharedirector);
        List<AoaUser> users=new ArrayList<>();
        List<AoaDirectorUsers> dus=new ArrayList<>();
        for (int i = 0; i < directors.length; i++) {
            AoaUser shareuser=userService.findOne(directors[i]);
            AoaDirectorUsers du=adressService.findByDirectorAndUser(aoaDirectorUsers.getDirectorId(), directors[i]);
            if(Objects.isNull(du)){
                AoaDirectorUsers dir=new AoaDirectorUsers();
                dir.setUserId(directors[i]);
                dir.setShareUserId(userId);
                dir.setDirectorId(aoaDirectorUsers.getDirectorId());
                dir.setSharetime(new Date());
                dir.setIsHandle(0);
                dus.add(dir);
            }else{
                users.add(shareuser);
            }
        }
        adressService.insertlist(dus);
        if(users.size()>0){
            model.addAttribute("users", users);
            return "address/userhas";
        }
        return "address/sharesuccess";
    }
    @RequestMapping("modalshare")
    public String modalShare(@RequestParam(value="page",defaultValue="0") int page,Model model,
                             @RequestParam(value="size",defaultValue="10") int size){
        PageHelper.startPage(page,size);
        List<AoaUser> users=userService.findAll();
        PageInfo<AoaUser> userspage=new PageInfo<>(users);
        model.addAttribute("modalurl", "modalpaging");
        model.addAttribute("modalpage", userspage);
        model.addAttribute("users", users);
        return "address/modalshare";
    }
    @RequestMapping("mesharemess")
    public String meShareMess(@RequestParam(value="page",defaultValue="0")int page,
                              @RequestParam(value="size",defaultValue="5")int size,
                              @RequestParam(value="baseKey",required=false) String baseKey,
                              Model model,@SessionAttribute("userId") Long userId
    ){
        PageHelper.startPage(page,size);
        List<AoaDirectorUsers> duspage=null;
        if(!StringUtils.isEmpty(baseKey)){
            duspage=adressService.findBaseKey("%"+baseKey+"%",userId);
        }else{
            duspage=adressService.findByShareuser(userId);
        }
        PageInfo<AoaDirectorUsers> dus=new PageInfo<>(duspage);

        model.addAttribute("page", dus);
        model.addAttribute("dus", duspage);
        model.addAttribute("url", "mesharemess");
        if (!StringUtils.isEmpty(baseKey)){
            model.addAttribute("baseKey",baseKey);
        }
        return "address/mesharemess";
    }
    @RequestMapping("sharemess")
    public String shareMess(@RequestParam(value="page",defaultValue="0")int page,
                            @RequestParam(value="size",defaultValue="5")int size,
                            @RequestParam(value="baseKey",required=false) String baseKey,
                            @RequestParam(value="catalog",required=false)String catalog,
                            @RequestParam(value="duid",required=false)Long duid,
                            Model model,@SessionAttribute("userId") Long userId){

        if(!StringUtils.isEmpty(duid)){
            AoaDirectorUsers du=adressService.findOneDerectorUser(duid);
            du.setCatelogName(catalog);
            du.setIsHandle(1);
            adressService.updatedirectorUser(du);
        }
        PageHelper.startPage(page,size);
        List<AoaDirectorUsers> dus=null;
        if(!StringUtils.isEmpty(baseKey)){
            dus=adressService.findBaseKeyShare("%"+baseKey+"%",userId);
            model.addAttribute("baseKey", baseKey);
        }else{
            dus=adressService.findByUserAndShareuserNotNull(userId);
        }
        PageInfo<AoaDirectorUsers> duspage=new PageInfo<>(dus);
        Set<String> catalogs=adressService.findCatalogNameByUser(userId);
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("page", duspage);
        model.addAttribute("dus", dus);
        model.addAttribute("url", "sharemess");
        return "address/sharemess";
    }
}
