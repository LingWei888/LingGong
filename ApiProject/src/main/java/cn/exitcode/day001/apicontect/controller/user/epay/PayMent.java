package cn.exitcode.day001.apicontect.controller.user.epay;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Orders;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.RechargeDTO;
import cn.exitcode.day001.apicontect.service.OrdersService;
import cn.exitcode.day001.apicontect.service.UserService;
import cn.exitcode.day001.apicontect.utils.epay.SignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RestController
public class PayMent {

    @Autowired
    private SignatureUtil signatureUtil;
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersService ordersService;

    private String url;
    private String pid;



    private String baseUrl;
    private String qianduanUrl;

    public PayMent(@Value("${epay.url}") String url,@Value("${epay.pid}") String pid,@Value("${site.url}") String baseUrl,@Value("${site.qianduan.url}") String qianduanUrl){
        this.pid = pid;
        this.url = url;
        this.baseUrl=baseUrl;
        this.qianduanUrl =qianduanUrl;
    }


    @RequestMapping("/User/payment")
    public Result payment(@RequestBody RechargeDTO rechargeDTO) {
        if(rechargeDTO.getMoney()<=0)return Result.result(400, "充值金额不能为0以下的数", null, null);
        if(!rechargeDTO.getUser().equals(jwtProperties.getUserName()))return Result.result(400, "非法操作", null, null);
        if(!rechargeDTO.getType().equals("alipay")&&!rechargeDTO.getType().equals("wxpay")&&!rechargeDTO.getType().equals("qqpay")) return Result.result(400, "非法操作", null, null);

        String out_trade_no = String.valueOf(System.currentTimeMillis());
        Map<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(this.pid));
        params.put("type", rechargeDTO.getType());
        params.put("out_trade_no", out_trade_no);
        params.put("notify_url", baseUrl+"/payment/notify");
        params.put("return_url", qianduanUrl+"/");//前端网站
        params.put("name", "充值金额"+rechargeDTO.getMoney());
        params.put("money", String.valueOf(rechargeDTO.getMoney()));
        params.put("sign", signatureUtil.getSign(params));

        StringBuilder urlBuilder = new StringBuilder(this.url+"submit.php?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 去掉最后一个 '&' 字符
        if (urlBuilder.length() > 0) {
            urlBuilder.setLength(urlBuilder.length() - 1);
        }
        //String sign = params.get("sign");
        Orders orders = new Orders();
        orders.setOrderid(out_trade_no);
        orders.setName( rechargeDTO.getUser()+"充值金额"+rechargeDTO.getMoney());
        orders.setAmount(String.valueOf(rechargeDTO.getMoney()));
        orders.setStatus(0);
        orders.setAddtime( LocalDateTime.now());
        orders.setTwoid(String.valueOf(jwtProperties.getUserId()));
        ordersService.save(orders);

        return Result.result(200, "请求成功", null, urlBuilder);
    }


    @RequestMapping("/payment/notify")
    public Result notify(@RequestParam Map<String, String> params) {
        String sign = params.get("sign");
        params.remove("sign");
        String sign2 = signatureUtil.getSign(params);
        if (sign.equals(sign2)) {
            Orders orders=ordersService.lambdaQuery().eq(Orders::getOrderid,params.get("out_trade_no")).one();
            if(orders!=null){
                if(orders.getStatus()==9) return Result.failstr("已经处理过了，请勿重复操作！");
                orders.setStatus(9);
                ordersService.updateById(orders);
            }
            User user = userService.lambdaQuery().eq(User::getId, orders.getTwoid()).one();
            if(user!=null){
                user.setBzmoney( (user.getBzmoney()+Float.parseFloat(orders.getAmount())));
                userService.updateById(user);
            }

            return Result.result(200, "请求成功", null, null);
        } else {
            return Result.result(400, "请求失败",null, null);
        }
    }

}
