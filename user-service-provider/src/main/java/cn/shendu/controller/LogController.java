package cn.shendu.controller;

import cn.shendu.doMain.Log;
import cn.shendu.repository.LogRepository;
import cn.shendu.service.LogService;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@RestController
@Transactional
public class LogController implements LogService{
    @Autowired
    protected LogRepository logRepository;

    @Override
    public void save(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> list(@RequestBody Log log, @RequestParam("page") Integer page,
                          @RequestParam("pageSize") Integer pageSize, @RequestParam("sort") cn.shendu.doMain.Sort sort,
                          @RequestParam("properties") String[] properties) {
        Sort.Direction direction = null;

        if(sort == cn.shendu.doMain.Sort.ASC){
            direction = Sort.Direction.ASC;
        }else if(sort == cn.shendu.doMain.Sort.DESC){
            direction = Sort.Direction.DESC;
        }


        Pageable pageable=new PageRequest(page-1,pageSize,direction,properties);
        Page<Log> pageLog=logRepository.findAll(new Specification<Log>() {

            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(log!=null){
                    if(StringUtils.isNotEmpty(log.getType())){
                        predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
                    }
                    if(log.getUser()!=null && StringUtils.isNotEmpty(log.getUser().getTrueName())){
                        predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
                    }
                    if(log.getBtime()!=null){
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if(log.getEtime()!=null){
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageLog.getContent();
    }

    @Override
    public Long getCount(@RequestBody Log log) {
        Long count=logRepository.count(new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(log!=null){
                    if(StringUtils.isNotEmpty(log.getType())){
                        predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
                    }
                    if(log.getUser()!=null && StringUtils.isNotEmpty(log.getUser().getTrueName())){
                        predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
                    }
                    if(log.getBtime()!=null){
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if(log.getEtime()!=null){
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }

        });
        return count;
    }
}
