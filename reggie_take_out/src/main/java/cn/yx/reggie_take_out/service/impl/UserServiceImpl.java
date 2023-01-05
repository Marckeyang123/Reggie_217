package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.mapper.UserMapper;
import cn.yx.reggie_take_out.pojo.User;
import cn.yx.reggie_take_out.service.UserService;
import cn.yx.reggie_take_out.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/*
@Auther：Y.
*@Date：2022/12/31  15:01
* 
**/

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean sendMsg(User user, HttpSession session) {
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String code  = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("验证码为：{}" ,code);
            //SMSUtils.sendMessage("不想起名字组的外卖","",phone,code);//向手机发送验证码
            session.setAttribute("phone",code);
            session.getAttribute("phone");
            return true;
        }
        return false;
    }

    @Override
    public boolean login(Map map, HttpSession session) {
        log.info("map集合：{}",map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object sessionCode  = session.getAttribute("phone");
        if(code!=null&&code.equals(sessionCode)){
            //判断是不是新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = this.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                this.save(user);
            }
            session.setAttribute("userId",user.getId());
            log.info("userId={}",session.getAttribute("userId"));
        }
        return true;
    }
}
