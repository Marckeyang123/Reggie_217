package cn.yx.reggie_take_out.service;

import cn.yx.reggie_take_out.Dto.SetmealDto;
import cn.yx.reggie_take_out.pojo.Setmeal;
import cn.yx.reggie_take_out.pojo.SetmealDish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/*
@Auther：Y.
*@Date：2022/12/5  20:37
* 
**/
public interface SetmealService extends IService<Setmeal> {

    public Page save(Integer page,Integer pageSize,String name);

    public String beginOrStop(Integer status, List<Long> ids);

    public SetmealDto feedBack(Long id);

    public void update(SetmealDto setmealDto);

    public List<Setmeal> show(Setmeal setmeal);

    public List<SetmealDish> setmealShow(Long id);

}
