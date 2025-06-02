package com.example.mapper;

import com.example.entity.Administrator_Review;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface Administrator_ReviewMapper {
    List<Administrator_Review> selectAll(Administrator_Review administrator_review);

    void insert(Administrator_Review administrator_review);


    void updateByID(Administrator_Review administrator_review);
    @Delete("Delete from `administrator_review` where id = #{id} ")

    void deleteByID(Integer id);
    
}
