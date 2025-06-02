package com.example.service;


import com.example.entity.Book;
import com.example.mapper.BookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Resource
    BookMapper bookMapper;
    //查询全部数据方法
    public  List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    //分页查询方法
    public PageInfo<Book> selectPage(Integer pageNum, Integer pageSize,Book book) {
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<Book> list = bookMapper.selectAll(book);

        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Book book) {
        bookMapper.insert(book);
    }

    //更新数据的方法
    public void update(Book book) {
        bookMapper.updateByID(book);
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        bookMapper.deleteByID(id);
    }

}



