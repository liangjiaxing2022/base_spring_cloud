package cn.shendu.repository;

import cn.shendu.doMain.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户Repository接口
 */

public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {

	/**
	 * 根据用户名查找用户实体
	 */
	@Query(value="select * from t_user where user_name=?1",nativeQuery=true)
	public User findByUserName(String userName);
}
