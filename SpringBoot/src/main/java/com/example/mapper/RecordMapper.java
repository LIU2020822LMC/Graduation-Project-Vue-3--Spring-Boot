package com.example.mapper;

import com.example.entity.Record;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface RecordMapper {
    List<Record> selectAll(Record record);

    void insert(Record record);


    void updateByID(Record record);
    @Delete("Delete from `record` where id = #{id} ")

    void deleteByID(Integer id);
    
}
