//抽取了一个父控制器

app.controller("baseController", function ($scope) {

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10, //总记录数.
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //重新加载,onChange此方法将会被自动加载
            //1,页面刷新
            //2,分页控件中数据发生变化，reloadList也会自动调用
            $scope.reloadList();
        }
    };

    //定义reloadList
    $scope.reloadList = function () {
        $scope.findPage($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    }; //传当前页currentPage,每页大小itemsPerPage

    //批量删除.定义数组,封装id参数.
    $scope.selectIds = []; //选中的复选框id集合.

    //更新复选框,组装选中id
    //$event 事件对象
    //$event.target.checked 翻译:事件,目标,选中.
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            //如果是被选中,则增加到数组.
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.seletIds.indexOf(id); //获取位置.
            $scope.selectIds.splice(idx, 1); //删除,指定位置,一个元素.
        }
    };


    //组装json文本数据
//循环参数数组：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
//参数1：json数组
//参数2：json的key固定式:text
//json数据操作：
//typeJson[i].key  这个方法是错误的方法，是获取不到值。必须使用:typeJson[i][key]
//案例：
//有如下格式的数据： list = [{id:1,name:"张三"},{id:2,name:"王五"}]
//获取name属性值：
//1,list[0].name √ 对象.属性 可以获取到值
//2,key="name" list[0].key × 不能获取到值，此时使用如下方法：list[0][key] √
//获取json对象中属性值，如果属性是通过变量存储的，必须使用[]方式获取
    $scope.jsonToStr = function (jsonStr,key) {
        //把json字符串转换json对象
        var typeJson = JSON.parse(jsonStr);

        //定义字符串对象，拼接json数组数据
        var value = "";

        //循环json数组对象
        for(var i=0;i<typeJson.length;i++){

            if (i>0){
                value += ",";
            }

            value +=  typeJson[i][key];
        }

        return value;

    }



});