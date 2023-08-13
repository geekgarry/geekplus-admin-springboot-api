/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/5/17 4:55 下午
 * description: 做什么的？
 */
package com.geekplus.framework.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        /*添加接口请求头参数配置 没有的话 可以忽略*/
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>();
//        tokenPar.name("Admin-Token").description("令牌").defaultValue("")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                // DocumentationType.SWAGGER_2 固定的，代表swagger2
                //.groupName("分布式任务系统") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .enable(true)
                .apiInfo(apiInfo()) // 用于生成API信息
                .groupName("GeekPlus极客普拉斯")
                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .apis(RequestHandlerSelectors.basePackage("com.geekplus.webapp")) // 用于指定扫描哪个包下的接口
                .paths(PathSelectors.any())// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .build()
                //.globalOperationParameters(pars);
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes());
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<ApiKey> securitySchemes()
    {
        List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
        apiKeyList.add(new ApiKey("Plus-Token", "Plus-Token", "header"));
        return apiKeyList;
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GeekPlus极客普拉斯项目API") //  可以用来自定义API的主标题
                .description("GeekPlus极客普拉斯项目SwaggerAPI管理") // 可以用来描述整体的API
                .contact(new Contact("GeekPlus","https://www.geekplus.xyz","geekgarry@hotmail.com"))
                .termsOfServiceUrl("geekplus.xyz") // 用于定义服务的域名
                .version("1.0") // 可以用来定义版本。
                .build(); //
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/admin/*")
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/doc.html/**");
//    }

//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/*/*/*").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/webjars/*/*").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        //super.addResourceHandlers(registry);
//    }
}
