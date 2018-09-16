package cn.shendu.service;

import cn.shendu.doMain.Role;
import cn.shendu.doMain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/*
    自定义的用户认证
    并且指定了使用密码的加密算法为 B


    通过这个SecutityUser 来完成用户的身份认证,其中,loadUserByUsername 调用了用户
    资源库接口的findByName方法,取得登录用户的详细信息

 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.findByUserName(userName);
        List<Role> roleList = roleService.findByUserId(user.getId());
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        }
        SecurityUser securityUser = new SecurityUser(user);
        securityUser.setRoleList(roleList);
        return securityUser;
    }
}












