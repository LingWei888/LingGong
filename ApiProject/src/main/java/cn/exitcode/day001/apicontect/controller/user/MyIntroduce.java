package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Introduce;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.service.IntroduceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class MyIntroduce {

    @Autowired
    private IntroduceService introduceService;

    @Autowired
    private JwtProperties jwtProperties;
    @RequestMapping("/MyIntroduce")
    public Result MyIntroduce(@RequestBody QueryPageParam param) {
        return introduceService.MyIntroduce(param);
    }
    @RequestMapping("/addIntroduce")
    public Result addIntroduce(@RequestBody Introduce introduce) {
        return introduceService.addIntroduce(introduce);
    }
    @RequestMapping("/updateIntroduce")
    public Result updateIntroduce(@RequestBody Introduce introduce) {
        return introduceService.updateIntroduce(introduce);
    }

    @RequestMapping("/deleteIntroduce")
    public Result deleteIntroduce(@RequestBody Introduce introduce) {
        return introduceService.deleteIntroduce(introduce);
    }

}
