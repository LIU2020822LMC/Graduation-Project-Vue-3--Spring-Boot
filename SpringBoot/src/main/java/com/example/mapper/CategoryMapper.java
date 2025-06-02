package com.example.mapper;

import com.example.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {
    List<Category> selectAll(Category category);

    void insert(Category category);


    void updateByID(Category category);
    @Delete("Delete from `category` where id = #{id} ")

    void deleteByID(Integer id);

    @Select("select * from category where id = #{id}")
    Category selectById(Integer id);
}
