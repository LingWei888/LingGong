package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.vo.ProjectInfoVO;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/User")
public class ProjectHall {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectrequireService projectrequireService;
    @RequestMapping("/projectList")
    public Result projectList(@RequestBody QueryPageParam param) {
        return userService.projectHall(param);
    }
    @RequestMapping("/cxProjectInfo")
    public Result cxProjectInfo(@RequestBody INTDTO pid) {
        return projectService.cxProjectInfo(pid);
    }

    @RequestMapping("/enterProject")
    public Result enterProject(@RequestBody INTDTO pid) {
        return projectService.enterProject(pid);
    }
    @RequestMapping("/outProject")
    public Result outProject(@RequestBody INTDTO pid) {
        return projectService.outProject(pid);
    }
}
