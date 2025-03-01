package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Introduce;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
public interface IntroduceService extends IService<Introduce> {

    Result MyIntroduce(QueryPageParam param);

    Result addIntroduce(Introduce introduce);

    Result updateIntroduce(Introduce introduce);

    Result deleteIntroduce(Introduce introduce);
}
