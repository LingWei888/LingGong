package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.controller.user.email.SendEmail;
import cn.exitcode.day001.apicontect.entity.*;
import cn.exitcode.day001.apicontect.entity.dto.*;
import cn.exitcode.day001.apicontect.entity.vo.*;
import cn.exitcode.day001.apicontect.mapper.UserMapper;
import cn.exitcode.day001.apicontect.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ContactService contactService;
    @Autowired
    private IntroduceService introduceService;
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectrequireService projectrequireService;

    @Override
    public User login(LoginDTO res){
        if(res == null|| res.getUser() == null|| res.getPwd() == null)return null;
        User user1 = lambdaQuery().eq(User::getUser, res.getUser()).eq(User::getPwd, res.getPwd()).one();
        if(user1 != null){
            return user1;
        }else{
            return null;
        }
    }
    @Override
    public Result check(INTDTO res){
        User user = this.lambdaQuery().eq(User::getId, res.getId()).one();
        //System.out.println(res.getId());
        if(user==null){
            return Result.failstr("用户不存在");
        }else{
            user.setRoleid(1);
            this.updateById(user);
            sendEmail.sendVerificationCode(user.getMail(),"您的账号已被管理员审核通过，请重新登录网站进行登录。", "审核通过");

            Orders orders = ordersService.lambdaQuery().eq(Orders::getTwoid, user.getId()).one();
            if(orders!=null){
                orders.setStatus(9);
                ordersService.updateById(orders);
            }



            return Result.result(200,"审核成功",0L,null);
        }
    }
    @Override
    public Result userList(QueryPageParam param){
        Page<User> page = new Page<>(param.getPageNum(),param.getPageSize());
        String name = param.getKeyword();
        IPage res;
        if(name!=null){
            res=this.lambdaQuery().like(User::getUser,name).page(page);
        }else{
            res=this.page(page);
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }
    @Override
    public Result isUser(String user){
        if (user == null || user.trim().isEmpty()) {
            return Result.result(400, "用户名不能为空", 0L, null);
        }
        Integer list = this.lambdaQuery().eq(User::getUser,user).count();
        if(list>0){
            return Result.result(201,"账号已存在",0L,null);
        }else{
            return Result.result(200,"账号不存在,可注册",0L,null);
        }
    }
    @Override
    public Result Ulist(QueryPageParam param){
        Page<Contact> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=contactService.lambdaQuery().like(Contact::getToid,title).eq(Contact::getUid,jwtProperties.getUserId()).page(page);
        }else{
            res=contactService.lambdaQuery().eq(Contact::getUid,jwtProperties.getUserId()).page(page);
        }
        List<Contact> records = res.getRecords();
        List<ContactVO> contactVOList =  new ArrayList<>();
        for (Contact contact : records) {
            ContactVO contactVO = new ContactVO();
            User user = this.lambdaQuery().eq(User::getId,contact.getToid()).one();
            if(user==null){
                continue;
            }

            contactVO.setToid(contact.getToid());
            contactVO.setId(contact.getId());
            contactVO.setToName(user.getUser());
            contactVO.setNum(contact.getNum());
            contactVO.setQq(user.getQq());
            contactVOList.add(contactVO);
        }
        res.setRecords(contactVOList);
        return Result.result(200,"成功",res.getTotal(),res);

    }
    @Override
    public Result selectHandler( SelectHandler selectHandler){
        User user= this.lambdaQuery().eq(User::getId,jwtProperties.getUserId()).one();
        if(projectService.getById(selectHandler.getProjectId())==null) return Result.failstr("项目不存在");
        Project project=projectService.lambdaQuery().eq(Project::getId,selectHandler.getProjectId()).one();
        if(!project.getUid().equals(jwtProperties.getUserId().toString()))return Result.failstr("权限不足");
        if(user.getBzmoney()<project.getMoney())return Result.failstr("您的余额不足，无法开始此项目");
        if(project.getState()!=0)return Result.failstr("项目已开始");
        float money=user.getBzmoney()-project.getMoney();
        user.setBzmoney(money);
        this.updateById(user);

        return projectService.selectHandler(selectHandler);
    }

    @Override
    public Result completeProject(INTDTO id){
        if(projectService.getById(id.getId())==null) return Result.failstr("项目不存在");
        Project project = projectService.lambdaQuery().eq(Project::getId,id.getId()).one();
        if(!project.getUid().equals(jwtProperties.getUserId().toString()))return Result.failstr("权限不足");
        if(project.getState().equals(9)) return Result.failstr("项目已完成");
        boolean flag=false;
        List<Projectrequire> projectrequireList = projectrequireService.lambdaQuery().eq(Projectrequire::getProid, project.getId().toString()).list();
        for(Projectrequire projectrequire : projectrequireList){
            if(projectrequire.getState()!=2){
                flag=true;
            }
        }
        if(flag) return Result.failstr("还有未完成的需求");

        User user = this.lambdaQuery().eq(User::getId,(Integer.parseInt(project.getAcid())-10000)).one();
        if(user==null)return Result.failstr("用户不存在");
        if(project.getState()!=1)return Result.failstr("项目未开始");
        /*if(project.getUevalute()==null||project.getUevalute().equals("0")){*/

        user.setBzmoney(user.getBzmoney()+project.getMoney());
        this.updateById(user);

        return projectService.completeProject(id);
    }

    @Override
    public Result AClist(QueryPageParam param){
        Page<Contact> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=contactService.lambdaQuery().like(Contact::getToid,title).eq(Contact::getUid,0).page(page);
        }else{
            res=contactService.lambdaQuery().eq(Contact::getUid,0).page(page);
        }
        List<Contact> records = res.getRecords();
        List<ContactVO> contactVOList =  new ArrayList<>();
        for (Contact contact : records) {
            ContactVO contactVO = new ContactVO();
            User user = this.lambdaQuery().eq(User::getId,contact.getToid()).one();
            if(user==null){
                continue;
            }
            contactVO.setToid(contact.getToid());
            contactVO.setId(contact.getId());
            contactVO.setToName(user.getUser());
            contactVO.setNum(contact.getNum());
            contactVO.setQq(user.getQq());
            contactVOList.add(contactVO);
        }
        res.setRecords(contactVOList);
        return Result.result(200,"成功",res.getTotal(),res);

    }
    @Override
    public Result cxUserInfo(INTDTO uid){
        User user = this.lambdaQuery().eq(User::getId,uid.getId()).one();
        List<Introduce> introduce = introduceService.lambdaQuery().eq(Introduce::getUid,uid.getId()).list();
        List<ProRateVO> proRate= new ArrayList<>();
        List<Project> pro ;
        if(user.getRoleid()==0){
             pro = projectService.lambdaQuery().eq(Project::getState,"9").eq(Project::getAcid,String.valueOf(uid.getId()+ 10000)).list();
             for(Project project : pro) {
                 ProRateVO proRateVO = new ProRateVO();
                 proRateVO.setProject(project.getTitle());
                 proRateVO.setUser(project.getUid());
                 proRateVO.setRate(project.getAcevalute());
                 proRate .add(proRateVO);
             }
        }else{
            pro = projectService.lambdaQuery().eq(Project::getState,"9").eq(Project::getUid,uid.getId()).list();
            for(Project project : pro) {
                ProRateVO proRateVO = new ProRateVO();
                proRateVO.setProject(project.getTitle());
                proRateVO.setUser(project.getAcid());
                proRateVO.setRate(project.getUevalute());
                proRate .add(proRateVO);
            }
        }


        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user.getUser());
        userInfoVO.setTips(user.getTips());
        userInfoVO.setMail(user.getMail());
        userInfoVO.setIntroduce(introduce);
        userInfoVO.setProrate(proRate);

        return Result.result(200,"成功",null,userInfoVO);
    }
    @Override
    public Result cxInfo(){
        User user1 = this.lambdaQuery().eq(User::getId,jwtProperties.getUserId()).one();
        IUserInfoVO userInfoVO = new IUserInfoVO();
        userInfoVO.setQq(String.valueOf(user1.getQq()));
        userInfoVO.setMail(user1.getMail());
        userInfoVO.setTips(user1.getTips());
        userInfoVO.setBZMoney(user1.getBzmoney());
        String name = user1.getCard().split("\\|\\|")[0];
        userInfoVO.setName(name);
        return Result.result(200,"成功",null,userInfoVO);
    }
    @Override
    public Result infoEdit(UserDTO user){
        User user1 = this.lambdaQuery().eq(User::getId,jwtProperties.getUserId()).one();
        user1.setQq(user.getQq());
        user1.setMail(user.getMail());
        user1.setTips(user.getTips());
        this.updateById(user1);
        return Result.result(200,"成功",null,null);
    }
    @Override
    public Result PwdEdit(PwdDTO user){
        User user1 = this.lambdaQuery().eq(User::getId,jwtProperties.getUserId()).one();
        if(!user1.getUser().equals(user.getUser())) return Result.result(400,"非法操作",null,null);
        user1.setPwd(user.getPwd());
        this.updateById(user1);
        return Result.result(200,"成功",null,null);
    }

    @Override
    public Result getConfig(){
        User user1=this.lambdaQuery().eq(User::getId, jwtProperties.getUserId()).one();
        int roleid=user1.getRoleid();
        UserIndexVO userIndexVO = new UserIndexVO();
        userIndexVO.setRoleid(roleid);
        userIndexVO.setContactCount(contactService.lambdaQuery().eq(Contact::getUid, jwtProperties.getUserId()).count());
        int projectCount=0,proid=0,waitcount=0;
        List<Project> tmp;
        List<Projectrequire> tmp1 = List.of();
        if(roleid==1){//雇主
            projectCount = projectService.lambdaQuery().eq(Project::getUid, jwtProperties.getUserId()).count();
            tmp=projectService.lambdaQuery().eq(Project::getUid, jwtProperties.getUserId()).ne(Project::getState, 9).list();
        }else{//自由工作者
            projectCount = projectService.lambdaQuery().eq(Project::getAcid, jwtProperties.getUserId()+10000).ne(Project::getState, 0).count();
            tmp=projectService.lambdaQuery().eq(Project::getAcid, jwtProperties.getUserId()+10000).ne(Project::getState, 0).ne(Project::getState, 9).list();
        }
        for (Project project : tmp) {
            proid = project.getId();
            waitcount += projectrequireService.lambdaQuery().eq(Projectrequire::getProid, proid).eq(Projectrequire::getState, 0).count();
            List<Projectrequire> tmp2 = projectrequireService.lambdaQuery().eq(Projectrequire::getProid, proid).eq(Projectrequire::getState, 0).list();
            if(projectrequireService.lambdaQuery().eq(Projectrequire::getProid, proid).count()==0){
                project.setState(100);
            }else{
                project.setState((projectrequireService.lambdaQuery().eq(Projectrequire::getProid, proid).ne(Projectrequire::getState, 0).count()*100/projectrequireService.lambdaQuery().eq(Projectrequire::getProid, proid).count()));
            }

            tmp1= Stream.concat(tmp1.stream(),tmp2.stream()).toList();
        }
        userIndexVO.setProjectrequireList(tmp1);
        userIndexVO.setProjectList(tmp);
        userIndexVO.setProjectCount(projectCount);
        userIndexVO.setWaitCount(waitcount);
        userIndexVO.setMoney(user1.getBzmoney());
        return Result.result(200,"成功",0L,userIndexVO);
    }

    @Override
    public Result projectHall(QueryPageParam param) {
        Page<Project> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=projectService.lambdaQuery().like(Project::getTitle,title).eq(Project::getState,"0").page(page);
        }else{
            res=projectService.lambdaQuery().eq(Project::getState,"0").page(page);
        }
        //遍历一遍res将其中的uid查询数据库转换为用户名
        List<Project> projectList = res.getRecords();
        for (Project project : projectList) {
            String username = this.getById(project.getUid()).getUser();
            project.setAcid(username); // 假设Project类中有一个setUsername方法
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }

    @Override
    public Result AgreeList(INTDTO id) {
        if(projectService.getById(id.getId())==null) return Result.failstr("项目不存在");
        Project project = projectService.getById(id.getId());
        if(!project.getUid().equals(jwtProperties.getUserId().toString()))return Result.failstr("权限不足");
        AgreeListVO agreeListVO = new AgreeListVO();
        agreeListVO.setTitle(project.getTitle());
        agreeListVO.setContent(project.getContent());
        agreeListVO.setState(project.getState());
        agreeListVO.setId(project.getId());
        agreeListVO.setProjectrequireList(projectrequireService.lambdaQuery().eq(Projectrequire::getProid,id.getId()).list());
        //对Project中的acid进行,分割处理,然后遍历获取每一个数据
        if (agreeListVO.getUserVOList() == null) {
            agreeListVO.setUserVOList(new ArrayList<>());
        }
        String[] split = project.getAcid().split(",");
        for (String s : split) {
            if(s==null||s.trim().isEmpty())continue;
            UserVO userVO = new UserVO();
            userVO.setId(Integer.parseInt(s));
            User user=this.getById(Integer.parseInt(s)-10000);
            if(user==null) continue;
            userVO.setUserName(user.getUser());
            userVO.setQq(user.getQq());
            agreeListVO.getUserVOList().add(userVO);
        }
        return Result.result(200,"成功",0L,agreeListVO);
    }
}
