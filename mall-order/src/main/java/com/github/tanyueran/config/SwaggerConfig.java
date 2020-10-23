package com.github.tanyueran.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@EnableSwagger2
@Configuration
public class SwaggerConfig {

    // 这是一个swagger分组
    @Bean
    public Docket docket() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        //Token 以及Authorization 为自定义的参数，session保存的名字是哪个就可以写成那个
        ticketPar.name("token").description("user ticket")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())                 // 用于定义api文档汇总信息
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.github.tanyueran.controller"))   // 指定controller包
                .paths(PathSelectors.any())         // 所有controller
                .build()
                // 添加传递认证信息
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cake-mall平台接口api")        // 文档页标题
                .contact(new Contact("tanyueran",// 联系人信息
                        "https://github.com/tanyueran",
                        ""))
                .description("描述信息")  // 详细信息
                .version("0.0.1")   // 文档版本号
                .termsOfServiceUrl("https://www.baidu.com") // 网站地址
                .build();
    }


}
