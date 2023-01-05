package cn.yx.reggie_take_out.Dto;

import cn.yx.reggie_take_out.pojo.Dish;
import cn.yx.reggie_take_out.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/11  22:20
* 
**/
@Data
public class DishDto extends Dish {

   private List<DishFlavor> flavors =new ArrayList<>();

   private String categoryName;

   private Integer copies;
}
