package cn.yx.reggie_take_out.contorller;

/*
@Auther：Y.
*@Date：2022/12/5  21:57
* 
**/

import cn.yx.reggie_take_out.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class FileUpLoadAndDownLoad {

    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀名(.jpg)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        File path = new File(basePath);
        if(!path.exists()){
            //若不存在目录，就创建
            path.mkdir();
        }
        file.transferTo(new File(basePath+fileName));

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(basePath+name);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");
        byte[] bytes= new byte[1024];
        int len= 0;
        while ((len = fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        fileInputStream.close();
        outputStream.close();
    }

}
