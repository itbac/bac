//控制层
app.controller('contentController', function ($scope, $controller, contentService, uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        contentService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (pageNum, pageSize) {
        contentService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        contentService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = contentService.update($scope.entity); //修改
        } else {
            serviceObject = contentService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        contentService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (pageNum, pageSize) {
        contentService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    // 定义图片上传的方法
    $scope.uploadFile = function () {
        //调用servicet图片上传的方法
        uploadService.uploadFile().success(function (data) {
            //判断
            if (data.success) {
                //新建按钮初始化  ng-click="entity={}"
                $scope.entity.pic = data.message;
            } else {
                alert(data.message);
            }

        })
    }

});	
