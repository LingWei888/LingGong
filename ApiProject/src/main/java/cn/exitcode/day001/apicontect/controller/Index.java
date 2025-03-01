package cn.exitcode.day001.apicontect.controller;

import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
public class Index {
    @Autowired
    private ConfigService configService;

    @PostMapping("/WebConfig")
    public Result WebConfig() {
        return Result.result(200, "获取成功", 0L,configService.getValueByKey("sitename"));
    }
    @RequestMapping("/")
    public String index() {
        return "网站运行Ing";
    }

}
