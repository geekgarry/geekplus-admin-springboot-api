package com.geekplus.framework.netty;

import com.geekplus.common.util.netty.NettyUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.framework.domain.UserInfo;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 10/13/23 23:44
 * description: 做什么的？
 */
public class UserInfoManager {

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();
    /**
     * 登录注册 channel
     */
    public static void addChannel(Channel channel, String uid) {
        String remoteAddr = NettyUtil.parseChannelRemoteAddr(channel);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(uid);
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfos.put(channel, userInfo);
    }
    public static void removeChannel(Channel channel){
        userInfos.remove(channel);
        System.out.println("移除channel成功");
    }

    /**
     * 普通消息
     * @param message
     */
    public static void broadcastMess(String sender,String recevier,String message) {
        if (!StringUtils.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                Set<Channel> keySet = userInfos.keySet();
                for (Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    if (!userInfo.getUserId().equals(recevier)) {
                        continue;
                    }
                    String backmessage=sender+":"+message;
                    ch.writeAndFlush(new TextWebSocketFrame(backmessage));
                    System.out.println("客户端收到消息:"+backmessage);
                    /*responseToClient(ch,message);*/
                }
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public static UserInfo getUserInfo(Channel channel) {
        return userInfos.get(channel);
    }
}
