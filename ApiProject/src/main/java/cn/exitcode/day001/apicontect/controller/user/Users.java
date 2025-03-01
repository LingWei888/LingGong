package cn.exitcode.day001.apicontect.controller.user;


import cn.exitcode.day001.apicontect.common.JwtToken.JwtClaimsConstant;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtUtil;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.controller.user.email.SendEmail;
import cn.exitcode.day001.apicontect.entity.*;
import cn.exitcode.day001.apicontect.entity.dto.LoginDTO;
import cn.exitcode.day001.apicontect.entity.dto.RegDTO;
import cn.exitcode.day001.apicontect.entity.vo.UserConfig;
import cn.exitcode.day001.apicontect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/User")
public class Users {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ContactService contactService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private ProjectrequireService projectrequireService;
    @Autowired
    private SendEmail sendEmail;

    @RequestMapping("/isUser")
    public Result isUser(@RequestBody String user){
        Integer list = userService.lambdaQuery().eq(User::getUser,user).count();
        if(list>0){
            return Result.result(201,"账号已存在",0L,null);
        }else{
            return Result.result(200,"账号不存在,可注册",0L,null);
        }
    }
    @RequestMapping("/reg")
    public Result reg(@RequestBody RegDTO res){
        String email = res.getEmail();
        String code = res.getCode();
        String storedCode = redisService.getCode(email);

        if (storedCode == null) {
            return Result.failstr("验证码已过期或不存在");
        }
        Integer list = userService.lambdaQuery().eq(User::getUser,res.getUsername()).count();
        if(list>0){
            return Result.result(201,"账号已存在",0L,null);
        }else{
            if (storedCode.equals(code)) {
                redisService.deleteCode(email); // 验证成功后删除验证码
                User user = new User();
                user.setUser(res.getUsername());
                user.setPwd(res.getPassword());
                user.setMail(res.getEmail());

                user.setCard("");
                user.setBzmoney(0.0F);
                user.setXinyu("");
                user.setAddtime(String.valueOf(System.currentTimeMillis()));
                user.setTips("");


                user.setRoleid(-1);
                user.setState("0");
                userService.save(user);
                return Result.result(200,"注册成功",0L,null);
            } else {
                return Result.failstr(code);
            }
        }
    }
    @RequestMapping("/login")
    public Result login(@RequestBody LoginDTO res){
        User user=userService.login(res);
        if(user!=null){
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.LOGIN_ID, user.getId());
            claims.put(JwtClaimsConstant.roleid, user.getRoleid());
            claims.put(JwtClaimsConstant.USERNAME, user.getUser());
            String token = JwtUtil.createJWT(
                    jwtProperties.getUserSecretKey(),
                    jwtProperties.getUserTtl(),
                    claims);
            UserConfig map = new UserConfig();
            map.setUserId(user.getId());
            map.setToken(token);
            map.setRoleId(user.getRoleid());
            map.setUserName(user.getUser());
            map.setQq(user.getQq());
            return Result.result(200,"登录成功",0L,map);
        }else{
            return Result.failstr( "用户名或密码错误");
        }
    }
    @RequestMapping("/logout")
    public Result logout(){
        return Result.result(200,"退出成功",0L,null);
    }
    @RequestMapping("/index")
    public Result getConfig(){
        return userService.getConfig();
    }

    @RequestMapping("/cxUser")
    public Result cxUser(){
        return Result.result(200,"成功",0L,jwtProperties.getUserName());
    }
    @RequestMapping("/regcode")
    public Result regcode(@RequestBody RegDTO res){
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        redisService.setCode(res.getEmail(), code, 5, TimeUnit.MINUTES); // 存储验证码，5分钟过期
        sendEmail.sendVerificationCode(res.getEmail(),code,"注册验证码");
        return Result.result(200,"发送成功",0L,null);
    }



}
