package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.dto.RateDTO;
import cn.exitcode.day001.apicontect.entity.dto.SelectHandler;
import cn.exitcode.day001.apicontect.entity.vo.AgreeListVO;
import cn.exitcode.day001.apicontect.entity.vo.UserVO;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import cn.exitcode.day001.apicontect.service.UserService;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/User")
public class AgreeList {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectrequireService projectrequireService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @RequestMapping("/AgreeList")
    public Result AgreeList(@RequestBody INTDTO id){
        return userService.AgreeList(id);
    }
    @RequestMapping("/selectHandler")
    public Result selectHandler(@RequestBody SelectHandler selectHandler){
        return userService.selectHandler(selectHandler);
    }

    @RequestMapping("/completeProject")
    public Result completeProject(@RequestBody INTDTO id){
        return userService.completeProject(id);
    }
    @RequestMapping("/rateHandler")
    public Result rateHandler(@RequestBody RateDTO rateDTO){
        return projectService.rateHandler(rateDTO);
    }
    @RequestMapping("/completeProjectRequire")
    public Result completeProjectRequire(@RequestBody RateDTO id){
        return projectService.completeProjectRequire(id);
    }

}
