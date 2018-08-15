//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (pageNum, pageSize) {
        itemCatService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {

            //***上级节点id复制添加节点父id ,
            $scope.entity.parentId = $scope.parentId;

            //保存
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {

                    //重新查询
                    $scope.findItemCatListByParentId($scope.parentId)

                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                   // $scope.reloadList();//刷新列表
                    //重新查询
                    $scope.findItemCatListByParentId($scope.parentId);

                    $scope.selectIds = [];
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (pageNum, pageSize) {
        itemCatService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //定义方法,查询商品分类列表
    $scope.findItemCatListByParentId = function (parentId) {
        itemCatService.findItemCatByParentId(parentId).success(function (date) {
            $scope.catList = date;
        })
    };
    //面包屑导航js
    //1.定义级别方法,判断目前3级菜单,到底在哪一级.
    //2.定义查询方法,此方法记录每一个级别对象
    //初始化级别
    $scope.grade = 1;
    //定义方法做级别加法,点击下一级,就进行加1操作
    $scope.setGrade = function (value) {
        $scope.grade = value;
    };
    //定义方法查询每一个级别对象.
    $scope.selectList = function (entity) {

        //查询时候,记录此节点的父节点id,用于添加操作.
        $scope.parentId = entity.id;

        //判断级别,目录菜单在哪一级
        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        } else if ($scope.grade == 2) {
            $scope.entity_1 = entity;
            $scope.entity_2 = null;
        }
        else if ($scope.grade == 3) {
            $scope.entity_2 = entity;
        }
        //根据父id查子节点
        $scope.findItemCatListByParentId(entity.id);

    }


});	
