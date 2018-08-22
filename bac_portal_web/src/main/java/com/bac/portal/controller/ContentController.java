package com.bac.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.content.service.ContentService;
import com.bac.pojo.TbContent;
import com.bac.utils.BacResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    //注入远程服务对象
    @Reference(timeout = 10000000)
    private ContentService contentService;

    /*
     需求:根据内容分类id查询广告内容
      */
    @RequestMapping("/findContentListByCategoryId/{categroyId}")
    public List<TbContent> findContentListByCategoryId(@PathVariable Long categroyId) {
        //调用远程服务
        return contentService.findContentByCategoryId(categroyId);
    }
}
