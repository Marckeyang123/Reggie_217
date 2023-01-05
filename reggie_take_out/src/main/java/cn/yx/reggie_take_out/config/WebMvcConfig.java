package cn.yx.reggie_take_out.config;

import cn.yx.reggie_take_out.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import java.util.List;


/*
@Auther：Y.
*@Date：2022/12/3  19:24
* 
**/
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /*设置静态资源文件
    * */

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("访问成功.....");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //扩展mvc框架的消息转化器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建新的消息转换器
        MappingJackson2HttpMessageConverter m = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转化为json
        m.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中去
        converters.add(0,m);
    }
}
