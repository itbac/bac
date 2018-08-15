package com.bac.text;

import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDFSTest {
    /*
    需求:测试图片上传
     */
    @Test
    public void uplaodPic() throws Exception {
        //1.指定图片地址,文件名.
        String path="D:\\aaa\\666.jpg";

        //2.指定图片服务器连接配置文件的地址.
        String client="D:\\IdeaProjects\\bac\\bac_shop_web\\src\\main\\resources\\config\\fdfs_client.conf";

        //3.加载客户端配置文件
        ClientGlobal.init(client);

        //4.创建tracker客户端对象
        TrackerClient trackerClient=new TrackerClient();
        //5.从客户端对象获取连接
        TrackerServer trackerServer = trackerClient.getConnection();

        //6.初始化storageServer
        StorageServer storageServer=null;

        //7.创建storage客户端对象
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);

        //8.使用存储的客户端对象上传图片.
        String[] urls = storageClient.upload_file(path, "jpg", null);

        //9.用图片服务器ip组装图片地址.
        String http= "http://192.168.66.67";
        for (String url : urls) {
            System.out.println(url);
            http += "/"+url;
        }
        System.out.println("浏览器访问图片的网址:"+http);
    }
}
