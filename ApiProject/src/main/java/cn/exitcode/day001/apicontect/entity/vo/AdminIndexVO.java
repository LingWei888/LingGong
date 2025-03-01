package cn.exitcode.day001.apicontect.entity.vo;

import cn.exitcode.day001.apicontect.entity.User;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class AdminIndexVO {
    private String user;
    private int userCount;
    private int projectCount;
    private int orderCount;
    private List<User> userList;

}
