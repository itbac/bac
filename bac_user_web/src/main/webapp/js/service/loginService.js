//服务层
app.service('loginService', function ($http) {

    //获取用户名
    this.loadUserInfo = function () {
        return $http.get("../login/loadUserInfo");
    };

});
