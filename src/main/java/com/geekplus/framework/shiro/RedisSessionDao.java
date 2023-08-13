//package com.geekplus.framework.shiro;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.session.UnknownSessionException;
//import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.concurrent.TimeUnit;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 2022/4/30 4:32 下午
// * description: 做什么的？
// */
//@Slf4j
//@Component
//public class RedisSessionDao extends AbstractSessionDAO {
//
//    @Value("${token.expireTime}")
//    private long expireTime;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
////    private RedisCache cache;
////
////    @Autowired
////    private RedisCacheManager redisCacheManager;
////
////    public RedisSessionDao(RedisCacheManager redisCacheManager) {
////        this.cache = (RedisCache)redisCacheManager.getCache("session-cache-manager");
////    }
//
//    @Override
//    protected Serializable doCreate(Session session) {
//        Serializable sessionId = this.generateSessionId(session);
//        this.assignSessionId(session, sessionId);
//        //this.saveSession(session);
//        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.SECONDS);
//        return sessionId;
//    }
//
//    @Override
//    protected Session doReadSession(Serializable sessionId) {
//        return sessionId == null ? null : (Session) redisTemplate.opsForValue().get(sessionId);
//    }
//
//    @Override
//    public void update(Session session) throws UnknownSessionException {
//        if (session != null && session.getId() != null) {
//            session.setTimeout(expireTime * 1000);
//            redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.SECONDS);
//        }
//    }
//
//    @Override
//    public void delete(Session session) {
//        if (session == null || session.getId() == null) {
//            log.error("session or session id is null");
//            return;
//        }
//        redisTemplate.opsForValue().getOperations().delete(session.getId());
//    }
//
//    @Override
//    public Collection<Session> getActiveSessions() {
//
//        /** 这里最好别用keys，可能会阻塞
//         Set<String> keys = cache.keys();
//         if(keys != null && keys.size()>0){
//         for(String key : keys){
//         Session s = (Session)cache.get(key);
//         sessions.add(s);
//         }
//         }
//         **/
//        return redisTemplate.keys("*");
//    }
//
//    private void saveSession(Session session) throws UnknownSessionException{
//        if (session == null || session.getId() == null) {
//            log.error("session or session id is null");
//            return;
//        }
////        //设置过期时间
////        long expireTime = 1800000l;
////        session.setTimeout(expireTime);
//        //cache.put(session.getId().toString(), session);
//    }
//}
