package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utlis.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Resource
    NoticeMapper noticeMapper;
    //查询全部数据方法
    public  List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    //分页查询方法
    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize,Notice notice) {
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        List<Notice> list = noticeMapper.selectAll(notice);

        return PageInfo.of(list);
    }

    //新增数据（插入数据）的方法
    public void add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    //更新数据的方法
    public void update(Notice notice) {
        noticeMapper.updateByID(notice);
    }

    //单个删除数据方法
    public void deleteByID(Integer id) {
        noticeMapper.deleteByID(id);
    }

}



