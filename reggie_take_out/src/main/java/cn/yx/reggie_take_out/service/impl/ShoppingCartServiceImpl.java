package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.common.BaseContext;
import cn.yx.reggie_take_out.mapper.ShoppingCartMapper;
import cn.yx.reggie_take_out.pojo.ShoppingCart;
import cn.yx.reggie_take_out.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.LambdaMetafactory;
import java.time.LocalDateTime;
import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/13  23:43
* 
**/
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService{

    @Override
    @Transactional
    public ShoppingCart add(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //设置user_id,指定是哪个用户的订单数据
        if(shoppingCart.getUserId()==null){
            Long id = BaseContext.getCurrentId();
            shoppingCart.setUserId(id);
        }
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        //查询当前菜品或套餐是否已经在同一个用户条件下存在，存在则number+1
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if(dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(setmealId!=null,ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cart = this.getOne(queryWrapper);
        if(cart!=null){
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            this.updateById(cart);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            cart = shoppingCart;
        }
        return cart;
    }

    @Override
    public List<ShoppingCart> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        log.info("userId={}",BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = this.list(queryWrapper);
        return shoppingCarts;
    }

}
