package com.bac.manager.controller;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.pojo.TbItem;
import com.bac.manager.service.ItemService;

import com.bac.utils.PageResult;
import com.bac.utils.BacResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/item")
public class ItemController {

	@Reference
	private ItemService itemService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItem> findAll(){			
		return itemService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{pageNum}/{pageSize}")
	public PageResult  findPage(@PathVariable int pageNum,@PathVariable int pageSize){			
		return itemService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param item
	 * @return
	 */
	@RequestMapping("/add")
	public BacResult add(@RequestBody TbItem item){
		try {
			itemService.add(item);
			return new BacResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new BacResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param item
	 * @return
	 */
	@RequestMapping("/update")
	public BacResult update(@RequestBody TbItem item){
		try {
			itemService.update(item);
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
	public TbItem findOne(@PathVariable Long id){
		return itemService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public BacResult delete(@PathVariable Long [] ids){
		try {
			itemService.delete(ids);
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
	public PageResult search(@RequestBody TbItem item, int pageNum, int pageSize  ){
		return itemService.findPage(item, pageNum, pageSize);		
	}
	
}
