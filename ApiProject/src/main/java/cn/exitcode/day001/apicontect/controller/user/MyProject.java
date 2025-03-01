package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtClaimsConstant;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.service.ProjectService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/User")
public class MyProject {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtProperties jwtProperties;


    @RequestMapping("/myProject")
    public Result projectList(@RequestBody QueryPageParam param) {
        return projectService.projectList(param);
    }


}
