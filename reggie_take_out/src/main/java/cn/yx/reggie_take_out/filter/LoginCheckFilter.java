package cn.yx.reggie_take_out.filter;

import cn.yx.reggie_take_out.common.BaseContext;
import cn.yx.reggie_take_out.common.R;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
@Auther：Y.
*@Date：2022/12/3  21:42
* 
**/
//检查用户是否完成登录
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取url
        String requestURI = request.getRequestURI();//    /backend/index.html
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //不需要拦截，放行
        boolean check = check(urls,requestURI);
        if(check){
            filterChain.doFilter(request,response);
            log.info("请求通过，url为：{}",request.getRequestURI());
            return;
        }

        //通过判断session中是否存入的employee（id）来判断是否登录
        if(request.getSession().getAttribute("employee")!=null){
            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request,response);
            return;
        }

        //移动端判断是否登录
        if(request.getSession().getAttribute("userId")!=null){
            Long userId = (Long) request.getSession().getAttribute("userId");
            log.info("userId={}",userId);
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录！");
        //如果未登录，则需要通过输出流的方式把“NOLOGIN”写回前端
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    //判断url是否拦截
    public boolean check(String [] urls,String requestURI){

        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) return true;
        }
        return false;
    }
}
