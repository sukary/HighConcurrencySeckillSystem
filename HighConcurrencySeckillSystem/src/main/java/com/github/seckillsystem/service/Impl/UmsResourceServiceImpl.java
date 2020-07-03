package com.github.seckillsystem.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.seckillsystem.entity.UmsResource;
import com.github.seckillsystem.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Override
    public int create(UmsResource umsResource) {
        return 0;
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        return 0;
    }

    @Override
    public UmsResource getItem(Long id) {
        return null;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public List<UmsResource> listAll() {
        return null;
    }
//    @Autowired
//    private UmsResourceMapper resourceMapper;
//    @Autowired
//    private UmsAdminCacheService adminCacheService;
//    @Override
//    public int create(UmsResource umsResource) {
//        umsResource.setCreateTime(new Date());
//        return resourceMapper.insert(umsResource);
//    }
//
//    @Override
//    public int update(Long id, UmsResource umsResource) {
//        umsResource.setId(id);
//        int count = resourceMapper.updateByPrimaryKeySelective(umsResource);
//        adminCacheService.delResourceListByResource(id);
//        return count;
//    }
//
//    @Override
//    public UmsResource getItem(Long id) {
//        return resourceMapper.selectByPrimaryKey(id);
//    }
//
//    @Override
//    public int delete(Long id) {
//        int count = resourceMapper.deleteByPrimaryKey(id);
//        adminCacheService.delResourceListByResource(id);
//        return count;
//    }
//
//    @Override
//    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
//        PageHelper.startPage(pageNum,pageSize);
//        UmsResourceExample example = new UmsResourceExample();
//        UmsResourceExample.Criteria criteria = example.createCriteria();
//        if(categoryId!=null){
//            criteria.andCategoryIdEqualTo(categoryId);
//        }
//        if(StrUtil.isNotEmpty(nameKeyword)){
//            criteria.andNameLike('%'+nameKeyword+'%');
//        }
//        if(StrUtil.isNotEmpty(urlKeyword)){
//            criteria.andUrlLike('%'+urlKeyword+'%');
//        }
//        return resourceMapper.selectByExample(example);
//    }
//
//    @Override
//    public List<UmsResource> listAll() {
//        return resourceMapper.selectByExample(new UmsResourceExample());
//    }
}