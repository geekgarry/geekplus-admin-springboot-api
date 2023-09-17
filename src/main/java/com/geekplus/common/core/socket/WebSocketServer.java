/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/13 11:50 下午
 * description: 做什么的？
 */
package com.geekplus.common.core.socket;

import com.geekplus.common.util.json.JsonObjectUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint(value = "/websocket/{sid}", subprotocols = {"protocol"})
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
//    private static CopyOnWriteArraySet<String> sessionIdSet=new CopyOnWriteArraySet<>();
//    private static Map<String,Session> sessionPool = new HashMap<>();

    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static ConcurrentHashMap<String, WebSocketServer> socketClients = new ConcurrentHashMap<String, WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = null;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        //SysUser sysUser=(SysUser) SecurityUtils.getSubject().getPrincipal();
        this.session = session;
        this.sid = sid;
        //webSocketSet.add(this);     //加入set中
        //sessionIdSet.add(this.sid);
        //sessionPool.put(sid,session);
        if(socketClients.containsKey(sid)){
            //必须显示关闭，否则Map里没有了但是sesseion还能连接
            try {
                socketClients.get(sid).session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketClients.remove(sid);
            socketClients.put(sid,this);
        }
        socketClients.put(sid,this);
        addOnlineCount();           //在线数加1
        OnlineCount.incrementAndGet(); //原子加一
        //sid=sid+":"+System.currentTimeMillis();
        log.info("用户连接:"+sid+",当前在线人数为:" + getOnlineCount());
        HashMap<String,Object> map=new HashMap<>();
        map.put("onlineCount",WebSocketServer.onlineCount);
        map.put("type","online");
        map.put("userId",sid);
        sendMessageAll(JsonObjectUtil.objectToJson(map));

//        HashMap<String,Object> mapToUser= Maps.newHashMap();
//        //移除掉自己
//        Set<String> set = socketClients.keySet();
//        set.remove(sid);
//        mapToUser.put("onlineUser",set);
//        mapToUser.put("type","onlineUser");
//        sendInfo(mapToUser,sid);
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
    public void onClose() {
        //webSocketSet.remove(this);  //从set中删除
        //sessionIdSet.remove(this.sid);
        //循环移除
        //socketClients.forEach((sid,webSocket) -> {});
        if(socketClients.containsKey(this.sid)){
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketClients.remove(this.sid);
            //从set中删除
            subOnlineCount();
            OnlineCount.decrementAndGet(); //原子减一
        }
        //subOnlineCount();           //在线数减1
        //OnlineCount.decrementAndGet(); //原子减一
        //单个移除，可能会出现遗留多个没有移除完
        //socketClients.remove(this.sid);
        //断开连接情况下，更新主板占用情况为释放
        log.info("当前在线用户列表："+socketClients.keySet());
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        HashMap<String,Object> map=new HashMap<>();
        map.put("onlineCount",WebSocketServer.onlineCount);
        map.put("type","offline");
        map.put("userId",this.sid);
        sendMessageAll(JsonObjectUtil.objectToJson(map));
        //Map<String,Session> webSocketServers = new HashMap<>();
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
    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 1048576)
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            item.sendMessage(message);
//        }
        Map jsonObject = JsonObjectUtil.jsonToMap(message);
        String textMessage = jsonObject.get("message").toString();
        String msgType = jsonObject.get("type").toString();
        String fromUser = jsonObject.get("fromUser").toString();
        String toUser = jsonObject.get("toUser").toString();
        //如果不是发给所有，那么就发给某一个人
        //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
        Map<String,Object> map1 = Maps.newHashMap();
        map1.put("type",msgType);
        map1.put("message",textMessage);
        map1.put("fromUser",toUser);
        if(msgType.equals("heartBeat")){
            //map1.put("toUser",toUser);
            sendInfo(JsonObjectUtil.objectToJson(map1),toUser);
        }else{
            map1.put("toUser","all");
            sendMessageAll(JsonObjectUtil.objectToJson(map1));
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
     * 群发自定义消息
     */
    public static void sendInfo(Object message, String sid) {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);

        for (WebSocketServer item : socketClients.values()) {
            //这里可以设定只推送给这个sid的，为null则全部推送
            if (sid == null) {
                item.sendMessageAll(message);
            } else if (item.sid.equals(sid)) {
                item.sendMessage(item.session,message);
            }
        }
    }

    //*****************************通过Session池类发送消息**********************************
    /**
     * 单用户推送
     * 实现服务器主动推送
     */
    public static void sendMessage(Session session, Object message) {
        log.info("推送消息" + "，推送内容:" + message);
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

    /**
     * 全用户推送
     */
    public static void sendMessageAll(Object message) {
//        for (WebSocketServer item : socketClients.values()) {
//            item.session.getAsyncRemote().sendText(message.toString());
//        }
        socketClients.forEach((sid,socketItem) -> sendMessage(socketItem.session, message));
    }

    public static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static Map<String,WebSocketServer> getWebSocketPool(){
        return socketClients;
    }

//    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() { return webSocketSet; }
//
//    public static CopyOnWriteArraySet<String> getSessionIdSet(){ return sessionIdSet; }
//
//    public static Map<String,Session> getSessionPool(){return sessionPool;}
}
