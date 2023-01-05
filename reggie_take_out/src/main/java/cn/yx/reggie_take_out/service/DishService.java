package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.Dto.DishDto;
import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/5  20:37
* 
**/
public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public Page savePage(Integer page, Integer pageSize,String name);

    //根据id查询菜品信息以及口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateDishAndFlavor(DishDto dishDto);

    public void delete(List<Long> ids);

    public String stopOrBegin(Integer status,List<Long> ids);

    public  List<DishDto> get(DishDto dishDto);

}
