package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.dto.ProjectAddDTO;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import cn.exitcode.day001.apicontect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/User")
public class AddProject {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectrequireService projectrequireService;
    @RequestMapping("/addproject")
    public Result addproject(@RequestBody ProjectAddDTO project){
        return projectService.addproject(project);
    }
}
