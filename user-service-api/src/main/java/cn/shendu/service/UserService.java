package cn.shendu.service;

import cn.shendu.doMain.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户Service接口
 */
@RequestMapping("userService")
@FeignClient("user-service")
public interface UserService {
	/**
	 * 根据用户名查找用户实体
	 */
	@RequestMapping("findByUserName")
	public User findByUserName(@RequestParam("userName") String userName);

	/**
	 * 根据条件分页查询用户信息
	 */
	@RequestMapping("list")
	public List<User> list(@RequestBody User user,@RequestParam("page") Integer page,
						   @RequestParam("pageSize") Integer pageSize);

	/**
	 * 获取总记录数
	 */
	@RequestMapping("getCount")
	public Long getCount(@RequestBody User user);

	/**
	 * 添加或者修改用户信息
	 */
	@RequestMapping("save")
	public void save(@RequestBody User user);

	/**
	 * 根据id删除用户
	 */
	@RequestMapping("delete")
	public void delete(@RequestParam("id") Integer id);

	/**
	 * 根据id查询实体
	 */
	@RequestMapping("findById")
	public User findById(@RequestParam("id") Integer id);
	

}
