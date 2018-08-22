//服务层
app.service('contentService',function($http){

    /*
    需求:根据内容分类id查询广告内容
     */
	this.findContentListByCategoryId=function(categroyId){
		return $http.get('../content/findContentListByCategoryId/'+categroyId);
	};

});
