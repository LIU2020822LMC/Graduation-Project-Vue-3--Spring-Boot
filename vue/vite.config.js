import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite' //自动导入vue中的组件
import components from 'unplugin-vue-components/vite' //自动导入ui-组件
import {ElementPlusResolver} from "unplugin-vue-components/resolvers";


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  //按需从element-plus导入
    AutoImport({
      resolvers:[ElementPlusResolver()],
    }),
    components({
      resolvers:[
          ElementPlusResolver({importStyle:"sass"})
      ],
    })
  ],
css:{
    preprocessorOptions:{
      scss:{
        additionalData:'@use "@/assets/css/index.scss" as *;',
      },
    },
},
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
