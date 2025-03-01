package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.mapper.ContactMapper;
import cn.exitcode.day001.apicontect.service.ContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {
    @Autowired
    private JwtProperties jw;
    @Override
    public Result delContact(INTDTO contact) {
        Contact contact1 =this.lambdaQuery().eq(Contact::getId,contact.getId()).one();
        if(contact1==null)return Result.failstr("不存在");
        if(!contact1.getUid().equals(String.valueOf(jw.getUserId())))return Result.failstr("无权限");
        this.removeById(contact.getId());
        return Result.result(200,"成功",0L,null);
    }

}
