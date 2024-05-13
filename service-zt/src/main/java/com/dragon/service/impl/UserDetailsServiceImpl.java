package com.dragon.service.impl;

import com.dragon.constant.MessageConstant;
import com.dragon.custom.CustomUser;
import com.dragon.custom.UserDetailsService;
import com.dragon.dto.UserLoginDTO;
import com.dragon.entity.User;
import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.mapper.UserMapper;
import com.dragon.utils.JudgeParam;
import com.dragon.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String var) throws UsernameNotFoundException {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        if(RegexUtil.isEmailInValid(var)) userLoginDTO.setUsername(var);
        else userLoginDTO.setEmail(var);

        User user = userMapper.getUserByEmailOrUsername(userLoginDTO);
        if(null == user) throw new RuntimeExceptionCustom(MessageConstant.USER_NOT_EXIST);

        if(user.getStatus().intValue() != 0) throw new RuntimeExceptionCustom(MessageConstant.DISABLE_LOGOUT);

        return new CustomUser(user, Collections.emptyList());
    }

}
