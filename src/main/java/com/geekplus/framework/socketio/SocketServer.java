package com.geekplus.framework.socketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @author: geekgarry
 * @date: 8/7 15:14
 * @description:
 */
public class SocketServer {

    public static void main(String[] args) throws InterruptedException {

        Configuration configuration = new Configuration();

        configuration.setHostname("localhost");
        configuration.setPort(8080);
        //configuration.setTransports(Transport.WEBSOCKET, Transport.FLASHSOCKET,Transport.XHRPOLLING);

        SocketIOServer server = new SocketIOServer(configuration);
        ChatEventListener listener = new ChatEventListener();

        listener.setServer(server);
        server.addEventListener("chatEvent", ChatObject.class, listener);

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
