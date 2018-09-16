package cn.shendu.service;

import cn.shendu.doMain.Role;
import cn.shendu.doMain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends User implements UserDetails {
    private List<Role> roleList;

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    private static final long serialVersionUID = 1L;
    public SecurityUser(User user) {
        if(user != null)
        {
            this.setTrueName(user.getTrueName());
            this.setRemarks(user.getRemarks());
            this.setId(user.getId());
            this.setUserName(user.getUserName());
            this.setPassword(user.getPassword());
            this.setRoles(user.getRoles());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();


        if(roleList != null)
        {
            for (Role role : roleList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
