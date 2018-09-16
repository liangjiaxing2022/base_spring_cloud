package cn.shendu.controller;

import cn.shendu.annotation.Anoymous;
import cn.shendu.base.BaseController;
import cn.shendu.doMain.Log;
import cn.shendu.doMain.Menu;
import cn.shendu.doMain.Role;
import cn.shendu.doMain.User;
import cn.shendu.service.*;
import cn.shendu.utils.JwtTokenUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户Controller

 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private LogService logService;
/*	@Autowired
	private RedisUtils redisUtils;*/



/*	*//**
	 * 用户登录判断
	 * @param imageCode
	 * @param user
	 * @param bindingResult
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/login")
	@Anoymous
	public Map<String,Object> login(String imageCode, @Valid User user, BindingResult bindingResult, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isEmpty(imageCode)){
			map.put("success", false);
			map.put("errorInfo", "请输入验证码!");
			return map;
		}
	*//*	if(!session.getAttribute("checkcode").equals(imageCode)){
			map.put("success", false);
			map.put("errorInfo", "验证码输入错误!");
			return map;
		}*//*
		if(bindingResult.hasErrors()){
			map.put("success", false);
			map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
			return map;
		}

		try{
			String userName= user.getUserName();
			User currentUser=userService.findByUserName(userName);
			setUid(currentUser.getId()+"");
			redisUtils.set(getRedisUserId(),currentUser,new Long(5),TimeUnit.HOURS);

			List<Role> roleList=roleService.findByUserId(currentUser.getId());
			map.put("roleList", roleList);
			map.put("roleSize", roleList.size());
			map.put("uid",currentUser.getId());
			map.put("success", true);

			Map jwtMap = new HashMap();
			jwtMap.put("uid",currentUser.getId());
			String token = JwtTokenUtils.generatorToken(jwtMap);
			response.addHeader("Set-Cookie",
					"access_token="+token+";Path=/;HttpOnly");
			logService.save(new Log(Log.LOGIN_ACTION,"用户登录"));
			return map;
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("errorInfo", "用户名或者密码错误!");
			return map;
		}
	}*/
	
	/**
	 * 保存角色信息
	 * @param roleId
	 * @return
	 * @throws Exception
	 *//*
	@ResponseBody
	@RequestMapping("/saveRole")
	@Anoymous
	public Map<String,Object> saveRole(Integer roleId,String userId)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Role currentRole=roleService.findById(roleId);
		if(userId!=null){
			redisUtils.set(getRedisRoleId(),currentRole,new Long(5),TimeUnit.HOURS);
		}
		map.put("success", true);
		return map;
	}
	*/

	/**
	 * 加载当前用户信息
	 * @param session
	 * @return
	 * @throws Exception
	 *//*
	@ResponseBody
	@GetMapping("/loadUserInfo")
	public String loadUserInfo(HttpSession session)throws Exception{
		Object userObj = redisUtils.get(getRedisUserId());
		Object roleObj = redisUtils.get(getRedisRoleId());
		if(userObj!=null && roleObj!=null){
			User currentUser = (User)userObj;
			Role currentRole = (Role)roleObj;
			return "欢迎您："+currentUser.getTrueName()+"&nbsp;[&nbsp;"+currentRole.getName()+"&nbsp;]";
		}else{
			return TO_LOGIN;
		}

	}
	*/
	/**
	 * 加载权限菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping("/loadMenuInfo")
	public String loadMenuInfo(Integer parentId)throws Exception{
		List<String> authentication = getAuthentication();
		if(authentication!=null && authentication.size()!=0){
			Role currentRole = roleService.findByRoleName(authentication.get(0));
			return getAllMenuByParentId(parentId,currentRole.getId()).toString();
		}

		return null;
	}
	
	/**
	 * 获取所有菜单信息
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	public JsonArray getAllMenuByParentId(Integer parentId, Integer roleId){
		JsonArray jsonArray=this.getMenuByParentId(parentId, roleId);
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
			if("open".equals(jsonObject.get("state").getAsString())){
				continue;
			}else{
				jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt(), roleId));
			}
		}
		return jsonArray;
	}
	
	/**
	 * 根据父节点和用户角色Id查询菜单
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	public JsonArray getMenuByParentId(Integer parentId, Integer roleId){
		List<Menu> menuList=menuService.findByParentIdAndRoleId(parentId, roleId);
		JsonArray jsonArray=new JsonArray();
		for(Menu menu:menuList){
			JsonObject jsonObject=new JsonObject();
			jsonObject.addProperty("id", menu.getId()); // 节点Id
			jsonObject.addProperty("text", menu.getName()); // 节点名称
			if(menu.getState()==1){
				jsonObject.addProperty("state", "closed"); // 根节点
			}else{
				jsonObject.addProperty("state", "open"); // 叶子节点
			}
			jsonObject.addProperty("iconCls", menu.getIcon()); // 节点图标
			JsonObject attributeObject=new JsonObject(); // 扩展属性
			attributeObject.addProperty("url", menu.getUrl()); // 菜单请求地址
			jsonObject.add("attributes", attributeObject); 
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
}
