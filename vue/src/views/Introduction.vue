<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 280px;margin-left: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load" >查 询</el-button>
      <el-button  @click="reset" >重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'USER'">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
    </div>

    <div style="margin-bottom: 5px" class="card" >
      <el-table :data="data.tableData" style="width: 100%;max-height: 540px; overflow-y: auto"  :header-cell-style="{color:'#110101',background:'#9cceed'}">
        <el-table-column label="攻略主图" width="100" >
          <template #default="scope">
            <el-image v-if="scope.row.img"
                      :src="scope.row.img"
                      :preview-src-list="[scope.row.img]"
                      :preview-teleported="true"
                      style="width: 50px;height: 50px;border-radius: 10px; display:block"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="攻略标题"  />
        <el-table-column prop="categoryTitle" label="攻略分类"  />
        <el-table-column prop="username" label="发布用户"  />
        <el-table-column prop="content" label="攻略内容">
          <template v-slot="scope">
            <el-button type="info" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>>
      </el-table>
    </div>

    <el-dialog title="攻略信息" v-model="data.formVisible"  width=60% destroy-on-close="true">
      <el-form ref="formRef" :model="data.form"  label-width="100px" style="padding: 20px 70px 20px 0">
        <el-form-item label="ID" v-if="data.form.id"> <!-- 仅编辑时显示ID -->
          <el-input v-model="data.form.id" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item prop="img" label="攻略主图" >
          <el-upload
              action="http://localhost:2020/files/upload"
              :headers="{token: data.user.token}"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary" >上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="攻略标题" >
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入攻略标题" />
        </el-form-item>
        <el-form-item prop="title" label="攻略分类" >
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择攻略分类"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="攻略内容" >
          <div style="border: 1px solid #ccc;width: 100%">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :mode="mode"
            />
            <Editor
              style="height:500px;overflow-y:hidden;"
              v-model="data.form.content"
              :mode="mode"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="攻略内容" v-model="data.viewVisible"  width=60% destroy-on-close="true">
      <div v-html="data.content"></div>
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
import {Search} from "@element-plus/icons-vue";
import {onBeforeUnmount,reactive,ref,shallowRef} from "vue";
import request from "@/utlis/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' //引入css
import {Editor,Toolbar} from '@wangeditor/editor-for-vue'

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  title:null,
  PageNum:1,
  PageSize: 5,
  Total:0,
  tableData:[],
  formVisible:false,
  form:{},
  content:null,
  viewVisible:false,
  categoryData:[],
})

/*wangEditor5 初始化开始*/
const editorRef = shallowRef() // 编辑器实例，必须使用 shallowRef
const mode = 'default'
const editorConfig = {MENU_CONF:{}}
//图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers:{
    token:data.user.token
  },
  server:'http://localhost:2020/files/wang/upload', //服务端图片上传接口
  fieldName: 'file' // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if(editor == null) return
  editor.destroy()
})
//记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/*wangEditor5 初始化结束*/

const load = () =>{
  request.get("introduction/selectPage",{
    params:{
      pageNum: data.PageNum, // 当前页码（第几页）
      pageSize: data.PageSize, // 每页显示数据量
      title:data.title,//搜索条件（按标题搜索）
    }
  }).then(res => {
    if(res.code === "200"){
      data.tableData = res.data?.list;
      data.Total = res.data?.total;
    }else{
      ElMessage.error(res.message)
    }
  })
}
load()

const reset = () =>{
  data.title = null;
  load()
}

const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}

const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}

const handleFileSuccess = (res)=>{
  data.form.img = res.data
}

const add = () =>{
  request.post("/introduction/add",data.form).then(res => {
    if(res.code === "200"){
      ElMessage.success("新增成功")
      data.formVisible = false
      load()
    }else{
      ElMessage.error(res.message)
    }
  })
}

const update = () =>{
  request.put('/introduction/update',data.form).then(res =>{
    if(res.code === '200'){
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    }else{
      ElMessage.error(res.message)
    }
  })
}

const save = () =>{
  data.form.id ? update() : add()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const del = (id) =>{
  ElMessageBox.confirm("删除后无法恢复，您确定删除这行数据吗？","删除确认",{type:"warning"}).then(res=>{
    request.delete("introduction/delete/" + id).then(res => {
      if (res.code === "200") {
        ElMessage.success('删除数据成功');
        load();
      } else {
        ElMessage.error(res.message);
      }
    })
  }).catch(err=>{})
}

//获取攻略分类里面的选项
const loadCategory = () =>{
  request.get("/category/selectAll").then(res => {
    if(res.code === "200"){
      data.categoryData = res.data
    }else {
      ElMessage.error(res.message)
    }
  })
}
loadCategory()
</script>
