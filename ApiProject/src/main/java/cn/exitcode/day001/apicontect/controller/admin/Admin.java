package cn.exitcode.day001.apicontect.controller.admin;


import cn.exitcode.day001.apicontect.common.JwtToken.JwtClaimsConstant;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtUtil;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.controller.user.email.SendEmail;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.dto.LoginDTO;
import cn.exitcode.day001.apicontect.entity.vo.AdminIndexVO;
import cn.exitcode.day001.apicontect.service.ConfigService;
import cn.exitcode.day001.apicontect.service.OrdersService;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.exitcode.day001.apicontect.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/Admin")
public class Admin {
    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping("/index")
    public Result getConfig() {
        return configService.getConfig();
    }

    @RequestMapping("/check")
    public Result check(@RequestBody INTDTO res) {
        return userService.check(res);
    }
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO res) {
        if(configService.Login(res)){
            Map<String, Object> Admin2 = new HashMap<>();
            Admin2.put(JwtClaimsConstant.LOGIN_ID, 1);
            String token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    Admin2);
            return Result.result(200, "登录成功", 0L,token);
        }else{
            return Result.failstr("用户名或密码错误");
        }
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.result(200, "退出成功", 0L,null);
    }



}
