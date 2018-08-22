package com.bac.manager.controller;

import com.bac.utils.BacResult;
import com.bac.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    /*
    需求:文件上传操作
     */

    //扫描application.properties文件,注入图片服务器地址.
    @Value("${fast_dfs_url}")
    private String fast_dfs_url;


    @RequestMapping("/pic")
    public BacResult uploadPic(MultipartFile file) {


        //创建文件上传工具类对象
        try {
            //获取文件扩展名
            //获取文件名字
            String originalFilename = file.getOriginalFilename();
            //1.截取文件扩展名  jpg ,截取点后面的字符串.
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //2.创建FastDFSClient 客户端对象,参数传入配置文件.
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/fdfs_client.conf");
            //3.执行上传,参数1:文件的字节数组, 参数二.文件后缀名.
            String url = fastDFSClient.uploadFile(file.getBytes(), substring);

            //4.拼接application.properties配置的ip地址,与返回的url地址,拼装成完整的地址.
            url = fast_dfs_url + url;

            //上传成功返回地址.
            return new BacResult(true, url);

        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "上传失败");

        }
    }
}
