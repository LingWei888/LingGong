package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.dto.EditRequireDTO;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.dto.RequireDTO;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class ProjectRequireUSER {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ProjectrequireService projectrequireService;
    @RequestMapping("/projectRequire")
    public Result projectRequire(@RequestBody INTDTO pid)
    {
        return projectService.listU(pid);
    }
    @RequestMapping("/editRequire")
    public Result editRequire(@RequestBody EditRequireDTO editRequireDTO){
        return projectService.editRequire(editRequireDTO);
    }
}
