package com.github.seckillsystem.dao;

import com.github.seckillsystem.entity.UserAdmin;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserAdminMapper {
    int selectIfExitsByName(String username);
    UserAdmin selectByName(String username);
    int insert(UserAdmin userAdmin);
}
