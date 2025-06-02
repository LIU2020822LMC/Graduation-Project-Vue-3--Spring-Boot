<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.name" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load" >查 询</el-button>
      <el-button  @click="reset" >重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'ADMIN'">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
    </div>

    <div style="margin-bottom: 5px" class="card" >
      <el-table :data="data.tableData" style="width: 100%;max-height: 540px; overflow-y: auto" :header-cell-style="{color:'#110101',background:'#9cceed'}">
        <el-table-column prop="img" label="图书封面" width="100" >
          <template #default="scope">
            <el-image v-if="scope.row.img"
                      :src="scope.row.img"
                      :preview-src-list="[scope.row.img]"
                      :preview-teleported="true"
                      style="width: 50px;height: 50px;border-radius: 10%; display:block"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="图书名字"/>
        <el-table-column prop="author" label="图书作者"/>
        <el-table-column prop="price" label="图书价格"/>
        <el-table-column prop="num" label="剩余数量"/>
        <el-table-column label="操作" width="100" >
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-else>
            <el-button type="primary"  @click="borrow(scope.row)">借阅</el-button>
          </template>
        </el-table-column>>
      </el-table>
    </div>

    <el-dialog title="图书信息" v-model="data.formVisible"  width="500" destroy-on-close="true">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 70px 20px 0">
        <el-form-item label="ID" v-if="data.form.id"> <!-- 仅编辑时显示ID -->
          <el-input v-model="data.form.id" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item prop="img" label="图书封面" >
          <el-upload
              action="http://localhost:2020/files/upload"
              :headers="{token: data.user.token}"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary" >上传图书封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="图书名字" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入图书名字" />
        </el-form-item>
        <el-form-item prop="author" label="图书作者" >
          <el-input v-model="data.form.author" autocomplete="off" placeholder="请输入图书作者"/>
        </el-form-item>
        <el-form-item prop="price" label="图书价格" >
          <el-input v-model="data.form.price" autocomplete="off" placeholder="请输入图书价格"/>
        </el-form-item>
        <el-form-item prop="num" label="图书数量" >
          <el-input type="number" v-model="data.form.num" autocomplete="off" placeholder="请输入图书数量"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <!--  分页栏-->
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
    </div>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utlis/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  name:null,
  PageNum:1,
  PageSize: 5,
  Total:0,
  tableData:[],
  formVisible:false,
  form:{},
  //校验规则
  rules:{
    name:[
      {required:true,message:"请填写图书名字",trigger:'blur'}
    ],
    author:[
      {required:true,message:"请填写图书作者",trigger:'blur'}
    ],
    price:[
      {required:true,message:"请填写图书价格",trigger:'blur'}
    ],
    img:[
      {required:true,message:"请上传图书封面",trigger:'blur'}
    ],
    num:[
      {required:true,message:"请上传图书数量",trigger:'blur'}
    ],
  }
})

const formRef = ref(null)

const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

//数据刷新加载
const load = () => {
  request.get('book/selectPage',{
    params: {
      pageNum: data.PageNum, // 当前页码（第几页）
      pageSize: data.PageSize, // 每页显示数据量
      name:data.name,//搜索条件（按标题搜索）
    }
  }).then(res=>{
    if(res.code === "200"){
      data.tableData = res.data?.list;
      data.Total = res.data?.total;
    }
    else{
      ElMessage.error(res.message)
    }
  })
}
load()

//新增按钮事件
const handleAdd = ()=>{
  data.form = {}
  data.formVisible = true
}

const update = () =>{
  request.put("book/update", data.form).then(res =>{
    if (res.code === "200") {
      ElMessage.success('更新成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.message);
    }
  })
}

const add = ()=>{
  request.post('book/add', data.form).then(res => {
    if (res.code === "200") {
      ElMessage.success('新增公告成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.message);
    }
  })
}

//保存事件按钮
const save = ()=>{
  formRef.value.validate(valid => {
    if(valid){
      data.form.id ? update() : add();
    }
  })
}

//删除操作按钮事件
const del = (id) =>{
  ElMessageBox.confirm("删除后无法恢复，您确定删除这行数据吗？","删除确认",{type:"warning"}).then(res=>{
    request.delete("book/delete/" + id).then(res => {
      if (res.code === "200") {
        ElMessage.success('删除数据成功');
        load();
      } else {
        ElMessage.error(res.message);
      }
    })
  }).catch(err=>{})
}

//重置按钮函数
const reset = () =>{
  data.name = null;
  load()
}

//文件上传函数
const handleFileSuccess = (res) => {
  data.form.img = res.data
}

//借阅按钮事件函数
const borrow = (row)=>{
  request.post('record/add',{
    userId: data.user.id,
    bookId: row.id,
  }).then(res=>{
    if(res.code === '200'){
      ElMessage.success('操作成功，等待管理员审核')
      load()
    }else{
      ElMessage.error(res.message)
    }
  })
}
</script>