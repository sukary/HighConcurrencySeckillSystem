package com.github.seckillsystem.service.Impl;


import cn.hutool.core.collection.CollUtil;
import com.github.seckillsystem.dao.UserAdminMapper;
import com.github.seckillsystem.entity.*;
import com.github.seckillsystem.service.UserAdminService;
import com.github.seckillsystem.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("UserAdminService")
public class AdminServiceImpl implements UserAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserAdminMapper userAdminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserAdmin register(UserAdminParam param) {
        // 判断用户名是否已被使用
        int count = userAdminMapper.selectIfExitsByName(param.getUsername());
        if(count != 0){
            return null;
        }
        //生成一个用户
        UserAdmin userAdmin = new UserAdmin();
        // 对用户进行参数设置
        BeanUtils.copyProperties(param,userAdmin);
        userAdmin.setCreateTime(new Date());
        userAdmin.setStatus(1);
        //数据库储存密码加密
        String passwordEncode = passwordEncoder.encode(userAdmin.getPassword());
        userAdmin.setPassword(passwordEncode);
        int result = userAdminMapper.insert(userAdmin);
        if(result ==1){
            return userAdmin;
        }
        return null;
    }

    @Override
    public String login(String username,String password) {
        String token = null;
        try {
            // 获取用户信息
            UserDetails userDetails = loadUserByUsername(username);
            // 密码匹配
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            // 用户身份验证，返回一个带用户名和密码以及权限的Authentication
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            //验证成功后获取用户的安全上下文信息(如他的角色列表)并添加authentication
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 添加登录记录
//            insertLoginLog(username);
            token = jwtTokenUtil.generateToken(userDetails);
        }catch (AuthenticationException e){
            LOGGER.warn("登录异常",e.getMessage());
        }
        return token;
    }

    public UserDetails loadUserByUsername(String username) {
        UserAdmin admin =  userAdminMapper.selectByName(username);
        if (admin != null) {
            return new AdminUserDetails(admin);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

//    @Override
//    public List<UmsResource> getResourceList(Long adminId) {
//        List<UmsResource> resourceList = new ArrayList<>();
//        resourceList = adminRoleRelationDao.getResourceList(adminId);
//        return resourceList;
//    }
//    private void insertLoginLog(String username){
//        UserAdmin userAdmin = getAdminByUsername(username);
//
//
//
//    }
//    private UserAdmin getAdminByUsername(String username){
//        UserAdmin userAdmin =
//
//    }
}
