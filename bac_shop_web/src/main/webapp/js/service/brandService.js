//定义service服务层
app.service("brandService", function ($http) {
    //把内置服务发送请求方法全部抽取到服务层代码

    //查询所有
    this.findAll = function () {
        return $http.get("../brand/findAll");
    };
    //分页查询
    this.findPage = function (pageNum, pageSize) {
        return $http.get("../brand/findPage/" + pageNum + "/" + pageSize);
    };
    //添加
    this.add = function (entity) {
        return $http.post("../brand/add", entity);
    };
    //修改
    this.update = function (entity) {
        return $http.post("../brand/update", entity);
    };
    //批量删除
    this.dele = function (ids) {
        return $http.get("../brand/delete/" + ids);
    };
    //根据id查询
    this.findOne = function (id) {
        return $http.get("../brand/findOne?id=" + id);
    };

    /*
    需求:查询品牌下拉列表
    查询数据格式:[{id:'1',text:'联想'},{id:'2',text:'华为'}]
    返回值:List<Map>
     */
    this.findBrandList = function () {
        return $http.get("../brand/findBrandList");
    }


});