//package com.geekplus.framework.shiro;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.session.SessionListenerAdapter;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.annotation.WebListener;
//import java.util.concurrent.CopyOnWriteArraySet;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 2022/6/20 8:16 下午
// * description: 做什么的？
// */
////Shiro的session监听方案
//@Component
//@WebListener
//@Slf4j
//public class ShiroSessionListener extends SessionListenerAdapter {
//
//    private final CopyOnWriteArraySet<Session> atomSession=new CopyOnWriteArraySet<>();
//
//    @Override
//    public void onStart(Session session) {
//        log.info("************会话创建，在线用户+1***************");
//        atomSession.add(session);
//    }
//
//    @Override
//    public void onStop(Session session) {
//        log.info("************会话停止，在线用户-1***************");
//        atomSession.remove(session);
//    }
//
//    @Override
//    public void onExpiration(Session session) {
//        log.info("************会话过期，在线用户-1***************");
//        String userName=(String) session.getAttribute("userName");
//        String userId=(String) session.getAttribute("userId");
//        if(userName!=null&&userId!=null){
//            //TaskMonitor taskMonitor=new TaskMonitor();
//            atomSession.remove(session);
//        }
//    }
//
//    public CopyOnWriteArraySet<Session> getAtomSession(){
//        return atomSession;
//    }
//}
