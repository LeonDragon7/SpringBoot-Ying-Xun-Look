package com.dragon.custom;

import com.dragon.utils.JudgeParam;
import com.dragon.utils.RegexUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    private com.dragon.entity.User userEntity;

    public CustomUser(com.dragon.entity.User user,Collection<? extends GrantedAuthority> authorities) {
        super(JudgeParam.isUserNameOrEmail(user),user.getPassword(),authorities);
        this.userEntity = user;
    }

    public com.dragon.entity.User getUser() {
        return userEntity;
    }

    public void setUser(com.dragon.entity.User user) {
        this.userEntity = user;
    }

}
