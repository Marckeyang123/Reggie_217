package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.pojo.OrderDetail;
import cn.yx.reggie_take_out.pojo.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/*
@Auther：Y.
*@Date：2022/12/13  23:54
* 
**/
public interface OrderService extends IService<Orders> {

    public boolean submit(Orders orders);

    public Page showOrder(Integer page,Integer pageSize,String number);

    public boolean update(Orders orders);

}
