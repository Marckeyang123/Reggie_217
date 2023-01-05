package cn.yx.reggie_take_out.Dto;


import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    //套餐分类
    private String categoryName;
}
