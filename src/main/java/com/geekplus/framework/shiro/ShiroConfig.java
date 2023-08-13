//package com.geekplus.framework.shiro;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.session.SessionListener;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//import java.util.*;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 2022/4/24 8:39 下午
// * description: 做什么的？
// */
//
//@Configuration
//@Slf4j
//public class ShiroConfig {
//
//    @Value("${token.expireTime}")
//    private long expireTime;
//
//    @Bean(name="lifeCycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * @Author xxx
//     * @Description 因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，
//     *              需要告诉它我们用的是md5加密的，并且是加密了多次。
//     *              同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。
//     *              这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
//     * @Date 2018/11/13 10:33
//     * @Param
//     * @Return
//     */
//    @Bean(name = "myRetryLimitCredentialsMatcher")
//    public MyHashedCredentialsMatcher hashedCredentialsMatcher() {
//        MyHashedCredentialsMatcher hashedCredentialsMatcher = new MyHashedCredentialsMatcher();
//        // 采用MD5方式加密
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        // 散列算法，这里使用更安全的sha256算法
//        //credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
//        // 数据库存储的密码字段使用HEX还是BASE64方式加密
//        //credentialsMatcher.setStoredCredentialsHexEncoded(false);
//        // 设置加密次数(散列迭代次数)
//        hashedCredentialsMatcher.setHashIterations(1024);
//        return hashedCredentialsMatcher;
//    }
//
//    //将自己的验证方式加入到容器
//    @Bean
//    @DependsOn("lifeCycleBeanPostProcessor")
//    public ShiroRealm myCustomRealm(){
//        ShiroRealm myCustomRealm=new ShiroRealm();
//        myCustomRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return myCustomRealm;
//    }
//
//    //权限管理，配置主要死Realm的管理认证 DefaultWebSecurityManager也可以
//    @Bean
//    public DefaultWebSecurityManager securityManager(RedisSessionDao redisSessionDao){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myCustomRealm());
//        securityManager.setSessionManager(defaultWebSessionManager(redisSessionDao));
//        securityManager.setRememberMeManager(cookieRememberMeManager()); //注入rememberMeManager;
//        return securityManager;
//    }
//
//    @Bean
//    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDao redisSessionDao) {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setGlobalSessionTimeout(expireTime * 1000);
//        sessionManager.setDeleteInvalidSessions(true);
//        sessionManager.setSessionDAO(redisSessionDao);
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        sessionManager.setDeleteInvalidSessions(true);
//        /**
//         * 修改Cookie中的SessionId的key，默认为JSESSIONID，自定义名称
//         */
//        sessionManager.setSessionIdCookie(new SimpleCookie("JSESSIONID"));
//        //注册自定义Session监听
//        Collection<SessionListener> listeners=new ArrayList<>();
//        listeners.add(new ShiroSessionListener());
//        sessionManager.setSessionListeners(listeners);
//        return sessionManager;
//    }
//
//    public ShiroAuthFilter shiroAuthFilter(){
//        return new ShiroAuthFilter();
//    }
//
//    //对url的过滤筛选和授权
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, String> map = new LinkedHashMap<>();
//        //静态资源的过滤
//        //map.put("/static/**", "anon");
//        map.put("/*.html","anon");
//        map.put("/**/*.html","anon");
//        map.put("/**/*.css","anon");
//        map.put("/**/*.js","anon");
//        //默认的登出
//        //map.put("/logout", "logout");
//        // anon 可以理解为不拦截
//        map.put("/sys/user/login","anon");
//        map.put("/captchaBase64","anon");
//        map.put("/sys/user/logout","anon");
//        map.put("/covid/**","anon");
//        map.put("/openai/**","anon");
//        map.put("/geekplusapp/**","anon");
//        //map.put("/geekplus/websocket/**","anon");
//        map.put("/profile/**","anon");
//        map.put("/common/download**","anon");
//        map.put("/common/download/resource**","anon");
////        map.put("/sys/user/detail","anon");
////        map.put("/sys/user/getMenu","anon");
////        map.put("/sys/menu/getMenu","anon");
//        map.put("/druid/**","anon");
//        //swagger资源过滤
//        map.put("/swagger-resources/**","anon");
//        map.put("/webjars/**","anon");
//        map.put("/*/api-docs","anon");
//        map.put("/swagger-ui.html","anon");
//        map.put("/doc.html","anon");
//        //对所有用户认证
//        map.put("/**", "authc");
//        //authc:所有url必须通过认证才能访问，anon:所有url都可以匿名访问
//        //map.put("/**", "shiroAuthenticationFilter");
//        //登录页面
////        shiroFilterFactoryBean.setLoginUrl("/sys/user/login");
//        // 设置无权限时跳转的url;(错误页面，认证不通过跳转)
////        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/user/unAuth");
//        //成功登录后跳转的url
//        //shiroFilterFactoryBean.setSuccessUrl("/xxxx");
//        // 设置自定义filter
//        //Map<String, Filter> filter = new LinkedHashMap<>();
//        //filter.put("auth", new ShiroAuthFilter());
//        //shiroFilterFactoryBean.setFilters(filter);
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        shiroFilterFactoryBean.getFilters().put("authc",shiroAuthFilter());
//        return shiroFilterFactoryBean;
//    }
//
//    //授权
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    //开启shiro授权注解，若上面Bean未生效则使用此Bean
//    @Bean
//    //@ConditionalOnMissingBean
//    @DependsOn("lifeCycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator defaultAAPC = new DefaultAdvisorAutoProxyCreator();
//        defaultAAPC.setProxyTargetClass(true);
//        defaultAAPC.setUsePrefix(true);
//        return defaultAAPC;
//    }
//
//    // cookie管理对象;
//    @Bean
//    public CookieRememberMeManager cookieRememberMeManager() {
//        log.info("ShiroConfig.rememberMeManager()");
//        CookieRememberMeManager manager = new CookieRememberMeManager();
//        manager.setCookie(rememberMeCookie());
//        return manager;
//    }
//    /**
//     * @Author xxx
//     * @Description cookie对象 管理
//     * @Date 2018/11/13 10:35
//     * @Param
//     * @Return
//     */
//    @Bean
//    public SimpleCookie rememberMeCookie() {
//        log.info("ShiroConfig.rememberMeCookie()");
//        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
//        simpleCookie.setMaxAge(259200);
//        return simpleCookie;
//    }
//
//}
