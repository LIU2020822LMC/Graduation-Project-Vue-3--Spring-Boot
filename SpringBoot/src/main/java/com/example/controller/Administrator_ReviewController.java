package com.example.controller;
import com.example.common.Result;
import com.example.entity.Administrator_Review;
import com.example.service.Administrator_ReviewService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator_review")
public class Administrator_ReviewController {

    @Resource
    Administrator_ReviewService administrator_reviewService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Administrator_Review administrator_review) { //@RequestBody 接受前端传来的json参数
        administrator_reviewService.add(administrator_review);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Administrator_Review administrator_review) { //@RequestBody 接受前端传来的json参数
        administrator_reviewService.update(administrator_review);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        administrator_reviewService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/administrator_review/selectAll
    public Result selectAll(Administrator_Review administrator_review) {
        List<Administrator_Review> administrator_reviewList = administrator_reviewService.selectAll(administrator_review);
        return Result.Success(administrator_reviewList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Administrator_Review administrator_review) {
        PageInfo<Administrator_Review>pageInfo = administrator_reviewService.selectPage(pageNum,pageSize,administrator_review);
        return Result.Success(pageInfo);//返回的是分页的对象
    }

}
