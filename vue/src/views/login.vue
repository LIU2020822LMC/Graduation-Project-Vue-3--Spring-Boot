<template>
  <div class="bg">
    <div  style="width:350px;background-color: #ffffff;border-radius: 8px;box-shadow: rgba(6, 24, 44, 0.4) 0px 0px 0px 2px, rgba(6, 24, 44, 0.65) 0px 4px 6px -1px, rgba(255, 255, 255, 0.08) 0px 1px 0px inset;padding: 40px 20px">
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">欢 迎 登 录</div>
        <el-form-item prop="username"  >
          <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password"  >
          <el-input v-model="data.form.password" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
        </el-form-item>
         <el-form-item prop="role">
           <el-select size="large" style="width: 100%" v-model="data.form.role">
             <el-option label="管理员" value="ADMIN"></el-option>
             <el-option label="普通用户" value="USER"></el-option>
           </el-select>
         </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%" size="large" type="primary" @click="login">登 录</el-button>
        </div>
        <div style="text-align: right">
          还没有账号？请<a style="color:#076beb" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import request from "@/utlis/request.js";
const formRef = ref()
const  data = reactive({
    form:{role:"ADMIN"},
    rules:{
      username:[
        {required:true,message:"请输入账号",trigger:'blur'},
      ],
      password:[
        {required:true,message:"请输入密码",trigger:'blur'},
        {min:6,message:"密码最少6位",trigger:'blur'}
      ],
    }
})

const login = () => {
  formRef.value.validate((valid) =>{
    if(valid){
      request.post("/login",data.form).then(res=>{
        if(res.code === "200"){
          //存储用户信息
          localStorage.setItem("code_user",JSON.stringify(res.data || {}));
          ElMessage.success("登陆成功");
          if("USER" === res.data.role){
            location.href = 'front/home'
          }else{
            router.push('/')//登陆成功之后转到http://localhost:5173/manager/home这个页面
          }
        }
        else{
          ElMessage.error(res.message);
        }
      })
    }
  })
}
</script>

<style scoped>
.bg{
  height: 100vh;
  background-image: url("@/assets/images/LoginBG1.png");
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-size: cover;
}
</style>

