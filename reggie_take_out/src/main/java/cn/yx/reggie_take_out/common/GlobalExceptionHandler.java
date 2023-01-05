package cn.yx.reggie_take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
@Auther：Y.
*@Date：2022/12/4  16:06
* 全局异常
**/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //sql异常处理方法
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> SqlExceptionHandler(SQLIntegrityConstraintViolationException exception){
        if(exception.getMessage().contains("Duplicate entry")){
            String [] split = exception.getMessage().split(" ");
            String msg = split[2]+"已经存在！";
           return R.error(msg);
        }else {
            log.error("出现"+exception.getMessage()+"异常！");
            return R.error("出现未知异常.....");
        }
    }

    //菜品分类删除异常
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException exception){
        return R.error(exception.getMessage());
    }
}
