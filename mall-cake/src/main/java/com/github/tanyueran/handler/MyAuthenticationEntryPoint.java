package com.github.tanyueran.handler;

import com.alibaba.fastjson.JSON;
import com.github.tanyueran.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 处理匿名用户访问无权限资源时的异常
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("匿名用户访问异常：+++++++++++++++==");
        log.error(e.getMessage(), e);
        // 可以针对不同的异常，返回响应的文字
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        httpServletResponse.setStatus(401);
        ResponseResult res = new ResponseResult();
        res.setCode(401);
        res.setMsg(e.getMessage());
        res.setData(false);
        writer.write(JSON.toJSONString(res));
        writer.close();
    }
}
