package com.bac.fm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;


public class MyFreemaker {

    /*
    freemarker入门案例
    获取基本数据:${} 花括号中填写的是map中的key,获取map中value的值.
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

    /*
     freemarker assign常量指令
     */
    @Test
    public void test02() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("assian.ftl");

        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File(
                "D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\assian.html"));

        //生成静态页面
        template.process(null, out);
        out.close();
    }

    /*
    freemarker if elseif else指令
    */
    @Test
    public void test03() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("ifelse.ftl");


        //创建map对象,存储ftl模板页面需要的数据
        Map map = new HashMap<>();
        map.put("flag", 1);
        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\ifelse.html"));

        //生成静态页面
        template.process(map, out);
        out.close();
    }

    /*
   freemarker  list指令 ,循环list集合数据
   */
    @Test
    public void test04() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("list.ftl");


        //创建map对象,存储ftl模板页面需要的数据
        Map map = new HashMap<>();

        //创建集合对象,封装数据
        List<Person> list = new ArrayList<>();
        Person p = new Person();
        p.setId(1);
        p.setName("张三");
        p.setAddress("北京");
        p.setAge(18);
        p.setSex("男");

        Person p1 = new Person();
        p1.setId(1);
        p1.setName("张三丰");
        p1.setAddress("北京");
        p1.setAge(18);
        p1.setSex("男");

        Person p3 = new Person();
        p3.setId(1);
        p3.setName("张无忌");
        p3.setAddress("北京");
        p3.setAge(18);
        p3.setSex("男");
        list.add(p);
        list.add(p1);
        list.add(p3);

        map.put("pList", list);


        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\list.html"));

        //生成静态页面
        template.process(map, out);
        out.close();
    }

    /*
    freemarker eval内建函数指令
    */
    @Test
    public void test05() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("eval.ftl");

        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File(
                "D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\eval.html"));

        //生成静态页面
        template.process(null, out);
        out.close();
    }

    /*
   freemarker 时间内建函数指令
   */
    @Test
    public void test06() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("date.ftl");


        //创建map对象,存储ftl模板页面需要的数据
        Map map = new HashMap<>();
        map.put("today", new Date());
        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\date.html"));

        //生成静态页面
        template.process(map, out);
        out.close();
    }

    /*
   freemarker  null处理
    */
    @Test
    public void test07() throws Exception {
        //创建freemarker核心对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板页面所在路径
        cf.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\com\\bac\\ftl"));
        //设置模板文件编码
        cf.setDefaultEncoding("utf-8");
        //读取模板文件,获取模板对象
        Template template = cf.getTemplate("null.ftl");


        //创建map对象,存储ftl模板页面需要的数据
        Map map = new HashMap<>();
        map.put("name", "唐僧");
        //创建输出流对象,把生成静态页面写入磁盘.
        Writer out = new FileWriter(new File("D:\\IdeaProjects\\bac\\bac_html_utils\\src\\main\\resources\\out\\null.html"));

        //生成静态页面
        template.process(map, out);
        out.close();
    }
}
