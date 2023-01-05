package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.Dto.SetmealDto;
import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.mapper.SetmealMapper;
import cn.yx.reggie_take_out.pojo.Category;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import cn.yx.reggie_take_out.service.CategoryService;
import cn.yx.reggie_take_out.service.SetmealDishService;
import cn.yx.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

/*
@Auther：Y.
*@Date：2022/12/5  20:40
* 
**/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    public SetmealDishService setmealDishService;
    @Override
    public Page save(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Long categoryId = setmeal.getCategoryId();
            if(categoryId!=null){
                Category category = categoryService.getById(categoryId);
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage = setmealDtoPage.setRecords(setmealDtoList);
        return setmealDtoPage;
    }

    //回显数据
    public SetmealDto feedBack(Long id){
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        //设置套餐基本信息
        BeanUtils.copyProperties(setmeal,setmealDto);
        Long categoryId = setmeal.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName();
        //获得categoryName
        setmealDto.setCategoryName(categoryName);

        //获得菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id!=null,SetmealDish::getSetmealId,id)
                        .orderByAsc(SetmealDish::getPrice);
        setmealDto.setSetmealDishes(setmealDishService.list(queryWrapper));
        return setmealDto;
    }

    //对套餐相关信息进行修改
    @Transactional
    @Override
    public void update(SetmealDto setmealDto) {
        /*
        更新setmeal表
        */
        this.updateById(setmealDto);
       /* 更新setmeal_dish表*/
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map(setmealDish -> {
            SetmealDish setmealDish1 = new SetmealDish();
            BeanUtils.copyProperties(setmealDish,setmealDish1);
            setmealDish1.setSetmealId(setmealDto.getId());
            return setmealDish1;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    @Transactional
    public List<Setmeal> show(Setmeal setmeal) {
        Long categoryId = setmeal.getCategoryId();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        List<Setmeal> setmealList = this.list(queryWrapper);
        return setmealList;
    }

    @Override
    public List<SetmealDish> setmealShow(Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        Setmeal setmeal = this.getById(id);
        queryWrapper.eq(id!=null,SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        return setmealDishes;
    }

    @Override
    public String beginOrStop(Integer status, List<Long> ids) {
        String message;
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids!=null,Setmeal::getId,ids);
        if(status==0){
            updateWrapper.set(Setmeal::getStatus,0);
            this.update(updateWrapper);
            message = "起售成功！";
        }else {
            updateWrapper.set(Setmeal::getStatus,1);
            this.update(updateWrapper);
            message = "停售成功！";
        }
        return message;
    }
}
