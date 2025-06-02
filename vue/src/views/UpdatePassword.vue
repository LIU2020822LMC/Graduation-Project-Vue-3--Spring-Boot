<template>
<div class="card" style="width: 100%;">
  <div style="font-weight: bold;font-size: 18px;text-align: center">修改密码</div>
  <el-form status-icon ref="formRef" :rules = "data.rules" :model="data.user" label-width="100px" style="padding: 20px 70px 20px 0">
    <el-form-item prop="password" label="原密码" >
      <el-input v-model="data.user.password" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入原密码" />
    </el-form-item>
    <el-form-item prop="newPassword" label="新密码" >
      <el-input v-model="data.user.newPassword" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入新密码" />
    </el-form-item>
    <el-form-item prop="confirmPassword" label="确认密码" >
      <el-input v-model="data.user.confirmPassword" show-password autocomplete="off" prefix-icon="Lock" placeholder="请再次输入新密码" />
    </el-form-item>
  </el-form>
  <div style="text-align: center">
    <el-button type="primary" style="padding: 20px 45px" @click="updatePassword">保存</el-button>
  </div>
</div>
</template>

<script setup>
import {reactive,ref} from "vue";
import request from "@/utlis/request.js";
import {ElMessage} from "element-plus";


const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  rules:{
    password:[
      {required:true,message:"请填写账户原密码",trigger:'blur'},
      {min:6,message:"密码最少6位",trigger:'blur'}
    ],
    newPassword:[
      {required:true,message:"请填写账户新密码",trigger:'blur'},
      {min:6,message:"密码最少6位",trigger:'blur'}
    ],
    confirmPassword:[
      {required:true,message:"请再次填写账户新密码",trigger:'blur'},
      {min:6,message:"密码最少6位",trigger:'blur'}
    ],
  }
})

const formRef = ref()

const updatePassword = () =>{
  formRef.value.validate(valid =>{
    if(valid){
      request.post('/updatePassword',data.user).then(res =>{
        if(res.code === "200"){
          ElMessage.success("修改成功")
          setInterval(()=>{
            localStorage.removeItem("code_user")
            location.href="/login";
          },500)
        }
        else{
          ElMessage.error(res.message)
        }
      })
    }
  })
}
</script>


