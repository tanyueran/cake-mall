package com.github.tanyueran.config;

import com.github.tanyueran.filter.JWTAuthorizationFilter;
import com.github.tanyueran.handler.MyAccessDeniedHandler;
import com.github.tanyueran.handler.MyAuthenticationEntryPoint;
import com.github.tanyueran.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
// 开启注解
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${self.key.public}")
    private String publicKey;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${self.user_prefix}")
    private String redis_user_pre;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                // swagger-ui的
                "/v2/api-docs",//swagger api json
                "/swagger-resources/configuration/ui",//用来获取支持的动作
                "/swagger-resources",//用来获取api-docs的URI
                "/swagger-resources/configuration/security",//安全选项
                "/swagger-ui.html",
                "/webjars/**",
                "/druid/**",// druid
                "/user/login",// 登录的
                "/user/aLogin"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 添加权限
        /*
         * 注意：此处配置的匿名、允许所有的，自定义的filter都会执行
         * */
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()
                // 禁用表单认证和HTTPbasic
                .formLogin().disable()
                .httpBasic().disable()
                // 禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 禁用csrf
                .csrf().disable()
                // 添加自定义的异常处理器
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                // 添加jwt校验
                .addFilterBefore(new JWTAuthorizationFilter(RsaUtil.getPublicKey(publicKey), redisTemplate, redis_user_pre), UsernamePasswordAuthenticationFilter.class);
    }
}
