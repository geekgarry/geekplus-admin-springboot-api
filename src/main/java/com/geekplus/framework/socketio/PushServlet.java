package com.geekplus.framework.socketio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: geekgarry
 * @date: 8/8 10:04
 * @description:
 */
@WebServlet(name = "PushServlet",urlPatterns = "/push")
public class PushServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("给全部人员发送消息");
        Socketio socketio = new Socketio();
        socketio.pushArr("connect_msg", "今天下午2点开会");
    }
}
