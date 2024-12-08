package com.geekplus.webapp.common;

import com.geekplus.common.core.LoggerFactory;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.encrypt.EncryptUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.common.service.CustomerTokenService;
import com.geekplus.webapp.function.entity.Users;
import com.geekplus.webapp.function.service.UsersService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * author     : GeekPlus
 * date       : 9/20/24 5:10 AM
 * description: 客户端用户登录，用作扩展客户系统
 */
@RequestMapping("/customer")
@RestController
public class CustomerLoginController extends BaseController {
    private static Logger log =  LoggerFactory.getLogger(CustomerLoginController.class.getName());

    @Resource
    private CustomerTokenService customerTokenService;

    @Resource
    private UsersService usersService;

    @ApiOperation("客户端登录接口")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody Users customerUserDto) {
        HttpServletResponse response = ServletUtil.getResponse();
        log.info("客户端登录请求参数，customerUserDto:{}", customerUserDto);
        if(StringUtils.isNotEmpty(customerUserDto.getPassword())) {
            customerUserDto.setPassword(EncryptUtil.customerSHAEncryptPwd(customerUserDto.getPassword()));
        }
        //1. 校验用户是否有效
        Users customerUser = usersService.selectUsersForLogin(customerUserDto);
        if (Objects.isNull(customerUser)) {
            log.info("用户登录失败，用户不存在！");
            return Result.error("用户不存在");
        }
        //2. 校验用户名或密码是否正确
        if (!customerUser.getPassword().equals(customerUserDto.getPassword())) {
            log.info("密码不正确，customerAccount:{}", customerUser.getUserName());
            return Result.error("用户名或密码错误");
        }
        //用户登录信息
        log.info("用户: {},登录成功！", customerUser.getNickName());
        //String username = customerUser.getUserName();
        String token = customerTokenService.createToken(customerUser);
        // 设置返回的cookie
        Cookie newCookie = new Cookie("X-TK", token);
        newCookie.setMaxAge(3600); // 设置cookie有效期为1小时
        response.addCookie(newCookie);
        Result result = Result.success();
        result.put("userInfo",customerUser);
        result.put("token", token);
        return result;
    }

    @ApiOperation("客户端登录接口")
    @PostMapping("/register")
    public Result register(@Validated @RequestBody Users customerUserDto) {
        HttpServletResponse response = ServletUtil.getResponse();
        log.info("客户端注册请求参数，customerUserDto:{}", customerUserDto);
        if(StringUtils.isNotEmpty(customerUserDto.getPassword())) {
            customerUserDto.setPassword(EncryptUtil.customerSHAEncryptPwd(customerUserDto.getPassword()));
        }
        Users userName = new Users();
        userName.setUserName(customerUserDto.getUserName());
        //1. 校验用户是否存在
        List<Users> usersList = usersService.selectUsersList(userName);
        if(!usersList.isEmpty() && usersList.size() > 0){
            return Result.error("用户已经存在");
        }else {
            customerUserDto.setPassword(EncryptUtil.customerSHAEncryptPwd(customerUserDto.getPassword()));
            return toResult(usersService.insertUsers(customerUserDto));
        }
    }

    @ApiOperation("客户端登录接口")
    @PostMapping("/logout")
    public Result logout(@Validated @RequestBody(required = false) Users customerUserDto) {
        HttpServletRequest request = ServletUtil.getRequest();
        String token = customerTokenService.getToken(request);
        if(token != null && StringUtils.isNotEmpty(token)){
            customerTokenService.removeCustomerTokenId(token, null);
        }else {
            customerTokenService.removeCustomerTokenId(null, customerUserDto.getTokenId());
        }
        return Result.success();
    }

    @ApiOperation("发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public Result customerList() {
        log.info("发送邮箱验证码");
        return Result.success();
    }
}
