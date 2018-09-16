package cn.shendu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
oauth2 的认证流程
1 用户（资源持有者）打开客户端，客户端询问用户授权
2 用户同意授权
3 客户端向授权服务器申请授权
4 授权服务器对客户端进行认证，也包括用户信息的认证，认证成功后授权给予令牌
5 客户端获取令牌后，携带令牌向资源服务器请求资源
6 资源服务器确认令牌正确无误，向客户端释放资源

oauth2 provider的角色被分为authorization service(授权服务) 和 resource service（资源服务器），通常它们不在同一个服务中，
可能一个authorization service 对应多个resource service。spring oauth2 需配合spring security 一起使用，所有的请求
由spring mvc控制器处理，并经过一系列的spring security过滤器

resource server 提供了受oauth2 保护的资源，这些资源为API接口，html页面，Js文件等

案例一：
角色：
授权中心 Uaa 工程 auth-service
资源工程 service-hi
浏览器
 ： 浏览器向资源服务器获取token ,以后的每次请求都需要携带token给资源服务 service-hi,资源 服务器获取到请求携带的token,
 后，通过远程调度将token给授权服务oauth-service确认

 */

@SpringBootApplication
public class SecurityConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityConfigApplication.class, args);
	}
}
