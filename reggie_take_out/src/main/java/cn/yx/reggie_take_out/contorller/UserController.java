package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.User;
import cn.yx.reggie_take_out.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/*
@Auther：Y.
*@Date：2022/12/31  15:29
* localhost:8091/front/page/login.html
**/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    //发送手机验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        if(userService.sendMsg(user,session)){
            return R.success("短信已经发送到手机");
        }
        return R.error("手机号码为空！");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody Map map,HttpSession session){
        if(userService.login(map,session)){
            Object userId = session.getAttribute("userId");
            log.info("userId={}",userId);
            return R.success("登录成功！");
        }
        return R.error("登录失败！");
    }



}
