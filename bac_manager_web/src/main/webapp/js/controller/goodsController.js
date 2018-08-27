//控制层
app.controller('goodsController', function ($scope,
                                            $controller,
                                            goodsService,
                                            itemCatService,
                                            typeTemplateService,
                                            uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //定义查询商品分类方法.
    //读取一级分类
    //默认传递参数0,初始化商品分类顶级菜单.
    $scope.findCat1List = function () {
        itemCatService.findItemCatListByParentId(0).success(function (data) {
            $scope.cat1List = data;
        })
    };

    //加载商品分类二级节点,二级节点必须根据选中的顶级节点来进行加载
    //使用angularJS监听服务$watch服务,监听上级节点变化,一旦上级节点有变化,里面根据节点id查询下级子节点,实现联动效果.

    $scope.$watch('entity.goods.category1_id', function (newValue, oldValue) {
        //调用商品分类service服务方法,查询分类节点.
        itemCatService.findItemCatListByParentId(newValue).success(function (data) {
            $scope.cat2List = data;
        })

    });

    //监听二级节点,查询三级节点,实现三级联动效果
    $scope.$watch('entity.goods.category2_id', function (newValue, oldValue) {
        //调用商品分类service服务方法,查询分类节点.
        itemCatService.findItemCatListByParentId(newValue).success(function (data) {
            $scope.cat3List = data;
        })

    });

    //监听三级节点分类节点id,查询获取分类模板id.
    $scope.$watch('entity.goods.category3_id', function (newValue, oldValue) {
        //调用商品分类服务findOne方法,根据id查询分类对象
        itemCatService.findOne(newValue).success(function (data) {
            $scope.entity.goods.typeTemplateId = data.typeId;

        })

    });


    //定义页面实体结构,也就是包装类Googs.java,
    //entity整体是一个json对象. goods:{}是json对象,itemList:[] 是json数组,
    $scope.entity = {
        goods: {},
        goodsDesc: {itemImages: [], customAttributeItems: [], specificationItems: []},
        itemList: []

    };

    //监控分类模板id变化,一旦模板di发送变化,根据模板id查询出模板对象.
    //获取当前分类模板中的  品牌数据  ,此时模板中存储了当前分类商品的所有品牌
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        //查询模板对象
        typeTemplateService.findOne(newValue).success(function (data) {
            //先给模板对象赋值,进行初始化
            $scope.typeTemplate = data;
            //把字符串json转换成json对象,获取品牌数据.
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

            //  获取模板 扩展属性, 赋值给商品描述的  扩展属性
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
        })


        //查询规格及规格选项.显示规格列表
        typeTemplateService.findSpecList(newValue).success(
            function (data) {
                $scope.specList = data;
            });
    });


    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (pageNum, pageSize) {
        goodsService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {

        //获取富文本编辑器中,文本数据,封装给 包装类下的表的一个字段.
        $scope.entity.goodsDesc.introduction = editor.html();

        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {

                    //点击保存成功后.清空表单数据.
                    $scope.entity = {};
                    //清空editor 富文本框.
                    editor.html("");

                } else {
                    alert(response.message);
                }
            }
        );
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
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
        goodsService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;	 //分页包装类的返回值list结果集
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (data) {
            //判断,如果上传成功,就把地址存到url.
            //在新建按钮初始化 ng-click="image_entity={}
            if (data.success) {
                $scope.image_entity.url = data.message;
            } else {
                alert(data.message);
            }
        }).error(function () {
            alert("上传发送错误");
        });
    };


    //第一阶段,组装参数  , 添加图片对象.
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };

    //定义方法,判断选中的是哪个规格中的属性值.
    //参数1: entity.goodsDesc.specificationItems =[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},{},{}]
    //参数2:attributeName
    //参数3:网络 ,或 机身内存
    $scope.searchSpecOption = function (list, key, name) {
        //循环list集合
        for (var i = 0; i < list.length; i++) {
            //判断选中是哪个规格中的选项
            if (list[i][key] == name) {
                return list[i];
            }
        }
        return null;
    };

    // 定义方法,组装规格选项参数
    //把规格选项参数保存商品描述表specificationitems
    //参数1:$event 事件,用于判断是选中事件,还是取消事件
    //参数2:规格名称:网络,内存
    //参数3:规格选项 16G,32G
    //entity={goodsDesc:{specificationItems:[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["6寸","5寸"]}]}}
    $scope.updateSpeOption = function ($event, name, value) {

        //获取商品描述规格参数
        var specList = $scope.entity.goodsDesc.specificationItems;

        //判断添加规格选项是哪个规格中的选项.
        var obj = $scope.searchSpecOption(specList, 'attributeName', name);

        //判断是否为空
        if (obj != null) {
            //判断是否选中事件
            if ($event.target.checked) {
                obj.attributeValue.push(value);
            } else {
                //取消事件
                obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
                //判断如果规格选项已经删除完了,需要把整个规格对象也删除.
                if (obj.attributeValue.length == 0) {
                    specList.splice(specList.indexOf(obj), 1);
                }
            }
        } else {
            //第一次选择时,$scope.entity.goodsDesc.specificationItems; 是空数组,需要对其进行初始化.
            $scope.entity.goodsDesc.specificationItems.push(
                {
                    "attributeName": name,
                    "attributeValue": [value]
                });
            //第一次点击后:
            //$scope.entity.goodsDesc.specificationItems=[{"attributeName":"网络制式","attributeValue":["移动3G"]}]
        }
    };

    // 定义方法,根据选中规格选项,动态生成sku行
    $scope.createIetmList = function () {
        //初始化sku行
        $scope.entity.itemList = [{spec: {}, price: 0, num: 999999, status: '1', isDefaule: '0'}];

        //获取选中规格选项数据
        var speciList = $scope.entity.goodsDesc.specificationItems;

        //判断规格选项是否为空,删除sku
        if (speciList.length == 0) {
            $scope.entity.itemList = [];
        }


        //循环选中规格选项,根据规格选项生成sku行.
        for (var i = 0; i < speciList.length; i++) {
            //添加行的操作
            $scope.entity.itemList = addColumn($scope.entity.itemList, speciList[i].attributeName, speciList[i].attributeValue);
        }
    };

    // entity={goodsDesc:{specificationItems:[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["6寸","5寸"]}]}}
    //参数1:$scope.itemList = [{spec: {}, price: 0, num: 999999, status: '1', isDefaule: '0'}];
    //参数2:网络制式
    //参数3:["移动3G","移动4G"]
    addColumn = function (itemList, name, values) {
        //定义一个新的数组,封装新组装的结果
        var newList = [];
        for (var i = 0; i < itemList.length; i++) {
            //获取旧的行
            var oleRow = itemList[i];

            //根据选中规格选项组装行
            //循环规格选项
            for (var j = 0; j < values.length; j++) {
                //深克隆操作,新创建一行
                var newRow = JSON.parse(JSON.stringify(oleRow));
                newRow.spec[name] = values[j];

                //第一次循环结果:[{spec: {'网络':电信2G}}]
                //第二次循环结果:[{spec: {'网络':电信2G}},{spec: {'网络':联通2G}}]

                //把新行添加到newList
                newList.push(newRow);
            }
        }
        return newList;
    };


    //列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    };

    //商品状态 {{  status[ entity.auditStatus]}}
    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];

    //定义数组存储分类名称
    $scope.itemCatList = [];
    //查询分类,把所有的分类都封装数组中进行存储
    $scope.findItemCatList = function () {
        //调用分类服务,查询所有名称
        itemCatService.findAll().success(function (data) {
            //遍历分类数据的集合
            for (var i = 0; i < data.length; i++
            ) {
                //把分类id 作为数组角标,在此角标存储分类名称
                $scope.itemCatList[data[i].id] = data[i].name;
            }

        })

    };
    //运营商系统审核商品  ,修改状态.
    //0,未审核,1,审核通过,2,审核不通过,3关闭.
    $scope.updateStatus = function (status) {
        //调用服务方法
        goodsService.updateStatus($scope.selectIds,status).success(function (data) {

            if (data.success) {  //审核成功

                $scope.reloadList();  //刷新列表

                $scope.selectIds = [];  //情空id 集合
            } else {
                alert(data.message);
            }

        })

    };

});	
