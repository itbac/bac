//控制层
app.controller('sellerController', function ($scope, $controller, sellerService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        sellerService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (pageNum, pageSize) {
        sellerService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        sellerService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = sellerService.update($scope.entity); //修改
        } else {
            serviceObject = sellerService.add($scope.entity);//增加
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
        sellerService.dele($scope.selectIds).success(
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
        sellerService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };


    /*
     运营商管理 更改商家状态: 默认未审核0,通过1,未通过2,关闭商家3
      @param  sellerId
      @param  status
  */

    $scope.updateStatus = function (sellerId, status) {
        sellerService.updateStatus(sellerId, status).success(function (response) {
            if (response.success) {
                //刷新列表
                $scope.reloadList();
            } else {
                alert(response.message);

            }
        })
    }

});	
