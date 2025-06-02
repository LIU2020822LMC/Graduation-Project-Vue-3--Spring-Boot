<template>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
    <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.name" placeholder="请输入用户名查询" :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load" >查 询</el-button>
    <el-button  @click="reset" >重 置</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-button type="success" @click="handleAdd">新 增</el-button>
    <el-button type="success" @click="DeleteBatch" >批量删除</el-button>
    <el-upload
        style="display: inline-block;margin: 10px"
        action="http://localhost:2020/admin/import"
        :show-file-list="false"
        :on-success="handleImportSuccess"
    ><el-button type="success">批量导入</el-button></el-upload>
    <el-button type="success" @click="exportData">批量导出</el-button>
  </div>
  <div style="margin-bottom: 5px" class="card" >
    <el-table :data="data.tableData" style="width: 100%;max-height: 540px; overflow-y: auto"   @selection-change="handleSelectionChange"
              :header-cell-style="{color:'#110101',background:'#9cceed'}">
      <el-table-column type="selection" width="55" />
      <el-table-column label="头像" width="100">
        <template #default="scope">
          <el-image v-if="scope.row.avatar"
                    :src="scope.row.avatar"
                    :preview-src-list="[scope.row.avatar]"
                    :preview-teleported="true"
                    style="width: 50px;height: 50px;border-radius: 50%; display:block"/>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID"  />
      <el-table-column prop="username" label="账号"  />
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="name" label="用户名"/>
      <el-table-column prop="email" label="邮箱"/>
      <el-table-column prop="phone" label="电话"/>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
        </template>
      </el-table-column>>
    </el-table>
  </div>
  <div style="margin-bottom: 5px" class="card">
    <el-pagination
        v-model:current-page="data.PageNum"
        v-model:page-size="data.PageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[5, 10, 15, 20]"
        :total="data.Total"
        @current-change="load"
        @size-change="load"
    />
<!--   @current-change="load"<---事件绑定，最终实现了分页功能的数据动态加载,这一行的意思就是每次点击下一页或者点击某一页就会刷新页面数据-->
    <el-dialog title="管理员信息" v-model="data.formVisible"  width="500" destroy-on-close="true">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 70px 20px 0">
        <el-form-item label="ID" v-if="data.form.id"> <!-- 仅编辑时显示ID -->
          <el-input v-model="data.form.id" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item prop="username" label="账号" >
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password" label="密码" >
          <el-input v-model="data.form.password" autocomplete="off" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="name" label="用户名" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱" >
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话" >
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item prop="avatar" label="头像" >
          <el-upload
              action="http://localhost:2020/files/upload"
              :headers="{token: data.user.token}"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary" >上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utlis/request.js";
import {ElMessage, ElMessageBox} from "element-plus";


const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  PageNum:1,
  PageSize: 5,
  Total:0,
  username:null,
  name:null,
  tableData:[],
  formVisible:false,
  form:{},
  rules:{
    username:[
      {required:true,message:"请填写账户账号",trigger:'blur'}
    ],
    password:[
      {required:true,message:"请填写账户密码",trigger:'blur'}
    ],
    name:[
      {required:true,message:"请填写账户用户名",trigger:'blur'}
    ],
    email:[
      {required:true,message:"请填写账户邮箱",trigger:'blur'}
    ],
    phone:[
      {required:true,message:"请填写账户电话",trigger:'blur'}
    ],
  },
  rows:[],
  ids:[]
})
//数据刷新加载
const load = () => {
  request.get('admin/selectPage',{
    params: {
      pageNum: data.PageNum, // 当前页码（第几页）
      pageSize: data.PageSize, // 每页显示数据量
      name:data.name, // 搜索条件（按名称过滤）
      username:data.username
    }
  }).then(res=>{
    if(res.code === "200"){
      data.tableData = res.data.list;
      data.Total = res.data.total;
    }
    else{
      ElMessage.error(res.message)
    }
  })
}
load()
//重置按钮事件
const reset = () =>{
  data.name = null;
  data.username = null;
  load()
}
//新增按钮事件
const handleAdd = () =>{
  data.formVisible = true;
  data.form = {};
}
//表单验证方法
const formRef = ref()
//新增数据保存事件
const add =() => {
  // fromRef是表单的引用，通过它可以对表单进行验证
  formRef.value.validate((valid) =>{
    if(valid){ //验证通过的情况下才会调用后台数据接口
      request.post('admin/add', data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success('新增数据成功');
          load();
        } else {
          ElMessage.error(res.message);
        }
      })
    }
  })
}
//编辑按钮事件
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));//深度拷贝数据
  data.formVisible = true;
}
const update = () =>{
  // fromRef是表单的引用，通过它可以对表单进行验证
  formRef.value.validate((valid) =>{
    if(valid){ //验证通过的情况下才会调用后台数据接口
      request.put('admin/update', data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success('修改数据成功');
          load();
        } else {
          ElMessage.error(res.message);
        }
      })
    }
  })
}
//判断是新增数据保存还是更新数据保存
const save = () =>{
  data.form.id ? update() : add();
}

const del = (id) =>{
  ElMessageBox.confirm("删除后无法恢复，您确定删除这行数据吗？","删除确认",{type:"warning"}).then(res=>{
      request.delete("admin/delete/" + id).then(res => {
        if (res.code === "200") {
          ElMessage.success('删除数据成功');
          load();
        } else {
          ElMessage.error(res.message);
        }
      })
  }).catch(err=>{})
}
const handleSelectionChange = (rows) =>{ //rows就是实际选择的数组
  data.rows = rows
  data.ids = data.rows.map(v => v.id) //map可以把对象的数组转换成一个纯数字的数组
}
const DeleteBatch = () =>{
  if(data.rows.length === 0){
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm("删除后无法恢复，您确定删除这行数据吗？","删除确认",{type:"warning"}).then(res=>{
    request.delete("admin/deleteBatch",{data:data.rows}).then(res => {
      if (res.code === "200") {
        ElMessage.success('批量删除数据成功');
        load();
      } else {
        ElMessage.error(res.message);
      }
    })
  }).catch(err=>{})
}
const exportData = () =>{
  let idsStr = data.ids.join(",") //把数组转换成字符串：[1,2,3] -> "1,2,3"
  //window.open("http://localhost:2020/admin/export");//前端调用后端导出数据
  let url = `http://localhost:2020/admin/export?username=${data.username === null ? '' : data.username}`
  +`&name=${data.name === null ? '' : data.name}`
  +`&ids=${idsStr}`
  +`&token=${data.user.token}`;
  window.open(url);
}
const handleImportSuccess = (res) => {
 if(res.code === '200'){
   ElMessage.success("数据批量导入成功")
   load()
 }
 else{
   ElMessage.error(res.message)
 }
}
//文件上传函数
const handleFileSuccess = (res) => {
  data.form.avatar = res.data
}
</script>
