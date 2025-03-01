package cn.exitcode.day001.apicontect.controller.admin;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Admin")
public class AdminUser {

    @Autowired
    private UserService userService;


    @RequestMapping("/userList")
    public Result list(@RequestBody QueryPageParam param){
        return userService.userList(param);

    }
    //对User增删改的实现
    @RequestMapping("/userSave")
    public Result save(@RequestBody User user){
        return Result.result(200,"成功",0L,userService.updateById(user));
    }
    @RequestMapping("/userDelete")
    public Result delete(@RequestBody User user){
        return Result.result(200,"成功",0L,userService.removeById(user.getId()));
    }
    @RequestMapping("/userAdd")
    public Result add(@RequestBody User user){
        return Result.result(200,"成功",0L,userService.save(user));
    }
    //查询是否user存在
    @RequestMapping("/isUser")
    public Result isUser(@RequestBody String user){
        return userService.isUser(user);
    }
}
