package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Config;
import cn.exitcode.day001.apicontect.entity.Orders;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.*;
import cn.exitcode.day001.apicontect.entity.vo.AdminIndexVO;
import cn.exitcode.day001.apicontect.mapper.ConfigMapper;
import cn.exitcode.day001.apicontect.service.ConfigService;
import cn.exitcode.day001.apicontect.service.OrdersService;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public List<HashMap<String, Object>> getAllConfig() {
        List<Config> configList = this.list();
        List<HashMap<String, Object>> configList1 = configList.stream().map(config -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("k", config.getK());
            map.put("v", config.getV());
            return map;
        }).toList();
        return configList1;
    }
    @Override
    public String getValueByKey(String key) {
        List<HashMap<String, Object> > configList = getAllConfig();
        for (HashMap<String, Object> config : configList) {
            if (config.get("k").equals(key)) {
                return config.get("v").toString();
            }
        }
        return null;
    }

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public boolean updateValueByKey(String key, String value) {
        Config config = configMapper.selectOne(new QueryWrapper<Config>().eq("k", key));
        if (config != null) {
            config.setV(value);
            return configMapper.updateById(config) > 0;
        }
        return false;
    }

    @Override
    public boolean Login(LoginDTO res) {
        if(res.getUser().equals(getValueByKey( "user"))&& res.getPwd().equals(getValueByKey( "pwd"))){
            System.out.println("登录成功");
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Result setPwd(PwdDTO res) {
        if(res.getPwd()==null||res.getUser()==null)return Result.failstr("请输入完整");
        if(this.updateValueByKey("pwd",res.getPwd())&&this.updateValueByKey("user",res.getUser())){
            return Result.result(200, "修改成功", 0L,null);
        }else{
            return Result.failstr("修改失败");
        }
    }
    @Override
    public Result setApi(ApiDTO res){
        if(res.getApi()==null||res.getAppcode()==null||res.getKey()==null||res.getMail()==null||res.getPort()==null||res.getSmtp()==null)return Result.failstr("请输入完整");
        this.updateValueByKey("api",res.getApi());
        this.updateValueByKey("appcode",res.getAppcode());
        this.updateValueByKey("key",res.getKey());
        this.updateValueByKey("mail",res.getMail());
        this.updateValueByKey("port",res.getPort());
        this.updateValueByKey("smtp",res.getSmtp());
        return Result.result(200, "修改成功", 0L,null);
    }
    @Override
    public Result cxApi() {
        ApiDTO apiDTO = new ApiDTO();
        apiDTO.setApi(this.getValueByKey("api"));
        apiDTO.setAppcode(this.getValueByKey("appcode"));
        apiDTO.setKey(this.getValueByKey("key"));
        apiDTO.setMail(this.getValueByKey("mail"));
        apiDTO.setPort(this.getValueByKey("port"));
        apiDTO.setSmtp(this.getValueByKey("smtp"));
        return Result.result(200, "查询成功", 0L,apiDTO);
    }

    @Override
    public Result setConfig(WebConfigDTO res){
        if(res.getTitle()==""||res.getSitename()==""||res.getDescription()==""||res.getKeywords()==""||res.getGonggao()=="")return Result.failstr("请输入完整");
        this.updateValueByKey("title",res.getTitle());
        this.updateValueByKey("sitename",res.getSitename());
        this.updateValueByKey("description",res.getDescription());
        this.updateValueByKey("keywords",res.getKeywords());
        this.updateValueByKey("gonggao",res.getGonggao());
        return Result.result(200, "修改成功", 0L,null);
    }
    @Override
    public Result getConfig() {
        AdminIndexVO adminIndexVO = new AdminIndexVO();
        adminIndexVO.setUserCount(userService.count());
        adminIndexVO.setProjectCount(projectService.count());
        adminIndexVO.setOrderCount(ordersService.count());
        adminIndexVO.setUserList(userService.lambdaQuery().eq(User::getRoleid,-2).list());
        adminIndexVO.setUser(this.getValueByKey("user"));
        return Result.result(200, "获取成功", 0L,adminIndexVO);
    }
    @Override
    public Result cxConfig() {
        WebConfigDTO webConfigDTO=new WebConfigDTO();
        webConfigDTO.setDescription(this.getValueByKey("description"));
        webConfigDTO.setGonggao(this.getValueByKey("gonggao"));
        webConfigDTO.setKeywords(this.getValueByKey("keywords"));
        webConfigDTO.setTitle(this.getValueByKey("title"));
        webConfigDTO.setSitename(this.getValueByKey("sitename"));
        return Result.result(200, "查询成功", 0L,webConfigDTO);
    }

    @Override
    public Result checkinfo(CheckInfoDTO user){
        User user1 = userService.lambdaQuery().eq(User::getId,jwtProperties.getUserId()).one();
        if(user1.getRoleid()!=-1)return Result.failstr("您已通过审核或者正在审核Ing！");


        String host = this.getValueByKey("api");
        String path = "/verify/identity";
        String method = "POST";
        String appcode = this.getValueByKey("appcode");
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("name", user.getName());
        bodys.put("id_number", user.getId_card());
        String card = user.getName()+"||"+user.getId_card();
        //请求发送
        try {
            // 发送请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(host + path))
                    .header("Authorization", "APPCODE " + appcode)
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString("name=" + user.getName() + "&id_number=" + user.getId_card()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response.body(), Map.class);
            System.out.println(jsonResponse);
            int chargecode = (int) jsonResponse.get("charge");
            if(chargecode !=1)return Result.result(400,"身份信息错误",null,null);
            Map<String, Object> resultJson = (Map<String, Object>) jsonResponse.get("result");
            int checkResult = (int) resultJson.get("checkresult");

            if (user.getRole_id() == 0) {
                if (checkResult == 1) {
                    user1.setRoleid(user.getRole_id());
                    user1.setState("0");
                    user1.setCard(card);
                    userService.updateById(user1);
                    return Result.result(200, "成功", null, null);
                } else {
                    return Result.result(400, "身份信息错误", null, null);
                }
            } else {
                if (checkResult == 1) {
                    user1.setRoleid(-2);
                    user1.setState("0");
                    user1.setBzmoney((float) user.getMoney());
                    user1.setCard(card);
                    userService.updateById(user1);

                    Orders orders = new Orders();
                    orders.setName(user.getName()+"雇主保证金"+user.getMoney()+"元");

                    orders.setAmount(String.valueOf(user.getMoney()));
                    orders.setOrderid(UUID.randomUUID().toString());
                    orders.setStatus(0);
                    orders.setTwoid(String.valueOf(user1.getId()));
                    orders.setAddtime(LocalDateTime.now());
                    ordersService.save(orders);

                    return Result.result(201, "提交成功，等待邮箱通知审核成功", null, null);
                } else {
                    return Result.result(400, "身份信息错误", null, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.result(400, "失败", null, null);
        }
    }

}
