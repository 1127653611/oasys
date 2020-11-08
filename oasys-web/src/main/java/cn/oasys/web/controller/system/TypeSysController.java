package cn.oasys.web.controller.system;

import cn.oasys.web.common.Msg;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.service.inter.system.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class TypeSysController {
    @Autowired
    private TypeService typeService;

    @RequestMapping("testsystype")
    public String testsystype(HttpServletRequest req) {
        Iterable<AoaTypeList> typeList = typeService.findAll();
        req.setAttribute("typeList", typeList);
        return "systemcontrol/typemanage";
    }

    @RequestMapping("typeedit")
    public String typeEdit(HttpServletRequest request) {
        if (!StringUtils.isEmpty(request.getParameter("typeid"))) {
            long typeid = Long.parseLong(request.getParameter("typeid"));
            AoaTypeList aoaTypeList = typeService.findOneById(typeid);
            request.setAttribute("typeObj", aoaTypeList);

        }
        return "systemcontrol/typeedit";
    }

    @RequestMapping("typecheck")
    public String typecheck(AoaTypeList aoaTypeList, HttpServletRequest req) {
        if (StringUtils.isEmpty(aoaTypeList.getTypeName())) {
            req.setAttribute("errormess", "名字不能为空");
            return "systemcontrol/typeedit";
        }
        if (aoaTypeList.getTypeId()<=47){
            req.setAttribute("msgg", 1);
            return "forward:/notlimit";
        }
        typeService.save(aoaTypeList);
        req.setAttribute("success", "后台验证成功");
        return "systemcontrol/typeedit";
    }

    @RequestMapping("deletetype")
    public String deleteThis(HttpServletRequest request) {
        Long typeId = Long.parseLong(request.getParameter("id"));
        if (typeId<=47){
            request.setAttribute("msgg", 1);
            return "forward:/notlimit";
        }
        typeService.deleteType(typeId);
        return "forward:/testsystype";
    }

    @RequestMapping("typetable")
    public String typetable(HttpServletRequest req) {
        if (!StringUtils.isEmpty(req.getParameter("baseKey"))) {
            req.setAttribute("baseKey",req.getParameter("baseKey"));
            String name = "%" + req.getParameter("baseKey") + "%";
            req.setAttribute("typeList", typeService.findByTypeNameLikeOrTypeModelLike(name));
        } else {
            Iterable<AoaTypeList> typeList = typeService.findAll();
            req.setAttribute("typeList", typeList);
        }
        return "systemcontrol/typetable";
    }
}
