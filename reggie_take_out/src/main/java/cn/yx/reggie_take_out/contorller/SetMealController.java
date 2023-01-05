package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.Dto.SetmealDto;
import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import cn.yx.reggie_take_out.service.SetmealDishService;
import cn.yx.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

/*
@Auther：Y.
*@Date：2022/12/12  22:22
* 
**/
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page> save(Integer page,Integer pageSize,String name){
        Page pageInfo = setmealService.save(page, pageSize, name);
        return R.success(pageInfo);
    }

    //添加套餐
    @PostMapping
    public  R<String> add(@RequestBody SetmealDto setmealDto){
        setmealDishService.add(setmealDto);
        return R.success("添加套餐成功！");
    }

    //回显数据（用于后台修改数据）
    @GetMapping("/{id}")
    public R<SetmealDto> feedBack(@PathVariable("id") Long id){
        SetmealDto setmealDto = setmealService.feedBack(id);
        return R.success(setmealDto);
    }
    //显示套餐
    @GetMapping("/list")
    public R<List<Setmeal>> show(Setmeal setmeal){
        return R.success(setmealService.show(setmeal));
    }

    //显示套餐菜品
    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> setmealShow(@PathVariable("id") Long id){
        List<SetmealDish> setmealDishes = setmealService.setmealShow(id);
        return R.success(setmealDishes);
    }

    //修改数据
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.update(setmealDto);
        return R.success("修改成功！");
    }

    //删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealDishService.delete(ids);
        return R.success("删除成功！");
    }

    //起售或停售
    @PostMapping("/status/{status}")
    public R<String> beginOrStop(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        return R.success(setmealService.beginOrStop(status,ids));
    }
}
