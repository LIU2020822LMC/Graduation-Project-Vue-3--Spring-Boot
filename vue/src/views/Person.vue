<template>
  <div class="card" style="width: 100%;">
    <div style="font-weight: bold;font-size: 18px;text-align: center">个人中心</div>
    <el-form ref="formRef" :model="data.user"  label-width="100px" style="padding: 20px 70px 20px 0">
      <el-form-item label="ID" v-if="data.user.id"> <!-- 仅编辑时显示ID -->
        <el-input v-model="data.user.id" autocomplete="off" disabled />
      </el-form-item>
      <el-form-item prop="username" label="账号" >
        <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号" />
      </el-form-item>
      <el-form-item prop="password" label="密码" >
        <el-input v-model="data.user.password" autocomplete="off" placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item prop="name" label="用户名" >
        <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item prop="email" label="邮箱" >
        <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱"/>
      </el-form-item>
      <el-form-item prop="phone" label="电话" >
        <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话"/>
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
    <div style="text-align: center">
      <el-button type="primary" style="padding: 20px 45px" @click="UpdateSave">保存</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utlis/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
})

const handleFileSuccess = (res) =>{
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])

const UpdateSave = () =>{
  let url
  if(data.user.role === 'ADMIN'){
    url = '/admin/update'
  }
  if(data.user.role === 'USER'){
    url = '/user/update'
  }
  request.put(url,data.user).then(res =>{
      if(res.code === "200"){
        ElMessage.success("数据更新保存成功")
        localStorage.setItem("code_user",JSON.stringify(data.user))
        emit('updateUser')
      }
  })
}
</script>
