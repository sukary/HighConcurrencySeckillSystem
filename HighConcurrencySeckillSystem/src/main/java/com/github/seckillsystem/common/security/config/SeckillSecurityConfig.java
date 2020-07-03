package com.github.seckillsystem.common.security.config;


import com.github.seckillsystem.service.UmsResourceService;
import com.github.seckillsystem.service.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * seckill security配置
 * @author bu
 * Created by bu on 2019/5/23.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeckillSecurityConfig  {
    @Autowired
    private UserAdminService userAdminService;
    @Autowired
    private UmsResourceService resourceService;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userAdminService.loadUserByUsername(username);
    }

//    @Bean
//    public DynamicSecurityService dynamicSecurityService() {
//        return new DynamicSecurityService() {
//            @Override
//            public Map<String, ConfigAttribute> loadDataSource() {
//                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
//                List<UmsResource> resourceList = resourceService.listAll();
//                for (UmsResource resource : resourceList) {
//                    map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
//                }
//                return map;
//            }
//        };
//    }
}
