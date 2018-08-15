//控制层
app.controller('typeTemplateController', function ($scope,
                                                   $controller,
                                                   typeTemplateService,
                                                   brandService,
                                                   specificationService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };
    //分页
    $scope.findPage = function (pageNum, pageSize) {
        typeTemplateService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {

                $scope.entity = response;

                //把品牌json字符串转换成json对象.
                $scope.entity.brandIds= json.parse( $scope.entity.brandIds);

                //把规格json字符串转换成json对象.
                $scope.entity.spec= json.parse( $scope.entity.specIds);
                //把扩展属性 json字符串转换成json对象.
                $scope.entity.customAttributeItems= json.parse( $scope.entity.customAttributeItems)


            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
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
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        typeTemplateService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (pageNum, pageSize) {
        typeTemplateService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //品牌列表,模拟select2插件的属性config需要的数据是一个json对象,
    // 该对象的key是data,   valus是json数组,这是插件定义的一个接口,固定格式.
    // 后端查询返回数据格式:List<Map> = [{id=1, text=联想}, {id=2, text=华为}]
    //后端转换成json数组格式传到前端       [{id:'1',text:'联想'},{id:'2',text:'华为'}]
    // $scope.brandList = {data: [{id: 1, text: '联想'}, {id: 2, text: '华为'}, {id: 3, text: '小米'}]};

    //定义方法,查询品牌下拉列表
    $scope.findBrandList = function () {
        //调用service服务方法,实现品牌列表查询
        brandService.findBrandList().success(function (data) {
            $scope.brandList = {data: data};
        })
    };

    /*
    需求:查询规格值,进行下拉列表展示
     */
    $scope.findSpecList = function () {
        //调用service服务方法,实现品牌列表查询
        specificationService.findSpecList().success(function (data) {
            $scope.specList = {data: data};
        })
    };

    /*
    定义动态的添加扩展属性行的操作.
    entity.customAttributeItems
     */
    $scope.addTableRow = function () {
        //添加空对象.页面的新建按钮对属性进行初始
        //ng-click="entity={customAttributeItems:[]}"
        $scope.entity.customAttributeItems.push({});
    };

    //删除扩展行属性.$index 用于获取ng-repeat指令循环中的索引。 删除1个元素.
    $scope.delTableRow = function ($index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }
});	
