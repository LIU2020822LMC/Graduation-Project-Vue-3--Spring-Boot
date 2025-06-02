package com.example.controller;
import com.example.common.Result;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    RecordService recordService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Record record) { //@RequestBody 接受前端传来的json参数
        recordService.add(record);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Record record) { //@RequestBody 接受前端传来的json参数
        recordService.update(record);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        recordService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/record/selectAll
    public Result selectAll(Record record) {
        List<Record> recordList = recordService.selectAll(record);
        return Result.Success(recordList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Record record) {
        PageInfo<Record>pageInfo = recordService.selectPage(pageNum,pageSize,record);
        return Result.Success(pageInfo);//返回的是分页的对象
    }

}
