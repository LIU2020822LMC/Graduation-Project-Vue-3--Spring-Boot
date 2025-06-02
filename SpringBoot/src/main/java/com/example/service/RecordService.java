package com.example.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Book;
import com.example.entity.Record;
import com.example.mapper.BookMapper;
import com.example.mapper.RecordMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;
    @Resource
    BookMapper bookMapper;
    //查询全部数据方法
    public  List<Record> selectAll(Record record) {
        return recordMapper.selectAll(record);
    }

    //分页查询方法
    public PageInfo<Record> selectPage(Integer pageNum, Integer pageSize,Record record) {
        //提交者只能看自己的记录
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())){
            record.setUserId(currentUser.getId());
        }
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<Record> list = recordMapper.selectAll(record);

        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Record record) {
        record.setStatus("待审核");
        record.setTime(DateUtil.now());
        recordMapper.insert(record);
        //图书数量减一
        Book book = bookMapper.selectById(record.getBookId());
        if (ObjectUtil.isNotEmpty(book)){
            book.setNum(book.getNum() - 1);
            bookMapper.updateByID(book);
        }

    }

    //更新数据的方法
    public void update(Record record) {
        recordMapper.updateByID(record);
        //拒绝通过图书加一
        //获取当前用户
        Account currentUser = TokenUtils.getCurrentUser();
        if("ADMIN".equals(currentUser.getRole()) && "审核不通过".equals(record.getStatus())){
            //图书归还
            Book book = bookMapper.selectById(record.getBookId());
            if (ObjectUtil.isNotEmpty(book)){
                book.setNum(book.getNum() + 1);
                bookMapper.updateByID(book);
            }
        }
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        recordMapper.deleteByID(id);
    }

}



