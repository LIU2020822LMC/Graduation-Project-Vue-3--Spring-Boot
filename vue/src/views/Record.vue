<template>
  <div>
    <div class="card" style="margin-bottom: 5px"  v-if="data.user.role === 'ADMIN'">
      <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.userName" placeholder="请输入借阅人查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load" >查 询</el-button>
      <el-button  @click="reset" >重 置</el-button>
    </div>
    <div style="margin-bottom: 5px" class="card" >
      <el-table :data="data.tableData" style="width: 100%;max-height: 540px; overflow-y: auto" :header-cell-style="{color:'#110101',background:'#9cceed'}">
        <el-table-column prop="bookImg" label="图书封面"  >
          <template #default="scope">
            <el-image v-if="scope.row.bookImg"
                      :src="scope.row.bookImg"
                      :preview-src-list="[scope.row.bookImg]"
                      :preview-teleported="true"
                      style="width: 50px;height: 50px;border-radius: 10%; display:block"/>
          </template>
        </el-table-column>
        <el-table-column prop="bookName" label="图书名字"/>
        <el-table-column prop="bookAuthor" label="图书作者"/>
        <el-table-column prop="userName" label="借阅人"/>
        <el-table-column prop="time" label="借阅时间"/>
        <el-table-column prop="status" label="审核状况">
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status==='待审核'">{{scope.row.status}}</el-tag>
            <el-tag type="success" v-if="scope.row.status==='审核通过'">{{scope.row.status}}</el-tag>
            <el-tag type="danger"  v-if="scope.row.status==='审核不通过'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="审核说明"/>
        <el-table-column label="操作" width="100" v-if="data.user.role==='ADMIN'">
          <template #default="scope" >
            <el-button :disabled="scope.row.status !== '待审核'" type="primary" icon="Edit"  @click="handleEdit(scope.row)">审核</el-button>
          </template>
        </el-table-column>>
      </el-table>
    </div>
    <el-dialog title="借阅信息" v-model="data.formVisible"  width="500" destroy-on-close="true">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 70px 20px 0">
        <el-form-item label="ID" v-if="data.form.id"> <!-- 仅编辑时显示ID -->
          <el-input v-model="data.form.id" autocomplete="off" disabled />
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
  userName:null,
  PageNum:1,
  PageSize: 5,
  Total:0,
  tableData:[],
  formVisible:false,
  form:{},
})

const formRef = ref(null)

const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

//数据刷新加载
const load = () => {
  request.get('record/selectPage',{
    params: {
      pageNum: data.PageNum, // 当前页码（第几页）
      pageSize: data.PageSize, // 每页显示数据量
      userName:data.userName,//搜索条件（按标题搜索）
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
  request.put("record/update", data.form).then(res =>{
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
  request.post('record/add', data.form).then(res => {
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
      update()
    }
  })
}


//重置按钮函数
const reset = () =>{
  data.userName = null;
  load()
}


</script>