package com.bac.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Reference
    private SearchService searchService;

    /*
    接收查询参数,实现搜索
     */
    @RequestMapping("/searchList")
    public Map searchList(Map searchMap) {
        return searchService.searchList(searchMap);
    }
}
