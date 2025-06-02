package com.example.controller;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody User user) { //@RequestBody 接受前端传来的json参数
        userService.add(user);
        return Result.Success();
    }
    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody User user) { //@RequestBody 接受前端传来的json参数
        userService.update(user);
        return Result.Success();
    }
    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        userService.deleteByID(id);
        return Result.Success();
    }
    //批量删除数据接口
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> list ) { //@RequestBody 接受前端传来的json数组
        userService.deleteBatch(list);
        return Result.Success();
    }
    @GetMapping("/selectAll") //完整路径：http://ip:port/user/selectAll
    public Result selectAll(User user) {
        List<User> userList = userService.selectAll(user);
        return Result.Success(userList);
    }
    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             User user) {
        PageInfo<User>pageInfo = userService.selectPage(pageNum,pageSize,user);
        return Result.Success(pageInfo);//返回的是分页的对象
    }
    //数据导出接口
    //ids: 1,2,3
    @GetMapping("/export")
    public void exportData(User user,HttpServletResponse response) throws Exception {
        String ids = user.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            user.setIdsArr(idsArr);
        }
        //1.拿到所有数据
        List<User> list = userService.selectAll(user);
        //2.构建Wirter对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("username","账号");
        //writer.addHeaderAlias("password","密码"); 一般不导出密码，因为这是敏感信息
        writer.addHeaderAlias("name","用户名");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        //默认的，未添加alist的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        //4.写出数据到writer
        writer.write(list);
        //5.设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6.写出到输出流，并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }
    //批量数据导入接口
    @PostMapping("/import")
    public Result importData( MultipartFile file) throws Exception {
        //1.拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //2.通过Reader读取 excel 里面的数据
        reader.addHeaderAlias("ID","id");
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("用户名","name");
        reader.addHeaderAlias("邮箱","email");
        reader.addHeaderAlias("电话","phone");
        List<User> list = reader.readAll(User.class);
        //3.将数据写到数据库
        for(User user : list){
            userService.add(user);
        }
        return Result.Success();
    }
}
