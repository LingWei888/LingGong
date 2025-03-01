package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
public interface OrdersService extends IService<Orders> {

    Result orderList(QueryPageParam param);

}
