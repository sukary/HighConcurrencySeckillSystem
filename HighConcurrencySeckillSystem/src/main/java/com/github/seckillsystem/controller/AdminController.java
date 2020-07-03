package com.github.seckillsystem.controller;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.entity.UserAdmin;
import com.github.seckillsystem.entity.UserAdminLoginParams;
import com.github.seckillsystem.entity.UserAdminParam;
import com.github.seckillsystem.service.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@Api(tags = "AdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class AdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserAdminService adminService;

    /**
     * 用户注册
     * @return
     */
    @ApiOperation("用户注册")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResult register(@RequestBody UserAdminParam param, BindingResult result){
         UserAdmin user = adminService.register(param);
         if(user != null){
             return CommonResult.success(user);
         }
         return CommonResult.fail("注册失败，请重新再试");
    }
    /**
     * 用户登录
     *
     */
    @ApiOperation("用户登录,登录返回token")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResult login(@RequestBody UserAdminLoginParams params){
        String token = adminService.login(params.getUsername(),params.getPassword());
        if(token == null){
            return CommonResult.validateFailed("用户名密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }
}
