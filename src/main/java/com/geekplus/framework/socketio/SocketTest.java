package com.geekplus.framework.socketio;

/**
 * @author: geekgarry
 * @date: 8/8 09:09
 * @description:
 */
public class SocketTest {

    public static void main(String[] args) {
        System.out.println("给全部人员发送消息");
        Socketio socketio = new Socketio();
        socketio.pushArr("connect_msg", "今天下午2点开会");
    }
}
