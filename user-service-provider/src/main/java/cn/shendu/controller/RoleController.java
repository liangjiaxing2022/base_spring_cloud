package cn.shendu.controller;

import cn.shendu.doMain.Role;
import cn.shendu.repository.RoleRepository;
import cn.shendu.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
@RestController
@Transactional
public class RoleController implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<Role> findByUserId(@RequestParam("id") Integer id) {
        return roleRepository.findByUserId(id);
    }

    @Override
    public Role findById(@RequestParam("id") Integer id) {
        return roleRepository.findOne(id);
    }

    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> list(@RequestBody Role role, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Pageable pageable=new PageRequest(page-1,pageSize);
        Page<Role> pageUser=roleRepository.findAll(new Specification<Role>() {

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(role!=null){
                    if(StringUtils.isNotEmpty(role.getName())){
                        predicate.getExpressions().add(cb.like(root.get("name"), "%"+role.getName()+"%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        },pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(@RequestBody Role role) {
        Long count=roleRepository.count(new Specification<Role>() {

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(role!=null){
                    if(StringUtils.isNotEmpty(role.getName())){
                        predicate.getExpressions().add(cb.like(root.get("name"), "%"+role.getName()+"%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }

        });
        return count;
    }

    @Override
    public void save(@RequestBody Role role) {
        roleRepository.save(role);
    }

    @Override
    public void delete(@RequestParam("id") Integer id) {
        roleRepository.delete(id);
    }

    @Override
    public Role findByRoleName(@RequestParam("roleName") String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
