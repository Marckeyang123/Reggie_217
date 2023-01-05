package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.Dto.DishDto;
import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Dish;
import cn.yx.reggie_take_out.service.CategoryService;
import cn.yx.reggie_take_out.service.DishFlavorService;
import cn.yx.reggie_take_out.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
@Auther：Y.
*@Date：2022/12/10  23:42
* 
**/
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //新增菜品
    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    //菜品展示
    @GetMapping("/page")
    public R<Page> save(Integer page, Integer pageSize,String name){
        Page pageInfo = dishService.savePage(page, pageSize, name);
        return R.success(pageInfo);
    }


    //根据菜品id查询菜品信息和口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    //Dish和flavor的更新
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishAndFlavor(dishDto);
        return R.success("更新成功！");
    }

    //菜品回显
    @GetMapping("/list")
    public R<List<DishDto>> get(DishDto dishDto){
        List<DishDto> dishDtoList = dishService.get(dishDto);
        return R.success(dishDtoList);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.delete(ids);
        return R.success("删除成功！");
    }

    //批量起售和停售
    @PostMapping("/status/{status}")
    public R<String> stopOrBegin(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        return R.success(dishService.stopOrBegin(status,ids));
    }

}
