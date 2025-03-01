package cn.exitcode.day001.apicontect.common.WebSocket;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtClaimsConstant;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.JwtToken.JwtUtil;
import cn.exitcode.day001.apicontect.entity.Contact;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.CommunicateDTO;
import cn.exitcode.day001.apicontect.entity.dto.ImSingleDTO;
import cn.exitcode.day001.apicontect.service.CommunicateService;
import cn.exitcode.day001.apicontect.service.UserService;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

/**
 * WebSocket服务
 */
@ServerEndpoint("/ws/{token}/{user}")
@Component
public class WebSocketServer{


    private static JwtProperties jwtProperties;
    private static UserService userService;
    private static CommunicateService communicateService;
    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties, UserService userService, CommunicateService communicateService) {
        WebSocketServer.jwtProperties = jwtProperties;
        WebSocketServer.userService = userService;
        WebSocketServer.communicateService = communicateService;
    }


    // 存放会话对象
    private static Map<String, Session> sessionMap = new HashMap<>();



    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token, @PathParam("user") String user) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        if(user.equals("官方客服")&&CheckACToken(token)){
            sessionMap.put(user, session);
            System.out.println("官方客服建立连接");
        }else {

            if (CheckToken(token) && user.equals(jwtProperties.getUserName())) {
                //String user = jwtProperties.getUserName();
                System.out.println("客户端：" + user + "建立连接");
                sessionMap.put(user, session);
            } else {
                System.out.println("token验证失败");
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("token") String token, @PathParam("user") String user) {
        ImSingleDTO imSingleDTO = JSON.parseObject(message, ImSingleDTO.class);
        if(CheckToken(token)&&user.equals(jwtProperties.getUserName())) {
            //{"uid":"1","acid":"2","content":"556","type":"text","time":1739113477588}对数据分析
            User user1 =userService.lambdaQuery().eq(User::getUser,imSingleDTO.getUser()).one();
            CommunicateDTO communicateDTO = new CommunicateDTO();
            communicateDTO.setUsername(user);
            communicateDTO.setContent(imSingleDTO.getContent());
            communicateDTO.setType(imSingleDTO.getType());
            communicateDTO.setIsSelf(false);
            communicateDTO.setAvatar(user1.getQq());
            String message1 = JSON.toJSONString(communicateDTO);

            if (sessionMap.get(imSingleDTO.getTouser()) != null) {
                sessionMap.get(imSingleDTO.getTouser()).getAsyncRemote().sendText(message1);
                System.out.println(communicateService.talkadd(imSingleDTO,0));

            }else{
                    //发送给对方消息数+1
                    System.out.println(communicateService.talkadd(imSingleDTO,1));

            }

            System.out.println("收到来自客户端：" + user + "的信息:" + message);
        }else if(CheckACToken(token)&&user.equals("官方客服")){
            CommunicateDTO communicateDTO = new CommunicateDTO();
            communicateDTO.setUsername(user);
            communicateDTO.setContent(imSingleDTO.getContent());
            communicateDTO.setType(imSingleDTO.getType());
            communicateDTO.setIsSelf(false);
            communicateDTO.setAvatar("666666");
            String message1 = JSON.toJSONString(communicateDTO);
            if (sessionMap.get(imSingleDTO.getTouser()) != null) {
                sessionMap.get(imSingleDTO.getTouser()).getAsyncRemote().sendText(message1);
                System.out.println(communicateService.talkadd(imSingleDTO,0));
            }else{
                //发送给对方消息数+1
                System.out.println(communicateService.talkadd(imSingleDTO,1));
            }
            System.out.println("收到来自客服客户端：" + user + "的信息:" + message);
        }else{
            System.out.println("token验证失败");
        }
    }

    /**
     * 连接关闭调用的方法
     *
     * @param
     */
    @OnClose
    public void onClose(@PathParam("user") String user) {
        System.out.println("连接断开:" + user);
        sessionMap.remove(user);
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public boolean CheckToken(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.LOGIN_ID).toString());
            Long roleid = Long.valueOf(claims.get(JwtClaimsConstant.roleid).toString());
            String userName = claims.get(JwtClaimsConstant.USERNAME).toString();
            jwtProperties.setUserId(empId);
            jwtProperties.setRoleId(roleid);
            jwtProperties.setUserName(userName);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过
            return false;
        }
    }

    public boolean CheckACToken(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);


            return true;
        } catch (Exception ex) {
            //4、不通过
            return false;
        }
    }





}
