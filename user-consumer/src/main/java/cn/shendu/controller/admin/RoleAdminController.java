package cn.shendu.controller.admin;

import cn.shendu.base.BaseController;
import cn.shendu.doMain.*;
import cn.shendu.service.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 后台管理角色Controller
 */
@RestController
@RequestMapping("/admin/role")
public class RoleAdminController extends BaseController {

	
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private RoleMenuService roleMenuService;

	@Autowired
	private LogService logService;

	/**
	 * 查询所有角色
	 */
	@RequestMapping("/listAll")
	public Map<String,Object> listAll()throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("rows", roleService.listAll());
		logService.save(new Log(Log.SEARCH_ACTION,"查询所有角色信息"));
		return resultMap;
	}
	
	
	/**
	 * 分页查询角色信息
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(Role role, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<Role> roleList=roleService.list(role, page, rows);
		Long total=roleService.getCount(role);
		resultMap.put("rows", roleList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询角色信息"));
		return resultMap;
	}
	
	/**
	 * 添加或者修改角色信息
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Role role)throws Exception{
		if(role.getId()!=null){
			logService.save(new Log(Log.UPDATE_ACTION,"修改角色信息"+role));
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加角色信息"+role));
		}
		Map<String,Object> resultMap=new HashMap<>();
		if(role.getId()==null){
			if(roleService.findByRoleName(role.getName())!=null){
				resultMap.put("success", false);
				resultMap.put("errorInfo", "角色名已经存在！");
				return resultMap;
			}
		}
		roleService.save(role);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除角色信息
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(Integer id)throws Exception{
		logService.save(new Log(Log.DELETE_ACTION,"删除角色信息"+roleService.findById(id)));
		Map<String,Object> resultMap=new HashMap<>();
		roleMenuService.deleteByRoleId(id);
		userRoleService.deleteByRoleId(id);
		roleService.delete(id);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 根据父节点获取所有复选框权限菜单
	 */
	@RequestMapping("/loadCheckMenuInfo")
	public String loadCheckMenuInfo(Integer parentId,Integer roleId)throws Exception{
		List<Menu> menuList=menuService.findByRoleId(roleId);
		List<Integer> menuIdList=new LinkedList<Integer>();
		for(Menu menu:menuList){
			menuIdList.add(menu.getId());
		}
		return getAllCheckMenuByParentId(parentId, menuIdList).toString();
	}
	
	/**
	 * 根据父节点id和权限菜单id集合获取所有复选框菜单集合
	 */
	public JsonArray getAllCheckMenuByParentId(Integer parentId, List<Integer> menuIdList){
		JsonArray jsonArray=this.getCheckMenuByParentId(parentId, menuIdList);
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
			if("open".equals(jsonObject.get("state").getAsString())){
				continue;
			}else{
				jsonObject.add("children", getAllCheckMenuByParentId(jsonObject.get("id").getAsInt(), menuIdList));
			}
		}
		return jsonArray;
	}
	
	/**
	 * 根据父节点id和权限菜单id集合获取一层复选框菜单集合
	 */
	public JsonArray getCheckMenuByParentId(Integer parentId, List<Integer> menuIdList){
		List<Menu> menuList=menuService.findByParentId(parentId);
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
			if(menuIdList.contains(menu.getId())){
				jsonObject.addProperty("checked", true); 
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	/**
	 * 保存角色权限设置
	 */
	@RequestMapping("/saveMenuSet")
	public Map<String,Object> saveMenuSet(String menuIds,Integer roleId)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		roleMenuService.deleteByRoleId(roleId); // 根据角色id删除所有角色权限关联实体
		if(StringUtils.isNotEmpty(menuIds)){
			String idsStr[]=menuIds.split(",");
			for(int i=0;i<idsStr.length;i++){
				RoleMenu roleMenu=new RoleMenu();
				roleMenu.setRole(roleService.findById(roleId));
				roleMenu.setMenu(menuService.findById(Integer.parseInt(idsStr[i])));
				roleMenuService.save(roleMenu);
			}
		}
		resultMap.put("success", true);
		return resultMap;
	}
}
