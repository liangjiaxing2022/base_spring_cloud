package cn.shendu.intercepter;

import cn.shendu.annotation.Anoymous;
import cn.shendu.base.BaseController;
import cn.shendu.doMain.User;
import cn.shendu.service.UserService;
import cn.shendu.utils.CookieUtil;
import cn.shendu.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;


public class JwtTokenAuthenticationFilter extends BasicAuthenticationFilter {

    private final String ACCESS_TOKEN="access_token";
    @Autowired
    UserService userService;
    public JwtTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token= CookieUtil.getCookieValue(request,ACCESS_TOKEN);
        if(token!=null){

            try{
                Claims claims= JwtTokenUtils.phaseToken(token);
                String username = claims.get("username").toString();
                List<String> authorities = claims.get("authorities", List.class);
                if (username != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
           
            }catch (Exception e){
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("{\"code\":\"-1\",\"msg\":\"error\"}");
            }

        }

        filterChain.doFilter(request, response);
    }


}
