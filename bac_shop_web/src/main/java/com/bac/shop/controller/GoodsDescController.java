package com.bac.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.GoodsDescService;
import com.bac.pojo.TbGoodsDesc;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goodsDesc")
public class GoodsDescController {

	@Reference
	private GoodsDescService goodsDescService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoodsDesc> findAll(){			
		return goodsDescService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{pageNum}/{pageSize}")
	public PageResult  findPage(@PathVariable int pageNum,@PathVariable int pageSize){			
		return goodsDescService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/add")
	public BacResult add(@RequestBody TbGoodsDesc goodsDesc){
		try {
			goodsDescService.add(goodsDesc);
			return new BacResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new BacResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/update")
	public BacResult update(@RequestBody TbGoodsDesc goodsDesc){
		try {
			goodsDescService.update(goodsDesc);
			return new BacResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new BacResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbGoodsDesc findOne(@PathVariable Long id){
		return goodsDescService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public BacResult delete(@PathVariable Long [] ids){
		try {
			goodsDescService.delete(ids);
			return new BacResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new BacResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoodsDesc goodsDesc, int pageNum, int pageSize  ){
		return goodsDescService.findPage(goodsDesc, pageNum, pageSize);		
	}
	
}
