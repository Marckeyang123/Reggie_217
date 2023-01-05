package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.Dto.DishDto;
import cn.yx.reggie_take_out.common.CustomException;
import cn.yx.reggie_take_out.mapper.DisMapper;
import cn.yx.reggie_take_out.pojo.Category;
import cn.yx.reggie_take_out.pojo.Dish;
import cn.yx.reggie_take_out.pojo.DishFlavor;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import cn.yx.reggie_take_out.service.CategoryService;
import cn.yx.reggie_take_out.service.DishFlavorService;
import cn.yx.reggie_take_out.service.DishService;
import cn.yx.reggie_take_out.service.SetmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
@Auther：Y.
*@Date：2022/12/5  20:39
* 
**/
@Service
public class DishServiceImpl extends ServiceImpl<DisMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /*新增菜品，同时保存对应的口味数据*/
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();//菜品的id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page savePage(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map((Function<Dish, DishDto>) dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;
    }

    //根据id查询菜品信息以及口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        //查询菜品信息
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateDishAndFlavor(DishDto dishDto) {
        //更新dish表，因为dishDto是dish的子类，所以可以直接用dishDto
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> flavorList = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper= new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(ids!=null,Dish::getId,ids);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        int dishCount = this.count(dishLambdaQueryWrapper);
        if(dishCount>0){
           throw  new CustomException("包含正在售卖的菜品，不能删除！");
        }
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        int setmealDishCount = setmealDishService.count(setmealDishLambdaQueryWrapper);
        if (setmealDishCount>0){
            throw new CustomException("包含在其他套餐内的菜品，不能删除！");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }

    @Override
    public String stopOrBegin(Integer status,List<Long> ids) {
        String message;
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids!=null,Dish::getId,ids);
        //批量起售
        if(status==1){
            updateWrapper.set(Dish::getStatus,1);
            this.update(updateWrapper);
            message="起售成功！";

        }else {
            //批量停售
            updateWrapper.set(Dish::getStatus,0);
            this.update(updateWrapper);
            message="停售成功！";
        }
        return message;
    }

    //回显套餐的菜品数据
    @Override
    @Transactional
    public List<DishDto> get(DishDto dishDto) {
        Long categoryId = dishDto.getCategoryId();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(categoryId!=null,Dish::getCategoryId,categoryId);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        //Dish dish = this.getOne(dishLambdaQueryWrapper);
        List<Dish> dishList = this.list(dishLambdaQueryWrapper);
        List<DishDto> dishDtos = dishList.stream().map(item -> {
            DishDto itemDishDto = new DishDto();
            BeanUtils.copyProperties(item, itemDishDto);
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(item.getId() != null, DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            itemDishDto.setFlavors(dishFlavors);
            return itemDishDto;
        }).collect(Collectors.toList());
        return dishDtos;
    }
}
