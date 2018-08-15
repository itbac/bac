//服务层
app.service('itemService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../item/findAll');		
	};
	//分页 
	this.findPage=function(pageNum,pageSize){
		return $http.get('../item/findPage/'+pageNum+'/'+pageSize);
	};
	//查询实体
	this.findOne=function(id){
		return $http.get('../item/findOne/'+id);
	};
	//增加 
	this.add=function(entity){
		return  $http.post('../item/add',entity );
	};
	//修改 
	this.update=function(entity){
		return  $http.post('../item/update',entity );
	};
	//删除
	this.dele=function(ids){
		return $http.get('../item/delete/'+ids);
	};
	//搜索
	this.search=function(pageNum,pageSize,searchEntity){
		return $http.post('../item/search/'+pageNum+"/"+pageSize, searchEntity);
	}    	
});
