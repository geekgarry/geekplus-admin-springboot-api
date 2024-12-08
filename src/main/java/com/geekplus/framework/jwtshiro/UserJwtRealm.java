package com.geekplus.framework.jwtshiro;

import com.geekplus.common.core.LoggerFactory;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.common.service.CustomerTokenService;
import com.geekplus.webapp.function.entity.Users;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;

import javax.annotation.Resource;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 9/20/24 1:41 AM
 * description: 适用于前端的客户登录鉴权
 */
public class UserJwtRealm extends AuthenticatingRealm {
    private static Logger log =  LoggerFactory.getLogger(UserJwtRealm.class.getName());

    @Resource
    CustomerTokenService customerTokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UserJwtToken;
    }

    //当继承为 AuthorizingRealm，表示使用到授权，这里因为没有用到，所以注释掉
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        //获取登录用户
//        Users loginUser = (Users) principalCollection.getPrimaryPrincipal();
//        //添加角色和权限
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        return simpleAuthorizationInfo;
//    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        if (StringUtils.isEmpty(token)) {
            log.error("token无效");
            throw new AuthenticationException("用户认证不存在或失效!");
        }
        // 校验token有效性
        try {
            Users customerUser = this.customerTokenService.checkUserTokenIsEffect(token);
            return new SimpleAuthenticationInfo(customerUser, token, getName());
        } catch (AuthenticationException e) {
            //JwtUtil.responseError(SpringContextUtils.getHttpServletResponse(), 401, e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 清除当前用户的权限认证缓存
     *
     * @param principals 权限信息
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
