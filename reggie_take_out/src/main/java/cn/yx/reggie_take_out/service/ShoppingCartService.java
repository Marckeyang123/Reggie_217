package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/13  23:43
* 
**/
public interface ShoppingCartService extends IService<ShoppingCart> {

    public ShoppingCart add(ShoppingCart shoppingCart);

    public List<ShoppingCart> list();
}
