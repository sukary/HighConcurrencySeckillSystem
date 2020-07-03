package com.github.seckillsystem.service;


import com.github.seckillsystem.entity.UmsResource;
import com.github.seckillsystem.entity.UserAdmin;
import com.github.seckillsystem.entity.UserAdminLoginParams;
import com.github.seckillsystem.entity.UserAdminParam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserAdminService {
    UserAdmin register(UserAdminParam param);
    String login(String username,String password);
    UserDetails loadUserByUsername(String username);
}
