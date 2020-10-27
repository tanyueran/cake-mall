package com.github.tanyueran.filter;

import com.alibaba.fastjson.JSON;
import com.github.tanyueran.entity.ResponseResult;
import com.github.tanyueran.entity.ResponseResultCode;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RefreshScope
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {

    // 不需要校验的地址
    @Value("${self.blankUrl}")
    private List<String> blankUrl;

    @Value("${self.key.public}")
    private String publicKey;

    @Value("${self.user_prefix}")
    private String user_prefix;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 校验白名单
        List<String> collect = blankUrl.stream().filter(url -> Pattern.compile(url).matcher(path).matches()).collect(Collectors.toList());
        if (collect.size() > 0) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            // 响应token无效的信息
            ResponseResult result = new ResponseResult();
            result.setCode(ResponseResultCode.UNAUTHORIZED.getCode());
            result.setMsg("请登录");
            result.setData(false);
            return getVoidMono(response, result);
        }
        // 校验token是否合法
        Map<String, String> map = null;
        try {
            map = JwtUtils.verifyToken(token, (RSAPublicKey) RsaUtil.getPublicKey(publicKey));
        } catch (Exception e) {
            log.error("token 解析失败");
            log.error(e.getMessage(), e);
            // 响应token无效的信息
            ResponseResult result = new ResponseResult();
            result.setCode(ResponseResultCode.UNAUTHORIZED.getCode());
            result.setMsg("无效token，请登录");
            result.setData(false);
            return getVoidMono(response, result);
        }
        String userCode = map.get("userCode");
        Object o = redisTemplate.opsForValue().get(user_prefix + userCode);
        if (o == null) {
            // 响应token无效的信息
            ResponseResult result = new ResponseResult();
            result.setCode(ResponseResultCode.UNAUTHORIZED.getCode());
            result.setMsg("token失效，请重新登录");
            result.setData(false);
            return getVoidMono(response, result);
        }
        return chain.filter(exchange);
    }

    // 消息响应
    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseResult responseResult) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
