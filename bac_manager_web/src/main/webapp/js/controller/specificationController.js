//在模块下定义控制器
//在controller中用specificationService,依赖注入
app.controller("specificationController", function ($scope, $controller, specificationService) {

    //控制器继承
    //继承baseController控制器, 且把父控制器$scope传递给子控制器.
    $controller("baseController", {$scope: $scope});

    //定义函数
    //读取列表数据绑定到表单
    //所有的函数都绑定到$scope域中
    $scope.findAll = function () {
        //使用$http内置服务调用后台restfull请求.
        specificationService.findAll.success(function (data) {
            $scope.list = data;
        })
    };


    //定义分页查询方法
    $scope.findPage = function (pageNum, pageSize) {

        //使用内置服务,发送分页查询请求.    当前页+每页大小.
        specificationService.findPage(pageNum, pageSize).success(function (data) {

            $scope.paginationConf.totalItems = data.total;  //总记录数.
            $scope.list = data.rows;      //当前页结果list集合.
        })

    };

    //保存
    $scope.save = function () {
        //定义服务层对象用来接收  添加,或者修改 方法 对象.
        var objservice = null;

        //调用修改方法.
        if ($scope.entity.specification.id != null) {
            objservice = specificationService.update($scope.entity);
        }else {
            //调用添加方法.
            objservice = specificationService.add($scope.entity);
        }


        objservice.success(function (response) {
            if (response.success) {
                //重新查询,重写加载
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    };
    //根据id查品牌
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    };


    //定义删除方法.
    $scope.dele = function () {
        //获取选中的复选框
        specificationService.dele($scope.selectIds).success(function (data) {
            if (data.success) {
                //清空删除ids
                $scope.selectIds = [];
                $scope.reloadList(); //删除成功,刷新列表.
            } else {
                alert(data.message); //删除失败.
            }
        })

    };
    /*
    定义添加行方法
    specificationOptionList=[{},{},...]
     */
    $scope.addTableRow = function () {
        //添加一行
        $scope.entity.specificationOptionList.push({});
    };

    //定义删除集合中行.
    $scope.delTableRow=function (index) {
        //删除
        $scope.entity.specificationOptionList.splice(index,1);
    }




});













