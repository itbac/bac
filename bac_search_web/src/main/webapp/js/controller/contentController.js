//控制层
app.controller('contentController', function ($scope, contentService) {



    /*
    需求:根据内容分类id查询广告内容
     */
       $scope.ContentList=[];
    //定义数组存储广告信息

    //读取列表数据绑定到表单中.
    $scope.findContentListByCategoryId = function (categroyId) {
        contentService.findContentListByCategoryId(categroyId).success(
            function (response) {
                //数组的角标存储对应的内容.
                $scope.ContentList[categroyId]=response;

            }
        );
    }


});
