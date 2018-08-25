package com.bac.fm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class MyFreemaker {

    /*
    freemarker入门案例
    获取基本数据:${}
     */
    @Test
    public void test01() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("hello.ftl");


        //创建map对象,存储ftl模板页面需要的数据
        Map map = new HashMap<>();
        map.put("hello", "欢迎来到freemarker世界,呵呵");
        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\first.html"));

        //生成静态页面
        template.process(map, out);

        out.close();


    }
}
