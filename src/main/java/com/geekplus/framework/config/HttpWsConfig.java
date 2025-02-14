package com.geekplus.framework.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  * @Author geekplus
  * @Description //开启两个端口分别为http和https，可以自定义http端口，http请求强转为https，创建websocket协议等配置
  * @Param
  * @Throws
  * @Return {@link }
  */
@Configuration
public class HttpWsConfig {

    //@Value("${myhttp.port}")
    private Integer myHttpPort=4002;

    //SpringBoot 2.x版本(以及更高版本) 使用下面的代码
    @Bean
    public ServletWebServerFactory servletContainer() {
        //创建Tomcat服务器工厂实例
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        //域名ssl验证,将http请求转换为https请求
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
        //添加此tomcat实例其它连接参数
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        //Connector port有两种运行模式(NIO和APR)，选择NIO模式：protocol="org.apache.coyote.http11.Http11NioProtocol"
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //启用http（80）端口
        connector.setScheme("http");
        //设置安全连接标志，该标志将被分配给通过该连接接收的请求
        //secure新的安全连接标志
        //如果connector.setSecure(true)，则http使用http, https使用https; 分离状态，因此设置false
        connector.setSecure(false);
        //http默认端口
        connector.setPort(myHttpPort);
        //http重定向为https
        //重定向证书端口443，便于http自动跳转https
        //connector.setRedirectPort(8443);
        return connector;
    }

    /**
     * 创建wss协议接口
     *
     * @return
     */
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        System.out.println("websocket init");
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                System.out.println("init customize");
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }
}
