package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.Dto.DishDto;
import cn.yx.reggie_take_out.Dto.SetmealDto;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/13  17:16
* 
**/
public interface SetmealDishService extends IService<SetmealDish> {

    public void add(SetmealDto setmealDto);

    public  void delete(List list);

}
