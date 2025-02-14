/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/20 7:32 下午
 * description: 做什么的？
 */
package com.geekplus.common.core.session;

import com.geekplus.webapp.system.entity.SysUser;

import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//通过session统计在线用户
public class OnlineUserListener implements HttpSessionAttributeListener, HttpSessionActivationListener, HttpSessionListener {
    //在线哟过湖列表
    private static List<SysUser> onlineUsers=new ArrayList<>();

    /**
     * 获取在线用户列表
     * @return
     */
    public static List getOnlineUsers(){
        return onlineUsers;
    }

    /*
     * session 属性添加时（即首次执行setAttribute时）触发
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if(event.getSession()==null){
            return;
        }
        // 当执行setAttribute("logonUserView",logonUserView)时
        // 将当前用户添加到在线用户列表中
        if(event.getName().equalsIgnoreCase("userName")){
            // 获取当前登陆用户信息及session
            String userName = (String)event.getValue();
            HttpSession session = event.getSession();

            // 创建在线用户信息对象
            SysUser onlineUser = new SysUser();
            onlineUser.setCreateTime(new Date());
            //onlineUser.setUserId(user.getUserfullid());
            onlineUser.setUsername(userName);
            //onlineUser.setStatus(session);

            // 添加到在线用户列表
            onlineUsers.add(onlineUser);
        }
    }

    /*
     * session 属性移除时（即执行removeAttribute时）触发
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
    }

    /*
     * session 属性更新时（即再次执行setAttribute时）触发
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if(event.getSession()==null){
            return;
        }

        // 当执行再次执行setAttribute("logonUserView")时，发生用户替换
        if(event.getName().equalsIgnoreCase("userName")){
            // 获取被替换用户信息及session
            String userName = (String)event.getValue();
            // 更新在线用户信息
            HttpSession session = event.getSession();
            // 获取当前登陆用户信息
            String newUser = (String)session.getAttribute("userName");
            // 循环查找被替换用户在线信息
            for(int i=0;i<onlineUsers.size();i++){
                SysUser onlineUserView = (SysUser) onlineUsers.get(i);
                if(onlineUserView.getUsername().equals(userName)){
                    // 更新在线用户信息
                    onlineUserView.setCreateTime(new Date());
                    //onlineUserView.setUserId(newUser.getUserfullid());
                    onlineUserView.setUsername(newUser);
                    break;
                }
            }
        }
    }

    /*
     * session 创建时触发
     */
    public void sessionCreated(HttpSessionEvent event) {
    }

    /*
     * session 销毁时（即用户注销、session超时时）触发
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        if(event.getSession()==null){
            System.out.println("OnlineUserListener.sessionDestroyed() session==null");
            return;
        }
        // 获取当前登陆用户信息及session
        HttpSession session = event.getSession();
        for(int i=0; i<onlineUsers.size();i++){
            SysUser onlineUserView = (SysUser) onlineUsers.get(i);
            if(onlineUserView!=null && onlineUserView.getUsername()!=null && session!=null
                    && onlineUserView.getUsername().equals(session.getId())){
                // 从在线用户列表移除用户
                onlineUsers.remove(onlineUserView);
                break;
            }
        }
    }
}
