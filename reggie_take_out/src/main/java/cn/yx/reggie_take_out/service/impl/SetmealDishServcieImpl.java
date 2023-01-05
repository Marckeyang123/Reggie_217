package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.Dto.SetmealDto;
import cn.yx.reggie_take_out.common.CustomException;
import cn.yx.reggie_take_out.mapper.SetmealDishMapper;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import cn.yx.reggie_take_out.service.SetmealDishService;
import cn.yx.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
@Auther：Y.
*@Date：2022/12/13  17:17
* 
**/
@Service
public class SetmealDishServcieImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService{

    @Autowired
    private SetmealService setmealService;

    @Transactional
    @Override
    public void add(SetmealDto setmealDto) {
        setmealService.save(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(id);
            return setmealDish;
        }).collect(Collectors.toList());
        this.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void delete(List list) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper= new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(list!=null,Setmeal::getId,list);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        if(setmealService.count(setmealLambdaQueryWrapper)>0){
            throw new CustomException("包含正在售卖中的套餐，不能删除！");
        }
        setmealService.removeByIds(list);
        setmealDishLambdaQueryWrapper.in(list!=null,SetmealDish::getSetmealId,list);
        this.remove(setmealDishLambdaQueryWrapper);
    }
}
