package com.example.controller;
import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    NoticeService noticeService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) { //@RequestBody 接受前端传来的json参数
        noticeService.add(notice);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) { //@RequestBody 接受前端传来的json参数
        noticeService.update(notice);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        noticeService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/notice/selectAll
    public Result selectAll(Notice notice) {
        List<Notice> noticeList = noticeService.selectAll(notice);
        return Result.Success(noticeList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Notice notice) {
        PageInfo<Notice>pageInfo = noticeService.selectPage(pageNum,pageSize,notice);
        return Result.Success(pageInfo);//返回的是分页的对象
    }

}
