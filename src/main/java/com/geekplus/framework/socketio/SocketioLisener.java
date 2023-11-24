package com.geekplus.framework.socketio;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author: geekgarry
 * @date: 8/8 09:06
 * @description:
 */
public class SocketioLisener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //启动Socketio服务
        Socketio socketio = new Socketio();
        socketio.startServer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //关闭Socketio服务
        Socketio socketio = new Socketio();
        socketio.stopSocketio();
    }

}
