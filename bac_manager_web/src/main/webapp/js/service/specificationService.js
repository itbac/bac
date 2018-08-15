//定义service服务层
app.service("specificationService", function ($http) {
    //把内置服务发送请求方法全部抽取到服务层代码

    //查询所有
    this.findAll = function () {
        return $http.get("../specification/findAll");
    };
    //分页查询
    this.findPage = function (pageNum, pageSize) {
        return $http.get("../specification/findPage/" + pageNum + "/" + pageSize);
    };
    //添加
    this.add = function (entity) {
        return $http.post("../specification/add", entity);
    };
    //修改
    this.update = function (entity) {
        return $http.post("../specification/update", entity);
    };
    //批量删除
    this.dele = function (ids) {
        return $http.get("../specification/delete/" + ids);
    };
    //根据id查询
    this.findOne = function (id) {
        return $http.get("../specification/findOne?id=" + id);
    };

    /*
    需求:查询规格值,进行下拉列表展示
     */
    this.findSpecList = function () {
        return $http.get("../specification/findSpecList");
    }


});