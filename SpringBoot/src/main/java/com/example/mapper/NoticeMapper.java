package com.example.mapper;

import com.example.entity.Notice;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface NoticeMapper {
    List<Notice> selectAll(Notice notice);

    void insert(Notice notice);


    void updateByID(Notice notice);
    @Delete("Delete from `notice` where id = #{id} ")

    void deleteByID(Integer id);
    
}
