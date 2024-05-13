package com.dragon.filter;

import cn.hutool.core.util.StrUtil;
import com.dragon.constant.MessageConstant;
import com.dragon.jwt.JwtHelper;
import com.dragon.result.ResponseUtil;
import com.dragon.result.Result;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public TokenAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/client/user/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(authentication != null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
        }else {
            ResponseUtil.out(response, Result.build(null, MessageConstant.NO_PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        //获取header中的token
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if(!StringUtils.isEmpty(token)){
            String username = JwtHelper.getUsername(token);
            logger.info("username:"+username);
            if(!StringUtils.isEmpty(username)){
                return new UsernamePasswordAuthenticationToken(username,null, Collections.emptyList());
            }
        }
        return null;
    }
}
