package cn.yx.reggie_take_out.contorller;

import cn.yx.reggie_take_out.common.BaseContext;
import cn.yx.reggie_take_out.common.R;
import cn.yx.reggie_take_out.pojo.Employee;
import cn.yx.reggie_take_out.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/*
@Auther：Y.
*@Date：2022/12/3  19:34
*http://localhost:8091/backend/page/login/login.html
**/
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //登录
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /*将前台获取的密码进行md5加密
        * */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /*用户名为unique字段，通过它查找信息
        * */
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp==null || !emp.getPassword().equals(password)){
            return R.error("登录失败！");
        }
        if(emp.getStatus()==0){
            return R.error("账号已禁用！");
        }

        //把id放到session,作用？？？？用于让拦截器获取，登录是否成功。
        request.getSession().setAttribute("employee",emp.getId());
        Long o = (Long) request.getSession().getAttribute("employee");
        System.out.println("id="+o);
        return R.success(emp);
    }

    /*展示
    * 前端需要records和total在Page类中
    * */
    @GetMapping("/page")
    public R<Page> select(Integer page, Integer pageSize, String name){
        log.info("page={},pagesize,{}"+page,pageSize);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //按照更新时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    //根据员工id修改员工statu
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("修改成功！");
    }

    //更新员工信息
    @GetMapping("/{id}")
    public R<Employee> update(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return null;
    }

    //员工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    //新增
    @PostMapping
    public R<String> insert (HttpServletRequest request,@RequestBody Employee employee){
        //设置初始密码，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        log.info("BaseContext.getCurrentId={}",BaseContext.getCurrentId());
        employeeService.save(employee);
        return R.success("新增成功！");
    }

}
