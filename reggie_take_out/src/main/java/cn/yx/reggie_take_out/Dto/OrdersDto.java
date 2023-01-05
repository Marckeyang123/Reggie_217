package cn.yx.reggie_take_out.Dto;


import cn.yx.reggie_take_out.pojo.OrderDetail;
import cn.yx.reggie_take_out.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
