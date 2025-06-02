package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Administrator_Review;
import com.example.mapper.Administrator_ReviewMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Administrator_ReviewService {
    @Resource
    Administrator_ReviewMapper administrator_reviewMapper;
    //查询全部数据方法
    public  List<Administrator_Review> selectAll(Administrator_Review administrator_review) {
        return administrator_reviewMapper.selectAll(administrator_review);
    }

    //分页查询方法
    public PageInfo<Administrator_Review> selectPage(Integer pageNum, Integer pageSize,Administrator_Review administrator_review) {
        //提交者只能看自己的记录
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())){
            administrator_review.setUserId(currentUser.getId());
        }
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Administrator_Review> list = administrator_reviewMapper.selectAll(administrator_review);
        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Administrator_Review administrator_review) {
        Account currentUser = TokenUtils.getCurrentUser();
        administrator_review.setUserId(currentUser.getId());
        administrator_review.setTime(DateUtil.now());
        administrator_review.setStatus("待审核");
        administrator_reviewMapper.insert(administrator_review);
    }

    //更新数据的方法
    public void update(Administrator_Review administrator_review) {
        administrator_reviewMapper.updateByID(administrator_review);
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        administrator_reviewMapper.deleteByID(id);
    }

}



