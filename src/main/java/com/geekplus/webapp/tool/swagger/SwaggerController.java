/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/28/23 04:03
 * description: 做什么的？
 */
package com.geekplus.webapp.tool.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping("/swagger")
    public String swaggerUI(){
        return "/swagger-ui.html";
    }

    @GetMapping("/apiDoc")
    public String apiDoc(){
        return "/doc.html";
    }
}
