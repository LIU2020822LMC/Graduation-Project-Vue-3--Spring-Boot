<template>
  <div>
    <div class="card" v-if="data.user.role === 'USER'">欢迎各位毕业生使用本系统</div>
    <div class="card" style="margin-top: 5px;width: 100%;" >
      <div style="font-size: 20px;margin-bottom: 20px" v-if="data.user.role === 'USER'">系统公告</div>
      <div style="max-height: 1000px; overflow-y: auto">
      <el-timeline  style="max-width: 600px" v-if="data.user.role === 'USER' ">
        <el-timeline-item :timestamp="item.time" color="#409EFF" placement="top" v-for="item in data.noticeData" :key="item.id">
          <el-card>
            <h3>{{item.title}}</h3>
            <p>{{item.content}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
        <div v-else >
          <div class="card" style="height: 400px;width: 100%;" id="line"></div>
          <div style="display: flex;margin-top: 10px" >
            <div class="card" style="height: 400px;width: 50%" id="pie"></div>
            <div class="card" style="height: 400px;width: 50%;margin-left: 10px" id="bar"></div>
          </div>
        </div>
    </div>

  </div>
  </div>
</template>
<script  setup>
import {reactive,onMounted} from "vue";
import request from "@/utlis/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  noticeData:[]
})

const loadNotice = ()=>{
  request.get('/notice/selectAll').then(res => {
    if(res.code === '200'){
      data.noticeData = res.data
    }else{
      ElMessage.error(res.message)
    }
  })
}
loadNotice();

//加载饼图函数
const loadPie = ()=>{
  request.get('echarts/pie').then(res => {
    if(res.code === '200'){
      let chartDom = document.getElementById('pie');
      let myChart = echarts.init(chartDom);
      pie_option.series[0].data = res.data
      myChart.setOption(pie_option);
    }
  })
}

//加载柱状图函数
const loadBar = ()=>{
  request.get('echarts/bar').then(res => {
    if(res.code === '200'){
      let chartDom = document.getElementById('bar');
      let myChart = echarts.init(chartDom);
      bar_option.xAxis.data = res.data.xAxis;
      bar_option.series[0].data = res.data.yAxis;
      myChart.setOption(bar_option);
    }
  })
}

//加载折线图函数
const loadLine = ()=>{
  request.get('echarts/line').then(res => {
    if(res.code === '200'){
      let chartDom = document.getElementById('line');
      let myChart = echarts.init(chartDom);
      line_option.xAxis.data = res.data.xAxis;
      line_option.series[0].data = res.data.yAxis;
      myChart.setOption(line_option);
    }
  })
}

onMounted(()=>{
  loadPie();
  loadBar();
  loadLine();
})


//饼图
let pie_option = {
  title: {
    text: '不同分类下用户发布旅游攻略帖子的数量',
    subtext: '统计维度：攻略分类',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter:'{a}<br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比', //鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center:['50%','60%'],
      data: [

      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

//柱状图
let bar_option = {
  title: {
    text: '不同用户发布帖子数量Top5',
    subtext: '统计维度：用户昵称',
    left: 'center'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },

  xAxis: {
    type: 'category',
    data: [],
    name:"昵称",
    axisLabel:{
      show:true,
      interval:0,
      rotate:-60,
      inside:false,
      margin:6,
    }
  },
  yAxis: {
    type: 'value',
    name:"攻略数量",
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [],
      type: 'bar'
    }
  ]
};

//平滑折线图
let line_option = {
  title: {
    text: '最近一周每天平台用户发布的帖子数量',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  xAxis: {
    name:'日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    name:'攻略数量',
    type: 'value'
  },
  series: [
    {
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true
    }
  ]
};
</script>