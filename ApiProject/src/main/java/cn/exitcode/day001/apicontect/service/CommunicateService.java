package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Communicate;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;
import cn.exitcode.day001.apicontect.entity.dto.ImSingleDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */

public interface CommunicateService extends IService<Communicate> {

    Result talklist(INTDTO toid1);

    Result talkadd(ImSingleDTO communicate,int xx);


    Result ACtalk(INTDTO id);
}
