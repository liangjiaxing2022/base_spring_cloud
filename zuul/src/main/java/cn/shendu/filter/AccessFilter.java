package cn.shendu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AccessFilter extends ZuulFilter{
    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
    private final String ACCESS_TOKEN="access_token";
    /*
    过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。这里定义为 pre,代表会在请求被路由之前执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /*
    过滤器的执行顺序。当请求在一个阶段中存在多个过滤器时，需要根据该方法返回的值来依次执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /*
    判断过滤器是否需要被执行，这里我们直接返回了true,因此该过滤器对所有请求都会生效。实际运用中我们可以利用该函数
    来指定过滤器的有效范围
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }


    /*
    没进行登录则进行登录
     */
    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String url = request.getRequestURL().toString();
        log.info("send {} request to {},",request.getMethod(),url);
        if(StringUtils.contains(url,"login")||StringUtils.contains(url,".css")||StringUtils.contains(url,".js")
                ||StringUtils.contains(url,"drawImage")){
            return null;
        }
        Cookie[] cookies = request.getCookies();

        String token = null;

        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), ACCESS_TOKEN)) {
                    token = cookie.getValue();
                }
            }
        }
        log.info("access token ok");
        if(token==null){
            ctx.set(FilterConstants.FORWARD_TO_KEY,"/");
        }

        return null;
    }
}












