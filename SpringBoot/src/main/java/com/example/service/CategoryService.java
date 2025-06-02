package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    CategoryMapper categoryMapper;
    //查询全部数据方法
    public  List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    //分页查询方法
    public PageInfo<Category> selectPage(Integer pageNum, Integer pageSize,Category category) {
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<Category> list = categoryMapper.selectAll(category);

        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    //更新数据的方法
    public void update(Category category) {
        categoryMapper.updateByID(category);
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        categoryMapper.deleteByID(id);
    }

}



