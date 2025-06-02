package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.mapper.UserMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    //查询全部数据方法
    public  List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }
    //分页查询方法
    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize,User user) {
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<User> list = userMapper.selectAll(user);

        return PageInfo.of(list);
    }
    //新增数据（插入数据）的方法
    public void add(User user) {
            //根据新插入的账户数据来查询数据库里面是否有相同的账号，有的话就抛出错误提醒用户
           User dbuser = userMapper.selectByUsername(user.getUsername());
           if(dbuser != null){
                throw new CustomerException("账号重复，请重新输入");
           }

//        如果密码为空的话，会自动补全一个默认密码 123456
            if(StrUtil.isBlank(user.getPassword())){
                user.setPassword("123456");
            }
            //把账号名作为用户名
            if(StrUtil.isBlank(user.getName())){
                user.setName(user.getUsername());
            }
            if(StrUtil.isBlank(user.getRole())){
                user.setRole("USER");
            }
            userMapper.insert(user);

    }
    //更新数据的方法
    public void update(User user) {
        userMapper.updateByID(user);
    }
    //单个删除数据方法
    public void deleteByID(Integer id) {
        userMapper.deleteByID(id);
    }
    //批量删除数据方法
    public void deleteBatch(List<User> list) {
        for(User user : list){
            this.deleteByID(user.getId()); //利用for循环将批量选择到的数据用deleteByID（单个删除数据方法）方法逐个删除
        }
    }

    public User login(Account account) {
        User dbuser = userMapper.selectByUsername(account.getUsername());
        if(dbuser == null){
            throw new CustomerException("账号不存在");
        }
        //验证密码是否正确
        if(!dbuser.getPassword().equals(account.getPassword())){
            throw new CustomerException("账号或密码错误");
        }
        //创建token并返回给前端
        String token = TokenUtils.createToken(dbuser.getId() + "-" + "USER",dbuser.getPassword());
        dbuser.setToken(token);
        return dbuser;
    }

    public void register(User user) {
        this.add(user);
    }

    public User selectByID(String id) {
        return userMapper.selectByID(id);
    }

    public void updatePassword(Account account) {

        //先判断新密码和确认密码是否一致
        if(!account.getNewPassword().equals(account.getConfirmPassword())){
            throw new CustomerException("500","两次输入的密码不一致");
        }

        //校验一下原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if ( !account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500", "原密码输入错误");
        }

        //开始更新密码了
        User user = userMapper.selectByID(currentUser.getId().toString());
        user.setPassword(account.getNewPassword());
        userMapper.updateByID(user);
    }
}



