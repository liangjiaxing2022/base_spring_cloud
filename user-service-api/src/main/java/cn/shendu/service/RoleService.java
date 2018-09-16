package cn.shendu.service;

import cn.shendu.doMain.Role;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色Service接口
 */
@RequestMapping("roleService")
@FeignClient("user-service")
public interface RoleService {

	/**
	 * 根据用户id查角色集合
	 */
	@RequestMapping("findByUserId")
	public List<Role> findByUserId(@RequestParam("id") Integer id);
	
	/**
	 * 根据id查询实体
	 */
	@RequestMapping("findById")
	public Role findById(@RequestParam("id") Integer id);
	
	/**
	 * 查询所有角色信息
	 */
	@RequestMapping("listAll")
	public List<Role> listAll();
	
	/**
	 * 根据条件分页查询角色信息
	 */
	@RequestMapping("list")
	public List<Role> list(@RequestBody Role role, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
	
	/**
	 * 获取总记录数
	 */
	@RequestMapping("getCount")
	public Long getCount(@RequestBody Role role);
	
	/**
	 * 添加或者修改角色信息
	 */
	@RequestMapping("save")
	public void save(@RequestBody Role role);
	
	/**
	 * 根据id删除角色
	 */
	@RequestMapping("delete")
	public void delete(@RequestParam("id") Integer id);
	
	/**
	 * 根据角色名查找角色实体
	 */
	@RequestMapping("findByRoleName")
	public Role findByRoleName(@RequestParam("roleName") String roleName);
}
