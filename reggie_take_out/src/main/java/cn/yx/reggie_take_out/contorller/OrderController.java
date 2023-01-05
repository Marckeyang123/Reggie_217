package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Orders;
import cn.yx.reggie_take_out.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/*
@Auther：Y.
*@Date：2022/12/13  23:52
* 
**/
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        if(orderService.submit(orders)){
            return R.success("购买成功！");
        }
        return R.error("订单异常！");
    }

    @GetMapping("/page")
    public R<Page> showOrders(Integer page, Integer pageSize, String number,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime){
        log.info("beginTime={}",beginTime);
        Page showOrder = orderService.showOrder(page, pageSize, number);
        return R.success(showOrder);
    }

    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        if(orderService.update(orders)){
            return R.success("更改成功！");
        }
        return R.error("更改失败！");
    }


}
