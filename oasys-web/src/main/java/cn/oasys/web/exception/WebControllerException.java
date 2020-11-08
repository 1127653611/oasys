package cn.oasys.web.exception;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ Description   :  TODO
 * @ Author        :  wang
 * @ CreateDate    :  2020/11/8 1:10
 */
@ControllerAdvice
public class WebControllerException {
    @ExceptionHandler(value = NameErrorException.class)
    public String nameErrHandler(Model model){
        model.addAttribute("error","请选择合适的人");
        return "common/proce";
    }
    @ExceptionHandler(value = Exception.class)
    public String exception(Model model,Exception e){
        model.addAttribute("error","服务器异常，请检查开发者日志");
        return "common/proce";
    }
    @ExceptionHandler(value = ClientAbortException.class)
    public String exception1(Model model,Exception e){
        model.addAttribute("error","文件已损坏");
        return "common/proce";
    }
}
