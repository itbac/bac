//控制层
app.controller('cartController', function ($scope, cartService) {

    //查询购物车
    $scope.findCartList = function () {
        cartService.findCartList().success(function (data) {
            $scope.cartList = data;

        })

    }


});
