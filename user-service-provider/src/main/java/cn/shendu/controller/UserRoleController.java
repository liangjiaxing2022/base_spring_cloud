package cn.shendu.controller;

import cn.shendu.doMain.UserRole;
import cn.shendu.repository.UserRoleRepository;
import cn.shendu.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RestController
@Transactional
public class UserRoleController implements UserRoleService{
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void deleteByUserId(@RequestParam("userId") Integer userId) {
        userRoleRepository.deleteByUserId(userId);
    }

    @Override
    public void save(@RequestBody UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    public void deleteByRoleId(@RequestParam("roleId") Integer roleId) {
        userRoleRepository.deleteByRoleId(roleId);
    }
}
