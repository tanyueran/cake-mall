package com.github.tanyueran.filter;

import com.alibaba.druid.util.StringUtils;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.vo.UserVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private PublicKey publicKey;
    private RedisTemplate redisTemplate;
    private String redis_user_pre;

    public JWTAuthorizationFilter() {
    }

    public JWTAuthorizationFilter(PublicKey publicKey, RedisTemplate redisTemplate, String redis_user_pre) {
        this.publicKey = publicKey;
        this.redisTemplate = redisTemplate;
        this.redis_user_pre = redis_user_pre;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            // 解析token，获取redis中账号的权限，保存在这次的session中
            Map<String, String> map = null;
            try {
                map = JwtUtils.verifyToken(token, (RSAPublicKey) publicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (map == null) {
                SecurityContextHolder.getContext().setAuthentication(null);
            } else {
                // redis中获取
                UserVo obj = (UserVo) this.redisTemplate.opsForValue().get(redis_user_pre + map.get("userCode"));
                if (obj == null) {
                    SecurityContextHolder.getContext().setAuthentication(null);
                } else {
                    String userCode = map.get("userCode");
                    List<SimpleGrantedAuthority> list = new ArrayList<>();
                    list.add(new SimpleGrantedAuthority("ROLE_" + obj.getUserRole().getRoleCode()));
                    Authentication authResult = new UsernamePasswordAuthenticationToken(userCode, null, list);
                    // 将获取到的数据放置此次的数据中
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
