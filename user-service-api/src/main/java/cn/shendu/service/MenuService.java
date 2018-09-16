package cn.shendu.service;


import cn.shendu.doMain.Menu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 权限菜单Service接口
 */
@RequestMapping("menuService")
@FeignClient("user-service")
public interface MenuService {
	
	/**
	 * 根据id获取实体
	 */
	@RequestMapping("findById")
	public Menu findById(@RequestParam("id") Integer id);

	/**
	 * 根据父节点以及用户角色id查询子节点
	 */
	@RequestMapping("findByParentIdAndRoleId")
	public List<Menu> findByParentIdAndRoleId(@RequestParam("parentId") int parentId,@RequestParam("roleId") int roleId);
	
	/**
	 * 根据角色id获取菜单集合
	 */
	@RequestMapping("findByRoleId")
	public List<Menu> findByRoleId(@RequestParam("roleId") int roleId);
	
	/**
	 * 根据父节点查找所有子节点
	 */
	@RequestMapping("findByParentId")
	public List<Menu> findByParentId(@RequestParam("parentId") int parentId);
}
