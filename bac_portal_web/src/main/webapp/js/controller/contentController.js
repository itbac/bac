//控制层
app.controller('contentController', function ($scope, contentService) {



    /*
    需求:根据内容分类id查询广告内容
     */
    $scope.ContentList = [];
    //定义数组存储广告信息

    //读取列表数据绑定到表单中.
    $scope.findContentListByCategoryId = function (categroyId) {
        contentService.findContentListByCategoryId(categroyId).success(
            function (response) {
                //数组的角标存储对应的内容.
                $scope.ContentList[categroyId] = response;

            }
        );
    }

    /*
    定义搜索方法
     */
    $scope.solrSearch = function () {
        //发送请求给 搜索系统, 从门户系统去到搜索系统.
        //angualrjs参数路由:静态页面如何传递参数?
        //angualrjs参数路由:#?在静态页面后面追加参数.

        window.location.href = "http://localhost:8083/search.html#?keywords=" + $scope.keywords;

    }


});
