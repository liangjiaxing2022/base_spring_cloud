package cn.shendu.controller;

import cn.shendu.doMain.RoleMenu;
import cn.shendu.repository.RoleMenuRepository;
import cn.shendu.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Transactional
@RestController
public class RoleMenuController implements RoleMenuService {
    @Autowired
    protected RoleMenuRepository roleMenuRepository;

    @Override
    public void deleteByRoleId(@RequestParam("roleId") Integer roleId) {
            roleMenuRepository.deleteByRoleId(roleId);
    }

    @Override
    public void save(@RequestBody RoleMenu roleMenu) {
        roleMenuRepository.save(roleMenu);
    }
}
