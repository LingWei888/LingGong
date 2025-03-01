package cn.exitcode.day001.apicontect.controller.admin;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.service.CommunicateService;
import cn.exitcode.day001.apicontect.service.UserService;
import cn.exitcode.day001.apicontect.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/Admin")
public class AdminCSE {
    @Autowired
    private UserService userService;
    @Autowired
    private CommunicateService communicateService;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @RequestMapping("/CSE")
    public Result CSE(@RequestBody QueryPageParam param) {
        return userService.AClist(param);
    }
    @RequestMapping("CSETalk")
    public Result CSETalk(@RequestBody INTDTO id) {
        return communicateService.ACtalk(id);
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String url = aliOSSUtils.upload(file);
        return Result.success(url);
    }
}
