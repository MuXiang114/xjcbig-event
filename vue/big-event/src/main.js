import './assets/main.scss'

import { createApp } from 'vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from '@/router/index.js'
import { createPinia } from 'pinia'
import {createPersistedState} from 'pinia-persistedstate-plugin'
import locale from 'element-plus/dist/locale/zh-cn.js'

const pinia = createPinia()
pinia.use(createPersistedState())
createApp(App).use(ElementPlus, { locale }).use(router).use(pinia).mount('#app') 
