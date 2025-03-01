package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.CheckInfoDTO;
import cn.exitcode.day001.apicontect.entity.dto.PwdDTO;
import cn.exitcode.day001.apicontect.entity.dto.UserDTO;
import cn.exitcode.day001.apicontect.entity.vo.IUserInfoVO;
import cn.exitcode.day001.apicontect.service.ConfigService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/User")
public class InfoEdit {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ConfigService configService;
    @RequestMapping("/cxInfo")
    public Result cxInfo(){
        return userService.cxInfo();
    }
    @RequestMapping("/infoEdit")
    public Result infoEdit(@RequestBody UserDTO user){
        return userService.infoEdit(user);
    }
    @RequestMapping("/PwdEdit")
    public Result PwdEdit(@RequestBody PwdDTO user){
        return userService.PwdEdit(user);
    }
    @RequestMapping("/CheckInfo")
    public Result CheckInfo(@RequestBody CheckInfoDTO user){
        return configService.checkinfo(user);

    }
}
