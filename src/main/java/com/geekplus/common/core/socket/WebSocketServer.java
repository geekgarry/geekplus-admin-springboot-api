/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/13 11:50 下午
 * description: 做什么的？
 */
package com.geekplus.common.core.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private static CopyOnWriteArraySet<String> sessionIdSet=new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        //SysUser sysUser=(SysUser) SecurityUtils.getSubject().getPrincipal();
        this.session = session;
        webSocketSet.add(this);     //加入set中
        this.sid = sid;
        sessionIdSet.add(this.sid);
        addOnlineCount();           //在线数加1
        //sid=sid+":"+System.currentTimeMillis();
        sessionPool.put(sid,session);
        log.info("连接成功！");
        sendMessageAll("onlineCount:"+WebSocketServer.onlineCount);
//        try {
//            //sendMessage("连接成功:"+sysUser.getUserId()+":"+sysUser.getUserName()+":"+sysUser.getNickName());
//            sendMessage("connect_success");
//            log.info("有新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());
//        } catch (IOException e) {
//            log.error("websocket IO Exception");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);  //从set中删除
        sessionIdSet.remove(this.sid);
        subOnlineCount();           //在线数减1
        sendMessageAll("onlineCount:"+WebSocketServer.onlineCount);
        Map<String,Session> webSocketServers = new HashMap<>();
//        if (sessionPool.containsKey(this.sid)) {
//            //webSocketServers.put(sid,sessionPool.get(sid));//.stream().filter(o -> o.session.getId().equals(session.getId())).collect(Collectors.toList());
//            sessionPool.remove(this.sid,this.session);
//        }
        log.info("用户【" + this.sid + "】sessionId:[" + this.session.getId() + "]断开连接" );
//        if (sessionPool.containsKey(sid) && webSocketServers.size() > 0) {
//            webSocketServers.containsKey(sessionPool.get(sid));
//            Set<Map.Entry<String, Session>> entries = webSocketServers.entrySet();
//            Iterator iterator = entries.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Session> next = (Map.Entry<String, Session>) iterator.next();
//                if (next.getValue().getId().equals(session.getId())) {
//                    iterator.remove();
//                }
//            }
//            sessionPool.putAll(webSocketServers);
//            log.info("用户【" + sid + "】sessionId:[" + session.getId() + "]断开连接" );
//        }
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为："+sid);
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Object message) throws IOException {
        this.session.getBasicRemote().sendText(message.toString());
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(Object message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    //*****************************通过Session池类发送消息**********************************
    // 单用户推送
    public static void sendMessage(Session session, Object message) {
        if (session == null) {
            return;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null)
        {
            return;
        }
        try {
            basic.sendText(message.toString());
        } catch (IOException e) {
            System.out.println("sendMessage IOException "+ e);
        }
    }

    // 全用户推送
    public static void sendMessageAll(Object message) {
        sessionPool.forEach((sid,session) -> sendMessage(session, message));
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    public static CopyOnWriteArraySet<String> getSessionIdSet(){ return sessionIdSet; }
    public static Map<String,Session> getSessionPool(){return sessionPool;}
}
