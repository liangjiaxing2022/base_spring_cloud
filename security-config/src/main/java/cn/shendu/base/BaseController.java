package cn.shendu.base;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseController {

    static  ThreadLocal<String> uidThreadLocal=new ThreadLocal<>();

    public void setUid(String uid){
        uidThreadLocal.set(uid);
    }
    public String getUid(){
      return  uidThreadLocal.get();
    }

    public String getRedisUserId(){
        return "user_"+getUid();
    }

    public String getRedisRoleId(){
        return "role_"+getUid();
    }

    protected static final String TO_LOGIN = "to_login";


    public String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<String> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            list.add(grantedAuthority.getAuthority());
        }
        return list;
    }
}
