package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.controller.user.email.SendEmail;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.*;
import cn.exitcode.day001.apicontect.entity.vo.AgreeListVO;
import cn.exitcode.day001.apicontect.entity.vo.ProjectInfoVO;
import cn.exitcode.day001.apicontect.entity.vo.UserVO;
import cn.exitcode.day001.apicontect.mapper.ProjectMapper;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ProjectrequireService projectrequireService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private SendEmail sendEmail;


    @Override
    public Result listAll(QueryPageParam param) {
        Page<Project> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=this.lambdaQuery().like(Project::getTitle,title).page(page);
        }else{
            res=this.page(page);
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }
    @Override
    public Result addproject(ProjectAddDTO project){
        if(jwtProperties.getRoleId()!=1) return Result.failstr("您不是雇主，没有权限");

        Project project1=new Project();
        project1.setUid(jwtProperties.getUserId().toString());
        project1.setTitle(project.getTitle());
        project1.setContent(project.getContent());
        project1.setMoney(project.getAmount());
        project1.setAddtime(String.valueOf(System.currentTimeMillis()));
        project1.setEndtime(project.getDeadline());
        this.save(project1);
        //取刚加入数据库中的project数据库的id

        //对project中的List<String> projectquire进行遍历，然后插入到project_quire表中
        for (String s : project.getRequirements()) {
            //插入到project_quire表中
            Projectrequire projectrequire=new Projectrequire();
            projectrequire.setProid(project1.getId().toString());
            projectrequire.setContent(s);
            projectrequireService.save(projectrequire);
        }
        return Result.success(null);
    }

    @Override
    public Result selectHandler(SelectHandler selectHandler) {

        Project project = this.getById(selectHandler.getProjectId());
        project.setState(1);
        project.setAcid(String.valueOf(selectHandler.getUserId()+10000));
        this.updateById(project);
        return Result.success(null);
    }
    @Override
    public Result completeProject(INTDTO id) {
        Project project = this.getById(id.getId());
        project.setState(9);
        this.updateById(project);
        return Result.success(null);
    }
    @Override
    public Result rateHandler(RateDTO rateDTO) {
        if(this.getById(rateDTO.getId())==null) return Result.failstr("项目不存在");
        if(rateDTO.getRating()>5||rateDTO.getRating()<0)return Result.failstr("评价范围在0-5");
        Project project = this.getById(rateDTO.getId());
        if(!project.getUid().equals(jwtProperties.getUserId().toString())&&!project.getAcid().equals(String.valueOf(jwtProperties.getUserId()+10000)))return Result.failstr("权限不足");
        if(!project.getState().equals(9)) return Result.failstr("项目未完成");
        if(jwtProperties.getRoleId()==0){
            if(!project.getUevalute().equals("0"))return Result.failstr("您已经评价过了");
            project.setUevalute(String.valueOf(rateDTO.getRating()));
        }else{
            if(!project.getAcevalute().equals("0"))return Result.failstr("您已经评价过了");
            project.setAcevalute(String.valueOf(rateDTO.getRating()));
        }
        this.updateById(project);
        return Result.success(null);
    }
    @Override
    public Result projectList(QueryPageParam param) {
        LambdaQueryChainWrapper<Project> list;
        if(jwtProperties.getRoleId()==1){
            list=this.lambdaQuery().eq(Project::getUid,jwtProperties.getUserId());
        }else{
            list=this.lambdaQuery().like(Project::getAcid,jwtProperties.getUserId()+10000);
        }
        Page<Project> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=list.like(Project::getTitle,title).page(page);
        }else{
            res=list.page(page);
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }

    @Override
    public Result cxProjectInfo(INTDTO pid) {
        Project project = this.getById(pid.getId());
        if(project==null) return Result.failstr("项目不存在");
        if(project.getState().equals("1")) return Result.failstr("项目已开始");
        ProjectInfoVO projectInfoVO = new ProjectInfoVO();

        projectInfoVO.setTitle(project.getTitle());
        projectInfoVO.setContent(project.getContent());
        projectInfoVO.setEndtime(project.getEndtime());
        projectInfoVO.setProjectrequire(projectrequireService.lambdaQuery().eq(Projectrequire::getProid,pid.getId()).list());
        return Result.result(200,"成功",0L,projectInfoVO);
    }
    @Override
    public Result enterProject(INTDTO pid) {
        Project res= this.lambdaQuery().eq(Project::getId,pid.getId()).one();
        if(jwtProperties.getRoleId()<0)return Result.failstr(" 请等待审核");
        if(jwtProperties.getRoleId()==1)return Result.failstr("雇主不能加入项目");
        if(res.getUid().equals(String.valueOf(jwtProperties.getUserId()))) return Result.failstr("不能加入自己的项目");
        if(res==null) return Result.failstr("项目不存在");
        if(res.getState()!=0) return Result.failstr("项目已开始");
        if(res.getAcid()!=null&&!res.getAcid().equals("")){
            String[] acid=res.getAcid().split(",");
            for(int i=0;i<acid.length;i++){
                if(acid[i].equals(String.valueOf(jwtProperties.getUserId()+10000))){
                    return Result.failstr("你已经加入该项目");
                }
                else{
                    res.setAcid(res.getAcid()+","+String.valueOf(jwtProperties.getUserId()+10000));
                }
            }
        }else{
            res.setAcid(String.valueOf(jwtProperties.getUserId()+10000));
        }
        return Result.result(200,"成功",0L,this.updateById(res));
    }

    @Override
    public Result completeProjectRequire(RateDTO pid){
        Projectrequire projectrequire= projectrequireService.lambdaQuery().eq(Projectrequire::getId,pid.getId()).one();
        if(projectrequire.getState()==2)return Result.failstr("项目需求已完成");
        if(projectrequire.getState()==0)return Result.failstr("自由工作者还并未处理！");
        if(projectrequire.getState()!=1)return Result.failstr("错误状态！");
        if(pid.getRating()!=0&&pid.getRating()!=2)return Result.failstr("错误状态！");
        Project project = this.getById(projectrequire.getProid());
        if(!project.getUid().equals(String.valueOf(jwtProperties.getUserId())))return Result.failstr("权限不足");
        projectrequire.setState(pid.getRating());
        projectrequireService.updateById(projectrequire);
        return Result.result(200,"成功",0L,null);
    }

    @Override
    public Result outProject(INTDTO pid) {
        if (this.getById(pid.getId()) == null) return Result.failstr("项目不存在");

        Project project = this.getById(pid.getId());
        if(!project.getState().equals(0))return Result.failstr("项目已开始,无法退出");
        if (!project.getAcid().contains(String.valueOf(jwtProperties.getUserId() + 10000)))
            return Result.failstr("权限不足");
        String[] acid = project.getAcid().split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < acid.length; i++) {
            if (acid[i].equals(String.valueOf(jwtProperties.getUserId() + 10000))) {
                continue;
            } else {
                sb.append(acid[i] + ",");
            }
        }
        project.setAcid(sb.toString());
        this.updateById(project);
        return Result.result(200, "成功", 0L, null);

    }


    @Override
    public Result listU(INTDTO pid) {
        if(this.getById(pid.getId())==null) return Result.failstr("项目不存在");
        Project project = this.getById(pid.getId());
        if(project.getState().equals(0))return Result.failstr("项目未开始");
        if(!project.getAcid().equals(String.valueOf(jwtProperties.getUserId()+10000))) return Result.failstr("权限不足");
        RequireDTO requireDTO = new RequireDTO();
        requireDTO.setTitle(project.getTitle());
        requireDTO.setState(project.getState());
        requireDTO.setContent(project.getContent());
        requireDTO.setEndtime(project.getEndtime());
        requireDTO.setTodo(projectrequireService.lambdaQuery().eq(Projectrequire::getProid,pid.getId()).eq(Projectrequire::getState,0).list());
        requireDTO.setDoing(projectrequireService.lambdaQuery().eq(Projectrequire::getProid,pid.getId()).eq(Projectrequire::getState,1).list());
        requireDTO.setDone(projectrequireService.lambdaQuery().eq(Projectrequire::getProid,pid.getId()).eq(Projectrequire::getState,2).list());
        return Result.result(200,"成功",0L,requireDTO);
    }


    @Override
    public Result editRequire(EditRequireDTO editRequireDTO) {
        if(this.getById(editRequireDTO.getProid())==null) return Result.failstr("项目不存在");

        if(projectrequireService.lambdaQuery().eq(Projectrequire::getId,editRequireDTO.getId()).eq(Projectrequire::getProid,editRequireDTO.getProid())==null) return Result.failstr("项目不存在");
        Project project = this.getById(editRequireDTO.getProid());
        if(project.getState().equals(9))return Result.failstr("项目已完成");
        if(!project.getAcid().equals(String.valueOf(jwtProperties.getUserId()+10000))) return Result.failstr("权限不足");
        if(editRequireDTO.getState()!=0&&editRequireDTO.getState()!=1)return Result.failstr("无法处理为其他状态");

        Projectrequire projectrequire = projectrequireService.getById(editRequireDTO.getId());
        if(projectrequire.getState()==2)return Result.failstr("已经处理完成");
        projectrequire.setState(editRequireDTO.getState());

        projectrequireService.updateById(projectrequire);
        return Result.result(200,"成功",0L,null);
    }

}
