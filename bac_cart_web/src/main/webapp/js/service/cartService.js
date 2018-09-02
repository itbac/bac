//服务层
app.service('cartService', function ($http) {

    //用户注册
    this.findCartList = function () {
        return $http.findCartList("../cart/findCartList");
    };

});
