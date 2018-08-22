//控制层
app.controller('searchController', function ($scope,$location, searchService) {

    //前台传递搜索参数条件很多,因此使用对象参数做一个封装
    //分类
    //品牌
    //内存,尺寸,价格,排序,分页
    $scope.searchMap = {keywords: ""};

    //定义搜索方法
    //门户系统传递参数:search.html#?keywords=华为
    $scope.searchList = function () {

        //接收静态页面,或者ng-model绑定参数,都可以使用$location服务接收参数
        //接收参数语法:
        //$location.name
        //$location.search()['keywords']
        var keywords = $location.search()['keywords'];
        $scope.searchMap.keywords = keywords;
        //调用服务层方法,传递关键词进行搜索
        searchService.searchList($scope.searchMap).success(function (data) {
            $scope.resultMap = data;
        })
    }


});
