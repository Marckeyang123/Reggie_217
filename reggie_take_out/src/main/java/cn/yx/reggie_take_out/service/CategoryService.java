package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/*
@Auther：Y.
*@Date：2022/12/5  18:58
* 
**/
public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
