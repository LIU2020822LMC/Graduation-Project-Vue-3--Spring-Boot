package com.example.controller;
import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Category category) { //@RequestBody 接受前端传来的json参数
        categoryService.add(category);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Category category) { //@RequestBody 接受前端传来的json参数
        categoryService.update(category);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        categoryService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/category/selectAll
    public Result selectAll(Category category) {
        List<Category> categoryList = categoryService.selectAll(category);
        return Result.Success(categoryList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Category category) {
        PageInfo<Category>pageInfo = categoryService.selectPage(pageNum,pageSize,category);
        return Result.Success(pageInfo);//返回的是分页的对象
    }

}
