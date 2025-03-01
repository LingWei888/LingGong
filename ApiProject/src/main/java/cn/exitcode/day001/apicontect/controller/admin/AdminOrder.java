package cn.exitcode.day001.apicontect.controller.admin;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Orders;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.service.OrdersService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Admin")
public class AdminOrder {
    @Autowired
    private OrdersService ordersService;
    @RequestMapping("/orderList")
    public Result list(@RequestBody QueryPageParam param){
        return ordersService.orderList(param);
    }

    @RequestMapping("/orderDelete")
    public Result delete(@RequestBody Project project){
        return Result.result(200,"成功",0L,ordersService.removeById(project.getId()));
    }
}
