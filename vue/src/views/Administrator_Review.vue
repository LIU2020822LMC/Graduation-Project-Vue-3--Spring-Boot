<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load" >查 询</el-button>
      <el-button  @click="reset" >重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'USER'">
      <el-button type="primary" @click="handleAdd">提交请假申请</el-button>
    </div>

    <el-dialog title="请假信息" v-model="data.formVisible"  width="500" destroy-on-close="true">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 70px 20px 0">
        <el-form-item label="ID" v-if="data.form.id"> <!-- 仅编辑时显示ID -->
          <el-input v-model="data.form.id" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item prop="title" label="请假标题"  v-if="data.user.role === 'USER'">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入请假标题" />
        </el-form-item>
        <el-form-item prop="content" label="请假内容" v-if="data.user.role === 'USER'">
          <el-input type="textarea" :rows="8" v-model="data.form.content" autocomplete="off" placeholder="请输入请假内容"/>
        </el-form-item>
        <el-form-item prop="status" label="审核状态" v-if="data.user.role === 'ADMIN'">
          <el-radio-group v-model="data.form.status" >
            <el-radio-button label="待审核" value="待审核" />
            <el-radio-button label="审核通过" value="审核通过" />
            <el-radio-button label="审核不通过" value="审核不通过" />
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="reason" label="审核说明"  v-if="data.user.role === 'ADMIN' && data.form.status === '审核不通过'" >
          <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝说明" />
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

  <div style="margin-bottom: 5px" class="card" >
    <el-table :data="data.tableData" style="width: 100%;max-height: 540px; overflow-y: auto" :header-cell-style="{color:'#110101',background:'#9cceed'}">
      <el-table-column prop="title" label="请假标题"  />
      <el-table-column prop="content" label="请假说明"/>
      <el-table-column prop="username" label="请假人"/>
      <el-table-column prop="time" label="提交申请时间"/>
      <el-table-column prop="status" label="审核状况">
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status==='待审核'">{{scope.row.status}}</el-tag>
          <el-tag type="success" v-if="scope.row.status==='审核通过'">{{scope.row.status}}</el-tag>
          <el-tag type="danger"  v-if="scope.row.status==='审核不通过'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核说明"/>
      <el-table-column label="操作" width="100" >
        <template #default="scope" v-if="data.user.role === 'USER'">
          <el-button :disabled="scope.row.status !== '待审核'" type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button :disabled="scope.row.status !== '待审核'" type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
        </template>
        <template #default="scope" v-if="data.user.role === 'ADMIN'">
          <el-button :disabled="scope.row.status !== '待审核'" type="primary" icon="Edit"  @click="handleEdit(scope.row)">审核</el-button>
        </template>
      </el-table-column>>
    </el-table>
  </div>

  <!--  分页栏-->
  <div style="margin-bottom: 5px" class="card">
    <el-pagination
        v-model:current-page="data.PageNum"
        v-model:page-size="data.PageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[5, 10, 15, 20]"
        :total="data.total"
        @current-change="load"
        @size-change="load"
    />
  </div>
</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utlis/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  PageNum:1,
  PageSize:5,
  total:0,
  title:null,
  tableData:[],
  formVisible:false,
  form:{},
  //校验规则
  rules:{
    title:[
      {required:true,message:"请填写请假标题",trigger:'blur'}
    ],
    content:[
      {required:true,message:"请填写请假内容",trigger:'blur'}
    ],
  },
})

const formRef = ref(null)

const load = () =>{
  request.get('/administrator_review/selectPage',{
    params:{
      pageNum:data.PageNum,
      pageSize:data.PageSize,
      title:data.title,
    }
  }).then(res =>{
    if (res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else{
      ElMessage.error(res.message)
    }
  })
}
load()

const reset = ()=>{
  data.title = null
  load()
}

const handleAdd = ()=>{
  data.form = {}
  data.formVisible = true
}

const add = ()=>{
  request.post('administrator_review/add',data.form).then(res=>{
    if (res.code === "200"){
      ElMessage.success("提交成功，等待管理员审核")
      data.formVisible = false;
      load()
    }else{
      ElMessage.error(res.message)
    }
  })
}

const update = () =>{
  request.put('administrator_review/update',data.form).then(res =>{
    if (res.code === "200"){
      ElMessage.success("操作成功")
      data.formVisible = false;
      load()
    }else{
      ElMessage.error(res.message)
    }
  })
}

const save = () =>{
  formRef.value.validate(valid =>{
    if(valid){
      data.form.id ? update() : add();
    }
  })
}

const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const del = (id) =>{
  ElMessageBox.confirm("删除后无法恢复，您确定删除这行数据吗？","删除确认",{type:"warning"}).then(res=>{
    request.delete("administrator_review/delete/" + id).then(res => {
      if (res.code === "200") {
        ElMessage.success('删除数据成功');
        load();
      } else {
        ElMessage.error(res.message);
      }
    })
  }).catch(err=>{})
}
</script>
