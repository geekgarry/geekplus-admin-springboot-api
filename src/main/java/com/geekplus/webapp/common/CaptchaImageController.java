package com.geekplus.webapp.common;

import com.geekplus.common.domain.Result;
import com.geekplus.common.domain.ValidBase64CodeVO;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.CaptchaImageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/5/23 8:55 上午
 * description: 请求返回验证码图片
 */
@Slf4j
@Controller
public class CaptchaImageController {
    private long expireTime=60;

    //封装redis操作工具类
//    @Autowired
//    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取登录图片验证码(返回base64 application/json方式)
     */
    @ApiOperation(value = "获取图片验证码base64接口")
    @CrossOrigin
    @GetMapping("/captchaBase64")
    @ResponseBody
    public Result captchaImage() {

        CaptchaImageUtil instance = CaptchaImageUtil.getInstance();
        // 验证码标识
        String key = "GP" + System.currentTimeMillis() + (10000 + new Random().nextInt(89999));
        //String cacheKey = CacheKeyUtil.getImageValidateCodeCacheKey(key);
        redisTemplate.opsForValue().set(key, instance.getCode(), expireTime, TimeUnit.SECONDS);
        // 将图像输出到Servlet输出流中。
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(instance.getBuffImg(), "gif", out);
            return Result.success(new ValidBase64CodeVO(Base64Utils.encodeToString(out.toByteArray()), "image/gif", key));
        } catch (IOException e) {
            log.error("获取图片验证码异常", e);
            redisTemplate.delete(key);
            throw new BusinessException(ApiExceptionEnum.CAPTCHA_ACCESS_FAIL);
        }
    }

    /**
     * 获取登录图片验证码(返回图片流方式)
     */
    @ApiOperation(value = "获取图片验证码接口")
    @GetMapping(value = "/captchaImage")
    public ResponseEntity<byte[]> getCaptchaCode() {
        CaptchaImageUtil instance = CaptchaImageUtil.getInstance();
        // 验证码标识
        String key = "GP" + System.currentTimeMillis() + (10000 + new Random().nextInt(89999));
        //String cacheKey = CacheKeyUtil.getImageValidateCodeCacheKey(key);
        redisTemplate.opsForValue().set(key, instance.getCode(), expireTime, TimeUnit.SECONDS);
        // 将图像输出到Servlet输出流中。
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(instance.getBuffImg(), "jpeg", out);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .cacheControl(CacheControl.noCache())
                    .header("CaptchaKey", key)
                    .header("Access-Control-Expose-Headers", "CaptchaKey")
                    .body(out.toByteArray());
        } catch (IOException e) {
            redisTemplate.delete(key);
            log.error("获取图片验证码异常", e);
            throw new RuntimeException("获取图片验证码异常");
        }
    }

}
