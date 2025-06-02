import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path:'/',redirect:'/manager/home'},
    {path: '/manager',  component:()=> import('../views/Manager.vue'),
      children:[
          {path: '/manager/home', meta:{ name : '主页'}, component:()=> import('../views/Home.vue'),},
          {path: '/manager/admin',meta:{ name : '管理员信息 '},  component: ()=>import('../views/Admin.vue'),},
          {path: '/manager/user',meta:{ name : '普通用户信息 '},  component: ()=>import('../views/User.vue'),},
          {path: '/manager/person',meta:{ name : '个人中心 '},  component: ()=>import('../views/Person.vue'),},
          {path: '/manager/updatePassword',meta:{ name : '修改密码'},  component: ()=>import('../views/UpdatePassword.vue'),},
          {path: '/manager/notice',meta:{ name : '系统公告'},  component: ()=>import('../views/Notice.vue'),},
          {path: '/manager/introduction',meta:{ name : '旅游攻略'},  component: ()=>import('../views/Introduction.vue'),},
          {path: '/manager/category',meta:{ name : '攻略分类'},  component: ()=>import('../views/Category.vue'),},
          {path: '/manager/administrator_review',meta:{ name : '管理员请假审核'},  component: ()=>import('../views/Administrator_Review.vue'),},
          {path: '/manager/book',meta:{ name : '图书信息'},  component: ()=>import('../views/Book.vue'),},
          {path: '/manager/record',meta:{ name : '图书借阅'},  component: ()=>import('../views/Record.vue'),},
      ]
    },
    {path: '/front/home',  component: import('../views/Front.vue'),},
    {path: '/front/introductionDetail',  component: import('../views/IntroductionDetail.vue'),},
    {path: '/NotFound',  component: import('../views/404.vue'),},
    {path: '/login',  component: import('../views/login.vue'),},
    {path: '/register',  component: import('../views/Register.vue'),},
    {path: '/:pathMatch(.*)',  redirect:'/NotFound'},
  ],
})
export default router
