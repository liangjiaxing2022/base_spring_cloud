package cn.shendu.controller;

import cn.shendu.doMain.User;
import cn.shendu.repository.UserRepository;
import cn.shendu.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@RestController
@Transactional
public class UserController implements UserService {
    @Autowired
    protected UserRepository userRepository;

    @Override
    public User findByUserName(@RequestParam("userName") String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> list(@RequestBody User user,@RequestParam("page") Integer page,
                           @RequestParam("pageSize") Integer pageSize) {
        Pageable pageable=new PageRequest(page-1,pageSize);
        Page<User> pageUser=userRepository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(user!=null){
                    if(StringUtils.isNotEmpty(user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName()+"%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        },pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(@RequestBody User user) {
        Long count=userRepository.count(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(user!=null){
                    if(StringUtils.isNotEmpty(user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName()+"%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }

        });
        return count;
    }

    @Override
    public void save(@RequestBody User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(@RequestParam("id") Integer id) {
        userRepository.delete(id);
    }

    @Override
    public User findById(@RequestParam("id") Integer id) {
        return userRepository.findOne(id);
    }
}
