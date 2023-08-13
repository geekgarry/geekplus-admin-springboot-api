
package com.geekplus.common.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/5/23 9:01 上午
 * description: base64的图片验证码
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidBase64CodeVO {
    @ApiModelProperty("base64后的图片验证码")
    private String base64Code;

    @ApiModelProperty("图片媒体类型")
    private String mediaType;

    @ApiModelProperty("图片验证码的key")
    private String validateKey;
}
