<template>
  <div style="color: #110101">
<!--    头部区域开始-->
    <div style="height: 60px; display:flex;">
      <div style="width: 280px; display:flex;align-items: center;padding-left: 20px;background-image: linear-gradient(to right, #ff8177 0%, #ff867a 0%, #ff8c7f 21%, #f99185 52%, #cf556c 78%, #b12a5b 100%);">
        <img style="margin-left: 1px; width: 60px;height: 50px;border-radius: 10%" src="@/assets/images/logo.jpg" alt="图片加载不出来了">
        <span style="font-size: 20px;font-weight: bolder;color: #ffffff">LMC-2025毕设项目</span>
      </div>
      <div style="flex:1;border-bottom: 1px solid #ddd;display: flex;align-items: center;">
        <span style="margin: 5px; cursor: pointer;" @click="router.push('/manager/home')">首页</span>/<span style="margin-left: 5px">{{ router.currentRoute.value.meta.name}}</span>
      </div>
      <div style="width:fit-content;display: flex;align-items: center;border-bottom: 1px solid #ddd">
        <el-dropdown style="padding-right: 10px">
          <div style="display: flex;align-items: center;padding-right: 20px;">
            <img v-if="data.user?.avatar" style="width: 50px;height: 50px;border-radius: 50%" :src="data.user?.avatar "/>
            <img v-else style="width: 50px;height: 50px;border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="图片显示错误">
            <span style="padding-left: 5px;">{{ data.user?.name }}</span>
          </div>
          <template #dropdown >
            <el-dropdown-menu >
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu >
          </template>
        </el-dropdown>

      </div>
    </div>
<!--    头部区域结束-->

<!--    下方区域开始-->
    <div style="display: flex">
<!--      菜单区域开始-->
      <div style="width: 280px">
        <el-menu router :default-openeds="['1','2']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px);height:100%">
          <el-menu-item index="/manager/home">
            <el-icon><Menu /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice" v-if="data.user.role ==='ADMIN'">系统公告</el-menu-item>
            <el-menu-item index="/manager/notice" v-else>公告信息</el-menu-item>
            <el-menu-item index="/manager/category">攻略分类</el-menu-item>
            <el-menu-item index="/manager/introduction" >旅游攻略</el-menu-item>
            <el-menu-item index="/manager/administrator_review" >请假申请</el-menu-item>
            <el-menu-item index="/manager/book" >图书信息</el-menu-item>
            <el-menu-item index="/manager/record" >图书借阅记录</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">普通用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
<!--      菜单区域结束-->

<!--      数据渲染区开始-->
      <div style="flex: 1;width: 0;padding: 10px;background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);">
        <RouterView @updateUser = "updateUser"/>
      </div>
<!--      数据渲染区结束-->
    </div>
<!--    下方区域结束-->
  </div>
</template>
<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}")
})

//退出重新登录事件
const logout = ()=>{
  localStorage.removeItem("code_user")
  location.href="/login";
}

const updateUser = () =>{
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

if(!data.user?.id){
  location.href = '/login';
}
let userStr = localStorage.getItem('code_user');
if(userStr){
  let user = JSON.parse(userStr)
}else {
  location.href = '/login';
}

</script>
<style>
.el-menu{
  background-image: linear-gradient(to right, #ff8177 0%, #ff867a 0%, #ff8c7f 21%, #f99185 52%, #cf556c 78%, #b12a5b 100%);
  border-image:linear-gradient(to right, #ff8177 0%, #ff867a 0%, #ff8c7f 21%, #f99185 52%, #cf556c 78%, #b12a5b 100%);
}
.el-menu .is-active{
  background-image: linear-gradient(to right, #ffecd2 0%, #fcb69f 100%);
  color: #266cca;
}

.el-sub-menu__title{
  color: #ffffff;
  background-image: linear-gradient(to right, #ff8177 0%, #ff867a 0%, #ff8c7f 21%, #f99185 52%, #cf556c 78%, #b12a5b 100%);
}

.el-sub-menu__title:hover{
  color: #110101;

}
.el-menu-item{
  color: #ffffff;
}
.el-menu-item:not(.is-active):hover{
  color: #ffffff;
  background-image: linear-gradient(to right, #ffecd2 0%, #fcb69f 100%);
}
.black{
  color: #110101;
}
.el-dropdown{
  cursor: pointer;
}
.el-tooltip__trigger{
  outline: none;
}
.el-menu-inline.el-menu-item{
  padding-left: 50px !important;
}

</style>