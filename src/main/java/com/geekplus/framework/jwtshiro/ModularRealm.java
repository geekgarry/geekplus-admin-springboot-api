//package com.geekplus.framework.jwtshiro;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
//import org.apache.shiro.realm.Realm;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * author     : geekplus
// * email      :
// * date       : 10/1/24 11:35 AM
// * description: //TODO
// */
//public class ModularRealm extends ModularRealmAuthenticator {
//
//    @Override
//    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
//        // 判断getRealms()是否返回为空
//        assertRealmsConfigured();
//        // 所有Realm
//        Collection<Realm> realms = getRealms();
//        // 登录类型对应的所有Realm
//        Map<String, Realm> realmHashMap = new HashMap<>(realms.size());
//        for (Realm realm : realms) {
//            // 这里使用的realm中定义的Name属性来进行区分，注意realm中要加上
//            realmHashMap.put(realm.getName(), realm);
//        }
//        Cus token = (JWTToken) authenticationToken;
//        // 登录类型
//        String type = token.getLoginType();
//        if (realmHashMap.get(type) != null) {
//            return doSingleRealmAuthentication(realmHashMap.get(type), token);
//        } else {
//            return doMultiRealmAuthentication(realms, token);
//        }
//    }
//}
