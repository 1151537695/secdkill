package shu.xyj.secdkill.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shu.xyj.secdkill.vo.RespBean;
import shu.xyj.secdkill.vo.RespBeanEnum;

@RestControllerAdvice
public class GlobalExceptionHandle {


    // 异常处理类
    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandle(Exception e){
        // 如果抛出的是全局异常
        if (e instanceof GlobalException) {
            GlobalException ge = (GlobalException) e;
            return RespBean.error(ge.getRespBeanEnum());
        }

        // 如果抛出的是 validation 中参数校验的异常
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.Bind_ERROR);
            // 把返回中的消息设置成 IsMobileValidator 中的消息
            respBean.setMessage(be.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }

        return RespBean.error(RespBeanEnum.ERROR);
    }

}
