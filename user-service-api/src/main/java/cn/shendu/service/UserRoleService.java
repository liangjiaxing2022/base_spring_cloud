package cn.shendu.service;

import cn.shendu.doMain.UserRole;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户角色关联service接口
 */
@RequestMapping("userRoleService")
@FeignClient("user-service")
public interface UserRoleService {

	/**
	 * 根据用户id删除所有关联信息
	 */
	@RequestMapping("deleteByUserId")
	public void deleteByUserId(@RequestParam("userId") Integer userId);
	
	/**
	 * 添加或者修改用户角色关联
	 */
	@RequestMapping("save")
	public void save(@RequestBody UserRole userRole);
	
	/**
	 * 根据角色id删除所有关联信息
	 */
	@RequestMapping("deleteByRoleId")
	public void deleteByRoleId(@RequestParam("roleId") Integer roleId);
}
