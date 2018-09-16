package cn.shendu.controller;

import cn.shendu.doMain.Menu;
import cn.shendu.repository.MenuRepository;
import cn.shendu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Transactional
public class MenuController implements MenuService{
    @Autowired
    protected MenuRepository menuRepository;
    @Override
    public Menu findById(@RequestParam("id") Integer id) {
        return menuRepository.findOne(id);
    }

    @Override
    public List<Menu> findByParentIdAndRoleId(@RequestParam("parentId") int parentId,@RequestParam("roleId") int roleId){
        return menuRepository.findByParentIdAndRoleId(parentId, roleId);
    }

    @Override
    public List<Menu> findByRoleId(@RequestParam("roleId") int roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    @Override
    public List<Menu> findByParentId(@RequestParam("parentId") int parentId) {
        return menuRepository.findByParentId(parentId);
    }
}
