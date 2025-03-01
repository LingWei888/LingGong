package cn.exitcode.day001.apicontect.controller.admin;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.service.ProjectService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Admin")
public class AdminProject {

    @Autowired
    private ProjectService projectService;


    @RequestMapping("/projectList")
    public Result list(@RequestBody QueryPageParam param){
        return projectService.listAll(param);
    }
    //对Project增删改的实现
    @RequestMapping("/projectSave")
    public Result save(@RequestBody Project project){
        return Result.result(200,"成功",0L,projectService.updateById(project));
    }
    @RequestMapping("/projectDelete")
    public Result delete(@RequestBody Project project){
        return Result.result(200,"成功",0L,projectService.removeById(project.getId()));
    }
    @RequestMapping("/projectAdd")
    public Result add(@RequestBody Project project){
        return Result.result(200,"成功",0L,projectService.save(project));
    }

}
