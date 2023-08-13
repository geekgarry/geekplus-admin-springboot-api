package com.geekplus.framework.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/30 5:05 下午
 * description: 做什么的？
 */
@Configuration
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {

    // 这里使用的是Ehcache对密码重试次数进行缓存
    private Cache<String, AtomicInteger> passwordRetryCache;

//    public MyHashedCredentialsMatcher(CacheManager cacheManager) {
//        passwordRetryCache = (Cache<String, AtomicInteger>) cacheManager.getCache("passwordRetryCache");
//    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //CustomToken tk = (CustomToken) token;
//        if(tk.getType().equals(LoginType.NOPASSWORD)){
//            return true;
//        }
//        String username = (String) authcToken.getPrincipal();
//        AtomicInteger retryCount = passwordRetryCache.get(username);
//        if (retryCount == null) {
//            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(username, retryCount);
//        }
//        // 当用户连续输入密码错误5次以上禁止用户登录一段时间
//        if (retryCount.incrementAndGet() > 5) {
//            throw new ExcessiveAttemptsException();
//        }
//        boolean matches = super.doCredentialsMatch(token, info);
//        if (matches) {
//            passwordRetryCache.remove(username);
//        }
        boolean matches = super.doCredentialsMatch(token, info);
        return matches;
    }
}
