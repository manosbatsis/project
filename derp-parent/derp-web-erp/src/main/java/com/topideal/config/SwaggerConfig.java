package com.topideal.config;

import com.topideal.common.system.webapi.MessageEnum;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/** http://xxxxx:9010/derp-webapi.jsp
 1.首先通过 @EnableSwagger2 注解开启了 Swagger 2，然后最主要的是配置一个 Docket。
 2.通过 apis 方法配置要扫描的 controller 位置，通过 paths 方法配置路径。
 3.在 apiInfo 中构建文档的基本信息，例如描述、联系人信息、版本、标题等。
 * */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //配置是否禁用swagger
    @Value("${swaggerenable}")
    private Boolean enable;

    @Bean
    Docket docket() {
        //添加全局响应状态码
        List globalCodeList = new ArrayList();
        globalCodeList.add(new ResponseMessageBuilder()
                .code(Integer.valueOf(MessageEnum.SUCCESS_10000.getCode()))
                .message(MessageEnum.SUCCESS_10000.getMessage())
                .build());
        globalCodeList.add(new ResponseMessageBuilder()
                .code(Integer.valueOf(MessageEnum.LOGINTIMEOUT_99998.getCode()))
                .message(MessageEnum.LOGINTIMEOUT_99998.getMessage())
                .build());
        globalCodeList.add(new ResponseMessageBuilder()
                .code(Integer.valueOf(MessageEnum.ERROR_99999.getCode()))
                .message(MessageEnum.ERROR_99999.getMessage())
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, globalCodeList)
                .globalResponseMessage(RequestMethod.POST, globalCodeList)
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.topideal.webapi"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class)) // 扫描类注解
                //.paths(PathSelectors.any())
                //.paths(PathSelectors.ant("/system/**"))
                .build();
    }
   /* @Bean
    Docket docket_trans() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoTrans())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.topideal.api"))
                .paths(PathSelectors.ant("/trans/**"))
                .build();
    }*/

}
