package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.common.BaseContext;
import cn.yx.reggie_take_out.common.CustomException;
import cn.yx.reggie_take_out.mapper.OrderMapper;
import cn.yx.reggie_take_out.pojo.*;
import cn.yx.reggie_take_out.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
@Auther：Y.
*@Date：2022/12/13  23:54
* 
**/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public boolean submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        //查询用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        if(shoppingCartList==null||shoppingCartList.size()==0){
            throw new CustomException("购物车为空！下单失败！");
        }

        //查询用户数据
        User user = userService.getById(userId);
        //查询地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook==null){
            throw new CustomException("地址信息为空！下单失败！");
        }

        long orderId = IdWorker.getId();//订单号
        AtomicInteger amount = new AtomicInteger();//原子操作，保证线程安全
        List<OrderDetail> orderDetails = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(queryWrapper);
        return true;
    }

    @Override
    public Page showOrder(Integer page, Integer pageSize, String number) {
        Page<Orders> infoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number!=null,Orders::getNumber,number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        Page<Orders> OrdersPage = this.page(infoPage, queryWrapper);
        return OrdersPage;
    }

    @Override
    public boolean update(Orders orders) {
        LambdaUpdateWrapper<Orders>  updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(orders!=null,Orders::getId,orders.getId());
        updateWrapper.set(Orders::getStatus,Integer.valueOf(orders.getStatus()));
        this.update(updateWrapper);
        return true;
    }
}
