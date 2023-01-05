package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.ShoppingCart;
import cn.yx.reggie_take_out.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/13  23:48
* 
**/
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController{
    @Autowired
    private ShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart add = shoppingCartService.add(shoppingCart);
        if(add!=null){
            return R.success(add);
        }
        return R.error("未知错误！");
    }

    //购物车查看
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCarts = shoppingCartService.list();
        return R.success(shoppingCarts);
    }


}
