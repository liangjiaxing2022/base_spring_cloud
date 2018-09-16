package cn.shendu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
SSO 客户端要使用 安全配置模块的功能，需要在工程的maven依赖管理中
增加 security-config 模块

需要配置两方面内容，oauth2 的配置 与spring security 的配置,
 */

@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class UserConsumerApplication extends WebMvcConfigurerAdapter {
	public static void main(String[] args) {
		SpringApplication.run(UserConsumerApplication.class, args);
	}



}
