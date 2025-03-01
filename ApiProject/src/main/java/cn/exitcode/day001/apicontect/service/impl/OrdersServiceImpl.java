package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Orders;
import cn.exitcode.day001.apicontect.mapper.OrderMapper;
import cn.exitcode.day001.apicontect.service.OrdersService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class OrdersServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrdersService {
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public Result orderList(QueryPageParam param) {
        Page<Orders> page = new Page<>(param.getPageNum(),param.getPageSize());
        String orderid = param.getKeyword();
        IPage res;
        if(orderid !=null){
            res=this.lambdaQuery().like(Orders::getOrderid,orderid).page(page);
        }else{
            res=this.page(page);
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }
}
