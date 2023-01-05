package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Category;
import cn.yx.reggie_take_out.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/5  19:00
* 
**/
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分页查询
    @GetMapping("/page")
    public R<Page> save(Integer page,Integer pageSize){
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    //新增分类
    @PostMapping
    public R<String> add(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功！");
    }

    //修改
    @PutMapping
    public R<String> change(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功！");
    }

    //删除
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功！");
    }

    //回显口味数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
