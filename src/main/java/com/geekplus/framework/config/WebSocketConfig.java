/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/13 11:48 下午
 * description: 做什么的？
 */
package com.geekplus.framework.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

//WebSocket相关配置
@Configuration
public class WebSocketConfig implements ServletContextInitializer {

    /**
     * 如果直接使用springboot的内置容器，而不是使用独立的servlet容器，就要注入ServerEndpointExporter，外部容器则不需要。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }
}
