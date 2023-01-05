package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.common.CustomException;
import cn.yx.reggie_take_out.mapper.CategoryMapper;
import cn.yx.reggie_take_out.pojo.Category;
import cn.yx.reggie_take_out.pojo.Dish;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.service.CategoryService;
import cn.yx.reggie_take_out.service.DishService;
import cn.yx.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
@Auther：Y.
*@Date：2022/12/5  18:58
* 
**/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;


    @Override
    @Transactional
    public void remove(Long id) {
        //查询当前分类是否关联了菜品或套餐
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        if(dishService.count(dishLambdaQueryWrapper)>0||setmealService.count(setmealLambdaQueryWrapper)>0){
            throw new CustomException("当前分类关联了菜品或套餐，不能删除！");
        }
        super.removeById(id);
    }

}
