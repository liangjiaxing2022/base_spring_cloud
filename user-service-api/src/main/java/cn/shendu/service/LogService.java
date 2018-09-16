package cn.shendu.service;

import cn.shendu.doMain.Log;
import cn.shendu.doMain.Sort;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 日志Service接口
 */
@RequestMapping("logService")
@FeignClient("user-service")
public interface LogService {

	/**
	 * 添加或者修改日志信息	 */
	@RequestMapping("save")
	public void save(@RequestBody Log log);
	
	/**
	 * 根据条件分页查询日志信息
	 */
	@RequestMapping("list")
	public List<Log> list(@RequestBody Log log, @RequestParam("page") Integer page,
						  @RequestParam("pageSize") Integer pageSize, @RequestParam("sort") Sort sort,
						  @RequestParam("properties") String[] properties);
	
	/**
	 * 获取总记录数
	 */
	@RequestMapping("getCount")
	public Long getCount(@RequestBody Log log);
}
