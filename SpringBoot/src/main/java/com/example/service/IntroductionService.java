package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntroductionService {
    @Resource
    IntroductionMapper introductionMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;

    //查询全部数据方法
    public  List<Introduction> selectAll(Introduction introduction) {
        return introductionMapper.selectAll(introduction);
    }

    //分页查询方法
    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize,Introduction introduction) {
        //查之前要先给它条件
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            introduction.setUserId(currentUser.getId());
        }

        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        //这个list里面存储了旅游攻略的原始数据（只有分类id，categoryId）
        List<Introduction> list = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : list) {
            //拿到categoryId
            Integer categoryId = dbIntroduction.getCategoryId();
            //拿到userId
            Integer userId = dbIntroduction.getUserId();
            // 通过categoryId从category表里通过主键查询出分类数据
            Category category = categoryMapper.selectById(categoryId);
            User user = userMapper.selectByID(userId.toString());
            if(ObjectUtil.isNotEmpty(category)){
                // 把分类的title赋值给categoryTitle
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if(ObjectUtil.isNotEmpty(user)){
                dbIntroduction.setUsername(user.getUsername());
            }

        }
        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());
        introduction.setTime(DateUtil.now());
        introductionMapper.insert(introduction);
    }

    //更新数据的方法
    public void update(Introduction introduction) {
        introductionMapper.updateByID(introduction);
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        introductionMapper.deleteByID(id);
    }

    public Introduction selectById(Integer id) {
          Introduction dbIntroduction =  introductionMapper.selectById(id);
        //拿到categoryId
        Integer categoryId = dbIntroduction.getCategoryId();
        //拿到userId
        Integer userId = dbIntroduction.getUserId();
        // 通过categoryId从category表里通过主键查询出分类数据
        Category category = categoryMapper.selectById(categoryId);
        User user = userMapper.selectByID(userId.toString());
        if(ObjectUtil.isNotEmpty(category)){
            // 把分类的title赋值给categoryTitle
            dbIntroduction.setCategoryTitle(category.getTitle());
        }
        if(ObjectUtil.isNotEmpty(user)){
            dbIntroduction.setUsername(user.getUsername());
            dbIntroduction.setUserAvatar(user.getAvatar());
        }
        return dbIntroduction;
    }
}



