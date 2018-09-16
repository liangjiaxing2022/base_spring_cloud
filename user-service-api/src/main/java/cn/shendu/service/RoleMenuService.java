package cn.shendu.service;

import cn.shendu.doMain.RoleMenu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 角色菜单关联service接口
 */
@RequestMapping("roleMenuService")
@FeignClient("user-service")
public interface RoleMenuService {
	
	/**
	 * 根据角色id删除所有关联信息
	 */
	@RequestMapping("deleteByRoleId")
	public void deleteByRoleId(@RequestParam("roleId") Integer roleId);
	
	/**
	 * 保存实体
	 */
	@RequestMapping("save")
	public void save(@RequestBody RoleMenu roleMenu);
}
