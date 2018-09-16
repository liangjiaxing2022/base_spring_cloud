package cn.shendu.repository;

import cn.shendu.doMain.Menu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 菜单Repository接口
 */

public interface MenuRepository extends JpaRepository<Menu, Integer> {

	/**
	 * 根据角色id获取菜单集合
	 */
	@Query(value="SELECT m.* FROM t_role r,t_role_menu rm,t_menu m WHERE rm.`role_id`=r.`id` AND rm.`menu_id`=m.`id` AND r.`id`=?1",nativeQuery=true)
	public List<Menu> findByRoleId(int roleId);
	
	/**
	 * 根据父节点以及用户角色id查询子节点
	 */
	@Query(value="SELECT * FROM t_menu WHERE p_id=?1 AND id IN (SELECT menu_id FROM t_role_menu WHERE role_id=?2)",nativeQuery=true)
	public List<Menu> findByParentIdAndRoleId(int parentId, int roleId);
	
	/**
	 * 根据父节点查找所有子节点
	 */
	@Query(value="select * from t_menu where p_id=?1",nativeQuery=true)
	public List<Menu> findByParentId(int parentId);
}
