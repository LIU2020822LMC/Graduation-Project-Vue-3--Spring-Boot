<template>
  <div class="bg">
    <div  style="width:350px;background-color: #ffffff;border-radius: 8px;box-shadow: rgba(6, 24, 44, 0.4) 0px 0px 0px 2px, rgba(6, 24, 44, 0.65) 0px 4px 6px -1px, rgba(255, 255, 255, 0.08) 0px 1px 0px inset;padding: 40px 20px">
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">欢 迎 注 册</div>
        <el-form-item prop="username"  >
          <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password"  >
          <el-input v-model="data.form.password" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="confirmpassword"  >
          <el-input v-model="data.form.confirmpassword" show-password autocomplete="off" prefix-icon="Lock" placeholder="请再次确认密码" />
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%;background-color: #248243;border-color:#248243 "  size="large" type="primary" @click="register">注 册</el-button>
        </div>
        <div style="text-align: right">
          已有账号？请<a style="color:#0f43e6;" href="/login">登录</a>
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
//再次确认密码
const validatePassWord = (rule, value, callback) => {
  //value 表示用户输入的确认密码
  if (value !== data.form.password) {
    callback(new Error("两次输入的密码不匹配！"))
  } else {
    callback()
  }
}
const formRef = ref()
const  data = reactive({
    form:{},
    rules:{
      username:[
        {required:true,message:"请输入账号",trigger:'blur'},
      ],
      password:[
        {required:true,message:"请输入密码",trigger:'blur'},
        {min:6,message:"密码最少6位",trigger:'blur'}
      ],
      confirmpassword:[
        {required:true,message:"请再次输入密码",trigger:'blur'},
        {validator:validatePassWord, trigger: 'blur' },
      ],
    }
})
const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post("/register",data.form).then(res=>{
        if(res.code === "200"){
          ElMessage.success("注册成功");
          router.push("/login") //注册成功之后转到http://localhost:5173/login这个页面
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
  background-image: url("@/assets/images/LoginBG2.png");
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-size: cover;
}
</style>

