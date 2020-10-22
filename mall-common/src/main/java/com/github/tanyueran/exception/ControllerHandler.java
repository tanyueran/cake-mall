package com.github.tanyueran.exception;

import com.github.tanyueran.entity.ResponseResult;
import com.github.tanyueran.entity.ResponseResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class ControllerHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseResult) {
            return body;
        }
        ResponseResult result = new ResponseResult();
        result.setData(body);
        result.setCode(ResponseResultCode.SUCCESS.getCode());
        result.setMsg("请求成功");
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult runtimeException(Exception e) {
        exceptionLog(e);
        ResponseResult result = new ResponseResult();
        result.setCode(ResponseResultCode.FAILED.getCode());
        result.setMsg(e.getMessage());
        result.setData(null);
        return result;
    }

    public void exceptionLog(Exception e) {
        log.error(e.getMessage(), e);
    }
}
