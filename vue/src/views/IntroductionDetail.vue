<template>
  <div>
    <el-menu
        class="el-menu-demo"
        mode="horizontal"
    >
      <el-menu-item @click="logout">返回</el-menu-item>
    </el-menu>
  </div>
 <div style="width: 70%;margin: 20px auto">
   <div style="text-align: center;font-size:20px;font-weight: bold">{{ data.introductionData.title }}</div>
  <div style="margin-top:10px;display: flex ;align-items: center;justify-content: center;grid-gap:20px">
    <img :src="data.introductionData.userAvatar" alt="" style="width: 40px;height: 40px;border-radius: 50%;">
    <div>作者:{{data.introductionData.username}}</div>
    <div>所属分类:{{ data.introductionData.categoryTitle }}</div>
    <div style="color: #666666">发布时间:{{data.introductionData.time}}</div>
  </div>
  <div v-html="data.introductionData.content" style="margin-top: 50px"></div>
 </div>
</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utlis/request.js";
import {ElMessage} from "element-plus";



const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionId:router.currentRoute.value.query.id,
  introductionData:{},
})

const loadIntroduction = () =>{
  request.get(`introduction/selectById/${data.introductionId}`).then(res =>{
    if (res.code === '200'){
      data.introductionData = res.data
    }else{
      ElMessage.error(res.message)
    }
  })
}
loadIntroduction()

const logout = ()=>{
  location.href="/front/home";
}
</script>

