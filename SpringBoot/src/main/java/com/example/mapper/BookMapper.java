package com.example.mapper;

import com.example.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {
    List<Book> selectAll(Book book);

    void insert(Book book);


    void updateByID(Book book);
    @Delete("Delete from `book` where id = #{id} ")

    void deleteByID(Integer id);

    @Select("select * from `book` where id = #{id}")
    Book selectById(Integer id);
    
}
