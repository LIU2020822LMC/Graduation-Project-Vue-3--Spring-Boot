package com.example.controller;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
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
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;
    //增加数据的接口
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) { //@RequestBody 接受前端传来的json参数
        adminService.add(admin);
        return Result.Success();
    }
    //编辑数据的接口
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) { //@RequestBody 接受前端传来的json参数
        adminService.update(admin);
        return Result.Success();
    }
    //删除数据接口
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //@PathVariable 接受前端传来的路径参数
        adminService.deleteByID(id);
        return Result.Success();
    }
    //批量删除数据接口
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list ) { //@RequestBody 接受前端传来的json数组
        adminService.deleteBatch(list);
        return Result.Success();
    }
    @GetMapping("/selectAll") //完整路径：http://ip:port/admin/selectAll
    public Result selectAll(Admin admin) {
        List<Admin> adminList = adminService.selectAll(admin);
        return Result.Success(adminList);
    }
    @GetMapping("/selectPage")
//    分页查询
//    pageNum：当前页码
//    pageSize：每页展示的个数
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin>pageInfo = adminService.selectPage(pageNum,pageSize,admin);
        return Result.Success(pageInfo);//返回的是分页的对象
    }
    //数据导出接口
    //ids: 1,2,3
    @GetMapping("/export")
    public void exportData(Admin admin,HttpServletResponse response) throws Exception {
        String ids = admin.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            admin.setIdsArr(idsArr);
        }
        //1.拿到所有数据
        List<Admin> list = adminService.selectAll(admin);
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
        List<Admin> list = reader.readAll(Admin.class);
        //3.将数据写到数据库
        for(Admin admin : list){
            adminService.add(admin);
        }
        return Result.Success();
    }
}
