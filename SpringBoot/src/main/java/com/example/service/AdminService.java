package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    AdminMapper adminMapper;
    //查询全部数据方法
    public  List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }
    //分页查询方法
    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize,Admin admin) {
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<Admin> list = adminMapper.selectAll(admin);

        return PageInfo.of(list);
    }
    //新增数据（插入数据）的方法
    public void add(Admin admin) {
            //根据新插入的账户数据来查询数据库里面是否有相同的账号，有的话就抛出错误提醒用户
           Admin dbadmin = adminMapper.selectByUsername(admin.getUsername());
           if(dbadmin != null){
                throw new CustomerException("账号重复，请重新输入");
           }

//        如果密码为空的话，会自动补全一个默认密码 123456
            if(StrUtil.isBlank(admin.getPassword())){
                admin.setPassword("123456");
            }
        if(StrUtil.isBlank(admin.getRole())){
            admin.setRole("ADMIN");
        }
            adminMapper.insert(admin);

    }
    //更新数据的方法
    public void update(Admin admin) {
        adminMapper.updateByID(admin);
    }
    //单个删除数据方法
    public void deleteByID(Integer id) {
        adminMapper.deleteByID(id);
    }
    //批量删除数据方法
    public void deleteBatch(List<Admin> list) {
        for(Admin admin : list){
            this.deleteByID(admin.getId()); //利用for循环将批量选择到的数据用deleteByID（单个删除数据方法）方法逐个删除
        }
    }
    public Admin login(Account account) {
        Admin dbadmin = adminMapper.selectByUsername(account.getUsername());
        if(dbadmin == null){
            throw new CustomerException("账号不存在");
        }
        //验证密码是否正确
        if(!dbadmin.getPassword().equals(account.getPassword())){
            throw new CustomerException("账号或密码错误");
        }
        //创建token并返回给前端
        String token = TokenUtils.createToken(dbadmin.getId() + "-" + "ADMIN",dbadmin.getPassword());
        dbadmin.setToken(token);
        return dbadmin;
    }


    public Admin selectByID(String id) {
        return adminMapper.selectByID(id);
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
        Admin admin = adminMapper.selectByID(currentUser.getId().toString());
        admin.setPassword(account.getNewPassword());
        adminMapper.updateByID(admin);
    }
}



