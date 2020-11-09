package com.github.tanyueran.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Configuration
@AllArgsConstructor
// 拦截openFeign的请求，添加token
public class FeignAddTokenConfig implements RequestInterceptor {

    private HttpServletRequest req;
    private static final String HEADER_STRING = "token";

    @Override
    public void apply(RequestTemplate template) {
        String token = req.getHeader(HEADER_STRING);
        template.header(HEADER_STRING, token);
    }
}
