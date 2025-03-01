package cn.exitcode.day001.apicontect.controller.admin;

import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.dto.ApiDTO;
import cn.exitcode.day001.apicontect.entity.dto.LoginDTO;
import cn.exitcode.day001.apicontect.entity.dto.PwdDTO;
import cn.exitcode.day001.apicontect.entity.dto.WebConfigDTO;
import cn.exitcode.day001.apicontect.service.ConfigService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Admin")
public class AdminSet {

    @Autowired
    private ConfigService configService;
    @RequestMapping("/setPwd")
    public Result setPwd(@RequestBody PwdDTO res) {
        //判断是否为空
        return configService.setPwd(res);
    }
    @RequestMapping("/cxUser")
    public Result cxUser() {
        return Result.result(200, "查询成功", 0L,configService.getValueByKey("user"));
    }
    @RequestMapping("/cxConfig")
    public Result cxConfig() {
        return configService.cxConfig();
    }
    @RequestMapping("/setConfig")
    public Result setConfig(@RequestBody WebConfigDTO res) {
        return configService.setConfig(res);
    }
    @RequestMapping("/cxApi")
    public Result cxapi() {
        return configService.cxApi();
    }
    @RequestMapping("/setApi")
    public Result setapi(@RequestBody ApiDTO res) {
        return configService.setApi(res);
    }
}
