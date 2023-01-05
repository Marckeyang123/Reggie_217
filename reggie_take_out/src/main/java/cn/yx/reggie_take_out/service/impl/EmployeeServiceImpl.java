package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.mapper.EmployeeMapper;
import cn.yx.reggie_take_out.pojo.Employee;
import cn.yx.reggie_take_out.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/*
@Auther：Y.
*@Date：2022/12/3  19:43
* 
**/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
