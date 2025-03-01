package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Communicate;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.CommunicateDTO;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.dto.ImSingleDTO;
import cn.exitcode.day001.apicontect.mapper.CommunicateMapper;
import cn.exitcode.day001.apicontect.service.CommunicateService;
import cn.exitcode.day001.apicontect.service.ContactService;
import cn.exitcode.day001.apicontect.service.UserService;
import cn.exitcode.day001.apicontect.utils.AliOSSUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class CommunicateServiceImpl extends ServiceImpl<CommunicateMapper, Communicate> implements CommunicateService {
    @Autowired
    private ContactService contactService;
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;
    @Override
    public Result talklist(INTDTO toid1) {
        if(toid1.getId()!=0&&userService.lambdaQuery().eq(User::getId,toid1.getId()).count()==0)return Result.failstr("非法操作");
        if(contactService.lambdaQuery().eq(Contact::getUid,jwtProperties.getUserId().toString()).eq(Contact::getToid,String.valueOf(toid1.getId())).count()==0){
            Contact contact = new Contact();
            Random random = new Random();
            /*int randomNumber = 10000 + random.nextInt(90000);
            contact.setId(randomNumber);*/
            contact.setUid(jwtProperties.getUserId().toString());
            contact.setToid(String.valueOf(toid1.getId()));
            contact.setNum("0");
            contactService.save(contact);
        }else{
            Contact contact = contactService.lambdaQuery().eq(Contact::getUid,jwtProperties.getUserId()).eq(Contact::getToid,String.valueOf(toid1.getId())).one();
            if(!contact.getNum().equals("0")){
                contact.setNum("0");
                contactService.updateById(contact);
            }
        }
        // 创建两个 LambdaQueryWrapper 实例（注意这里应该是查询而不是更新）

        // 合并两个查询条件
        LambdaQueryWrapper<Communicate> combinedWrapper = new QueryWrapper<Communicate>().lambda()
                .or(wrapper -> wrapper.eq(Communicate::getToid, toid1.getId()).eq(Communicate::getUid, jwtProperties.getUserId()))
                .or(wrapper -> wrapper.eq(Communicate::getToid, jwtProperties.getUserId()).eq(Communicate::getUid, toid1.getId()));

        // 按时间顺序排序
        combinedWrapper.orderByAsc(Communicate::getAddtime);

        // 执行查询
        List<CommunicateDTO> list = new ArrayList<>();
        //遍历combinedWrapper
        Long id = jwtProperties.getUserId();
        String  username = "我";
        String Qq = userService.getById(id).getQq();

        String username1;
        String Qq1;
        if(toid1.getId()==0){//客服系统
            username1 = "官方客服";
            Qq1 = "666666";
        }else{
            username1 = userService.getById(toid1.getId()).getUser();
            Qq1 = userService.getById(toid1.getId()).getQq();
        }

        for (Communicate communicate : this.list(combinedWrapper)) {
            CommunicateDTO communicateDTO = new CommunicateDTO();
            if(communicate.getUid()==id){
                communicateDTO.setUsername(username);
                communicateDTO.setAvatar(Qq);
                communicateDTO.setIsSelf(true);
            }else{
                communicateDTO.setUsername(username1);
                communicateDTO.setAvatar(Qq1);
                communicateDTO.setIsSelf(false);
            }
            communicateDTO.setContent(communicate.getContent());
            communicateDTO.setType(communicate.getType());
            list.add(communicateDTO);
        }

        return Result.result(200, username1, 0L, list);
    }

    @Override
    public Result ACtalk(INTDTO toid1) {
        if(userService.lambdaQuery().eq(User::getId,toid1.getId()).count()==0)return Result.failstr("非法操作");
        if(contactService.lambdaQuery().eq(Contact::getUid,"0").eq(Contact::getToid,String.valueOf(toid1.getId())).count()==0){
            Contact contact = new Contact();
            Random random = new Random();
            /*int randomNumber = 10000 + random.nextInt(90000);
            contact.setId(randomNumber);*/
            contact.setUid("0");
            contact.setToid(String.valueOf(toid1.getId()));
            contact.setNum("0");
            contactService.save(contact);
        }else{
            Contact contact = contactService.lambdaQuery().eq(Contact::getUid,"0").eq(Contact::getToid,String.valueOf(toid1.getId())).one();
            if(!contact.getNum().equals("0")){
                contact.setNum("0");
                contactService.updateById(contact);
            }
        }
        // 创建两个 LambdaQueryWrapper 实例（注意这里应该是查询而不是更新）

        // 合并两个查询条件
        LambdaQueryWrapper<Communicate> combinedWrapper = new QueryWrapper<Communicate>().lambda()
                .or(wrapper -> wrapper.eq(Communicate::getToid, toid1.getId()).eq(Communicate::getUid, 0))
                .or(wrapper -> wrapper.eq(Communicate::getToid, 0).eq(Communicate::getUid, toid1.getId()));

        // 按时间顺序排序
        combinedWrapper.orderByAsc(Communicate::getAddtime);

        // 执行查询
        List<CommunicateDTO> list = new ArrayList<>();
        //遍历combinedWrapper
        Long id = 0L;
        String  username = "我";
        String Qq = "666666";

        String username1;
        String Qq1;
        if(toid1.getId()==0){//客服系统
            username1 = "官方客服";
            Qq1 = "666666";
        }else{
            username1 = userService.getById(toid1.getId()).getUser();
            Qq1 = userService.getById(toid1.getId()).getQq();
        }

        for (Communicate communicate : this.list(combinedWrapper)) {
            CommunicateDTO communicateDTO = new CommunicateDTO();
            if(communicate.getUid()==id){
                communicateDTO.setUsername(username);
                communicateDTO.setAvatar(Qq);
                communicateDTO.setIsSelf(true);
            }else{
                communicateDTO.setUsername(username1);
                communicateDTO.setAvatar(Qq1);
                communicateDTO.setIsSelf(false);
            }
            communicateDTO.setContent(communicate.getContent());
            communicateDTO.setType(communicate.getType());
            list.add(communicateDTO);
        }

        return Result.result(200, username1, 0L, list);
    }



    @Override
    public Result talkadd(ImSingleDTO communicate,int xx) {
        if(!(communicate.getUser().equals("官方客服"))&&!communicate.getUser().equals(jwtProperties.getUserName())) return Result.failstr("非法操作");
        Communicate communicate1 = new Communicate();
        int id;
        if(communicate.getTouser().equals("官方客服")){
            id=0;
        }else {
            User user1 = userService.lambdaQuery().eq(User::getUser, communicate.getTouser()).one();
            id = user1.getId();
        }
        int uid;
        if(communicate.getUser().equals("官方客服")){
            uid=0;
        }else{
            uid=jwtProperties.getUserId().intValue();
        }
        communicate1.setToid(id);
        communicate1.setUid(Math.toIntExact(uid));
        communicate1.setContent(communicate.getContent());
        communicate1.setType(communicate.getType());
        communicate1.setAddtime(System.currentTimeMillis());
        this.save(communicate1);
        /*if(xx==1){*/
            if(contactService.lambdaQuery().eq(Contact::getUid,String.valueOf(id)).eq(Contact::getToid,uid).count()==0){
                Contact contact = new Contact();
                Random random = new Random();
                /*int randomNumber = 10000 + random.nextInt(90000);
                contact.setId(randomNumber);*/
                contact.setNum("0");
                contact.setUid(String.valueOf(id));
                contact.setToid(String.valueOf(uid));
                contactService.save(contact);
            }

            if(xx==1){
                Contact contact = contactService.lambdaQuery().eq(Contact::getUid,String.valueOf(id)).eq(Contact::getToid,String.valueOf(uid)).one();
                String num = String.valueOf(Integer.parseInt(contact.getNum())+1);
                contact.setNum(num);
                contactService.updateById(contact);
            }
        /*}*/
        return Result.result(200,"成功",0L,null);

    }






}
