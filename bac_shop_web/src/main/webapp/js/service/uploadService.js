//文件上传服务层
app.service("uploadService",function($http){
    this.uploadFile=function(){
//创建表单对象,封装表单数据
        var formData=new FormData();
//向表单对象添加文件数据
        formData.append("file",file.files[0]);
//发送文件上传请求,及文件上传数据
        return $http({
            method:'POST',
            url:"/upload/pic",
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }
});