package cn.shendu.controller;

import cn.shendu.base.BaseController;
import cn.shendu.doMain.User;
import cn.shendu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxinguo on 2018/06/05.
 */
@RestController
@RequestMapping("/users")
public class RegisterController extends BaseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        User bizUser = userService.findByUserName(user.getUserName());
        if(null != bizUser){
            throw new RuntimeException("用户不存在.......");
        }
        /*user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));*/
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.save(user);
        return user;
    }

}
