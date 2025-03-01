package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
public interface ContactService extends IService<Contact> {

    Result delContact(INTDTO contact);
}
