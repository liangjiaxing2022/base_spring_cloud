package cn.shendu.controller.admin;

import cn.shendu.base.BaseController;
import cn.shendu.doMain.Log;
import cn.shendu.doMain.Sort;
import cn.shendu.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理用户Controller
 */
@RestController
@RequestMapping("/admin/log")
public class LogAdminController extends BaseController{

	@Autowired
	private LogService logService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	
	/**
	 * 根据条件分页查询日志信息
	 */
	@RequestMapping("/list")

	public Map<String,Object> list(Log log, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<Log> logList=logService.list(log, page, rows, Sort.DESC, new String[]{"time"});
		Long total=logService.getCount(log);
		resultMap.put("rows", logList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	
}
