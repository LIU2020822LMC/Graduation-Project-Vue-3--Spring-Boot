package com.example.controller;
import com.example.common.Result;
import com.example.entity.Introduction;
import com.example.service.IntroductionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/introduction")
public class IntroductionController {

    @Resource
    IntroductionService introductionService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Introduction introduction) { //@RequestBody 接受前端传来的json参数
        introductionService.add(introduction);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Introduction introduction) { //@RequestBody 接受前端传来的json参数
        introductionService.update(introduction);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        introductionService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/introduction/selectAll
    public Result selectAll(Introduction introduction) {
        List<Introduction> introductionList = introductionService.selectAll(introduction);
        return Result.Success(introductionList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Introduction introduction) {
        PageInfo<Introduction>pageInfo = introductionService.selectPage(pageNum,pageSize,introduction);
        return Result.Success(pageInfo);//返回的是分页的对象
    }
/*
    根据id查询
*/
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Introduction introduction = introductionService.selectById(id);
        return Result.Success(introduction);
    }

}
