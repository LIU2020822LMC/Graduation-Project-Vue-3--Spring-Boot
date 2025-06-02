<template>
  <div >
    <el-menu
        :default-active="1"
        class="el-menu-demo"
        mode="horizontal"
    >
      <el-menu-item index="1">系统首页</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <div style="margin-bottom: 20px">
      <el-carousel height="300px">
<!--        <el-carousel-item v-for="item in data.carouselData" :key="item">-->
<!--          <img :src="item" alt="图片加载错误" style="height: 300px;width: 100%">-->
<!--        </el-carousel-item>-->
        <el-carousel-item v-for="item in data.introduction" :key="item">
          <img :src="item.img" alt="图片加载错误"
               style="height: 350px;width: 100%;cursor: pointer"
               @click="navTo('/front/introductionDetail?id=' + item.id)">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="width: 80%;margin: 20px auto">
      <div style="font-size: 22px;
           font-weight: bold;border-left: 5px solid #076beb;
           padding-left: 5px;
           height:30px;line-height: 30px">旅游攻略</div>
      <div style="margin-top: 20px">
        <el-row >
          <el-col v-for="item in data.introduction" :span="6" style="margin-top: 10px">
            <img :src="item.img" alt="图片加载错误"
                 @click="navTo('/front/introductionDetail?id=' + item.id)"
                 style="width: 95%;border-radius: 5px;cursor: pointer">
            <div @click="navTo('/front/introductionDetail?id=' + item.id)"
                style="font-size: 18px;font-weight: bold;margin-top: 10px;cursor: pointer">{{item.title}}</div>
            <div style="display: flex;align-items: center;margin-top: 10px;grid-gap: 10px">
              <img :src="item.userAvatar" alt="图片加载错误" style="width: 30px;height: 30px;border-radius:50%">
              <div style="font-size: 15px">{{item.username}}</div>
              <div style="color: #666666">{{item.time}}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utlis/request.js";
import {ElMessage} from "element-plus";
import lun1 from '@/assets/images/lun-1.jpg';
import lun2 from '@/assets/images/lun-2.jpg';
import lun3 from '@/assets/images/lun-3.jpg';
import lun4 from '@/assets/images/lun-4.jpg';

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  introduction:[],
  //轮播图片数据
  carouselData:[lun1,lun2,lun3,lun4]
})

const loadIntroduction = () =>{
  request.get('introduction/selectAll').then(res=>{
    if(res.code === "200"){
      data.introduction = res.data
      console.log("返回的数据:", res.data[0]);
    }else{
      ElMessage.error(res.message)
    }
  })
}
loadIntroduction()

const logout = ()=>{
  localStorage.removeItem("code_user")
  location.href="/login";
}

//跳转路由设置
const navTo = (url) => {
  location.href = url
}
</script>