//服务层
app.service('itemCatService', function ($http) {

    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../itemCat/findAll');
    };
    //分页
    this.findPage = function (pageNum, pageSize) {
        return $http.get('../itemCat/findPage/' + pageNum + '/' + pageSize);
    };
    //查询实体
    this.findOne = function (id) {
        return $http.get('../itemCat/findOne/' + id);
    };
    //增加
    this.add = function (entity) {
        return $http.post('../itemCat/add', entity);
    };
    //修改
    this.update = function (entity) {
        return $http.post('../itemCat/update', entity);
    };
    //删除
    this.dele = function (ids) {
        return $http.get('../itemCat/delete/' + ids);
    };
    //搜索
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post('../itemCat/search/' + pageNum + "/" + pageSize, searchEntity);
    };
    /*
  需求:根据父id查询子节点
   */
    this.findItemCatByParentId = function (parentId) {
        return $http.get('../itemCat/findItemCatList/' + parentId);
    }
});
