package com.geekplus.framework.jwtshiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.CorsFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 8:39 下午
 * description: 做什么的？
 */

@Configuration
@Slf4j
public class JwtShiroConfig {

    @Value("${token.expireTime}")
    private long expireTime;

    //将自己的验证方式加入到容器
    @Bean
    @DependsOn("lifeCycleBeanPostProcessor")
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }

    /**
     * 跨域过滤器
     */
    @Autowired
    private FilterRegistrationBean corsFilter;

    //权限管理，配置主要死Realm的管理认证 DefaultWebSecurityManager也可以
    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm());
        // 关闭session
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        return securityManager;
    }

    //对url的过滤筛选和授权
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.getFilters().put("jwt",jwtFilter());
        Map<String, String> map = new LinkedHashMap<>();
        //静态资源的过滤
        //map.put("/*.html","anon");
        //map.put("/**/*.html","anon");
        map.put("/**/*.css","anon");
        map.put("/**/*.js","anon");
        //默认的登出
        //map.put("/logout", "logout");
        // anon 可以理解为不拦截
        map.put("/sys/user/login","anon");
        map.put("/captchaBase64","anon");
        map.put("/sys/user/logout","anon");
        map.put("/covid/**","anon");
        map.put("/openai/**","anon");
        map.put("/geekplusapp/**","anon");
        //map.put("/geekplus/websocket/**","anon");
        map.put("/profile/**","anon");
        map.put("/common/getQRCode**","anon");
        map.put("/common/download**","anon");
        map.put("/common/download/resource**","anon");
        map.put("/csrf","anon");
        map.put("/druid/**","anon");
        //swagger资源过滤
        map.put("/swagger-resources/**","anon");
        map.put("/webjars/**","anon");
        map.put("/*/api-docs","anon");
        map.put("/swagger-ui.html","anon");
        map.put("/doc.html","anon");
        //对所有用户认证
        map.put("/**", "jwt");
        //authc:所有url必须通过认证才能访问，anon:所有url都可以匿名访问
        //map.put("/**", "shiroAuthenticationFilter");
        //登录页面
//        shiroFilterFactoryBean.setLoginUrl("/sys/user/login");
        // 设置无权限时跳转的url;(错误页面，认证不通过跳转)
//        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/user/unAuth");
        //成功登录后跳转的url
        //shiroFilterFactoryBean.setSuccessUrl("/xxxx");
        // 设置自定义filter
        //Map<String, Filter> filter = new LinkedHashMap<>();
        //filter.put("auth", new ShiroAuthFilter());
        //shiroFilterFactoryBean.setFilters(filter);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //jwt过滤器
    public JwtFilter jwtFilter(){
        return new JwtFilter();
    }

    //授权
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //开启shiro授权注解，若上面Bean未生效则使用此Bean
    @Bean
    //@ConditionalOnMissingBean
    @DependsOn("lifeCycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAAPC = new DefaultAdvisorAutoProxyCreator();
        defaultAAPC.setProxyTargetClass(true);
        //defaultAAPC.setUsePrefix(true);
        return defaultAAPC;
    }

    @Bean(name="lifeCycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    // cookie管理对象;
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        log.info("ShiroConfig.rememberMeManager()");
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        return manager;
    }
    /**
     * @Author xxx
     * @Description cookie对象 管理
     * @Date 2018/11/13 10:35
     * @Param
     * @Return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        log.info("ShiroConfig.rememberMeCookie()");
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

}
