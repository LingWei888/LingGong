package cn.exitcode.day001.apicontect.controller.user;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.common.WebSocket.WebSocketServer;
import cn.exitcode.day001.apicontect.entity.Communicate;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.service.CommunicateService;
import cn.exitcode.day001.apicontect.service.ContactService;
import cn.exitcode.day001.apicontect.utils.AliOSSUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/User")
public class Talk {
    @Autowired
    private CommunicateService communicateService;

    @RequestMapping("/talklist")
    public Result talk(@RequestBody INTDTO toid1) { // 假设 DTO 类名为 ToidDTO
        return communicateService.talklist(toid1);
    }
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String url = aliOSSUtils.upload(file);
        return Result.success(url);
    }





}
