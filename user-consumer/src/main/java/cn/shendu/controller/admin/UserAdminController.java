package cn.shendu.controller.admin;

import cn.shendu.base.BaseController;
import cn.shendu.doMain.Log;
import cn.shendu.doMain.Role;
import cn.shendu.doMain.User;
import cn.shendu.doMain.UserRole;
import cn.shendu.service.LogService;
import cn.shendu.service.RoleService;
import cn.shendu.service.UserRoleService;
import cn.shendu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理用户Controller
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private LogService logService;
	
	/**
	 * 分页查询用户信息
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String,Object> list(User user, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception{

		System.out.println("userid:"+getUid());
		Map<String,Object> resultMap=new HashMap<>();
		List<User> userList=userService.list(user, page, rows);
		for(User u:userList){
			List<Role> roleList=roleService.findByUserId(u.getId());
			StringBuffer sb=new StringBuffer();
			for(Role r:roleList){
				sb.append(","+r.getName());
			}
			u.setRoles(sb.toString().replaceFirst(",", ""));
		}
		Long total=userService.getCount(user);
		resultMap.put("rows", userList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询用户信息"));
		return resultMap;
	}
	
	/**
	 * 添加或者修改用户信息
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(User user)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		if(user.getId()==null){
			if(userService.findByUserName(user.getUserName())!=null){
				resultMap.put("success", false);
				resultMap.put("errorInfo", "用户名已经存在！");
				return resultMap;
			}
		}
		if(user.getId()!=null){
			logService.save(new Log(Log.UPDATE_ACTION,"更新用户信息"+user));
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加用户信息"+user));
		}
		userService.save(user);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除用户信息
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(Integer id)throws Exception{
		logService.save(new Log(Log.DELETE_ACTION,"删除用户信息"+userService.findById(id)));
		Map<String,Object> resultMap=new HashMap<>();
		userRoleService.deleteByUserId(id);
		userService.delete(id);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 保存用户角色设置
	 */
	@RequestMapping("/saveRoleSet")
	@ResponseBody
	public Map<String,Object> saveRoleSet(String roleIds,Integer userId)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		userRoleService.deleteByUserId(userId);
		if(StringUtils.isNotEmpty(roleIds)){
			String roleIdStr[]=roleIds.split(",");
			for(int i=0;i<roleIdStr.length;i++){
				UserRole userRole=new UserRole();
				userRole.setUser(userService.findById(userId));
				userRole.setRole(roleService.findById(Integer.parseInt(roleIdStr[i])));
				userRoleService.save(userRole);
			}
		}
		resultMap.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"保存用户角色设置"));
		return resultMap;
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping("/modifyPassword")
	@ResponseBody
	public Map<String,Object> modifyPassword(String newPassword)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		User user=userService.findById(Integer.parseInt(getUid()));
		user.setPassword(newPassword);
		userService.save(user);
		resultMap.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"修改密码"));
		return resultMap;
	}
	
	/**
	 * 安全退出
	 */
	@GetMapping("/logout")
	public String logout()throws Exception{

		//@ToDo 登出处理
		logService.save(new Log(Log.LOGOUT_ACTION,"用户注销"));

		return "redirect:/login.html";
	}
}
