package com.dragon.filter;

import cn.hutool.core.util.StrUtil;
import com.dragon.constant.MessageConstant;
import com.dragon.custom.CustomUser;
import com.dragon.dto.UserLoginDTO;
import com.dragon.jwt.JwtHelper;
import com.dragon.result.ResponseUtil;
import com.dragon.result.Result;
import com.dragon.utils.JudgeParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 * </p>
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/client/user/login","POST"));
    }

    /**
     * 登录认证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginDTO userLoginDTO = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(JudgeParam.isUserNameOrEmail(userLoginDTO), userLoginDTO.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录成功
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUser customerUser = (CustomUser) authResult.getPrincipal();
        String token = JwtHelper.createToken(customerUser.getUser().getId().longValue(), customerUser.getUser().getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, Result.success(map));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if(failed.getCause() instanceof RuntimeException)
            ResponseUtil.out(response,Result.build(null,201,failed.getMessage()));
        else
            ResponseUtil.out(response,Result.build(null, MessageConstant.LOGIN_DATA_EXCEPTION));
    }
}
