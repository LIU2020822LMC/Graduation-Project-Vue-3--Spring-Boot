package com.example.mapper;

import com.example.entity.Introduction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IntroductionMapper {
    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);


    void updateByID(Introduction introduction);
    @Delete("Delete from `introduction` where id = #{id} ")

    void deleteByID(Integer id);

    @Select("select * from `introduction` where id = #{id} ")
    Introduction selectById(Integer id);
}
