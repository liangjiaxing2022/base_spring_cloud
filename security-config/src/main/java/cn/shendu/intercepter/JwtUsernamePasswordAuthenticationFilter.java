package cn.shendu.intercepter;

import cn.shendu.doMain.User;
import cn.shendu.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/*
验证用户名密码正确后，生成一个token,并将token返回给客户端
attemptAuthentication 接收并解析用户凭证
successfulAuthentication 用户成功登陆后，这个方法会被调用，我们在这个方法里生成token
 */
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse rsp)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(req.getInputStream(),User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse rsp, FilterChain chain,
                                            Authentication auth) {

        Map<String,Object> map = new HashMap<>();
        map.put("username",auth.getName());
        map.put("authorities",auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String token = JwtTokenUtils.generatorToken(map);


        rsp.addHeader("Set-Cookie",
                "access_token="+token+";Path=/;HttpOnly");
        try {
            rsp.getWriter().write("{\"result\":\"ok\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
