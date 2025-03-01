package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.Introduce;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.vo.ContactVO;

import cn.exitcode.day001.apicontect.service.ContactService;
import cn.exitcode.day001.apicontect.service.IntroduceService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/User")
public class Communicates {
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    private IntroduceService introduceService;
    @Autowired
    private JwtProperties jwtProperties;
    @RequestMapping("/contactList")
    public Result list(@RequestBody QueryPageParam param) {
        return userService.Ulist(param);
    }

    @RequestMapping("cxUserInfo")
    public Result cxUserInfo(@RequestBody INTDTO uid) {
        return userService.cxUserInfo(uid);
    }
    @RequestMapping("delContact")
    public Result delContact(@RequestBody INTDTO contact) {
        return contactService.delContact(contact);
    }

}
