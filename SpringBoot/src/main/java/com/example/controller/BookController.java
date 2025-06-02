package com.example.controller;
import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    BookService bookService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Book book) { //@RequestBody 接受前端传来的json参数
        bookService.add(book);
        return Result.Success();
    }

    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Book book) { //@RequestBody 接受前端传来的json参数
        bookService.update(book);
        return Result.Success();
    }

    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        bookService.deleteByID(id);
        return Result.Success();
    }

    @GetMapping("/selectAll") //完整路径：http://ip:port/book/selectAll
    public Result selectAll(Book book) {
        List<Book> bookList = bookService.selectAll(book);
        return Result.Success(bookList);
    }

    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Book book) {
        PageInfo<Book>pageInfo = bookService.selectPage(pageNum,pageSize,book);
        return Result.Success(pageInfo);//返回的是分页的对象
    }

}
