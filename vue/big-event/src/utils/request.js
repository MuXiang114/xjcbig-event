//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useTokenStore } from '@/stores/token.js'
import { useRouter } from 'vue-router';
import router from '@/router/index.js'
//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = '/api';
const instance = axios.create({ baseURL })
//添加请求拦截器
instance.interceptors.request.use(
    config => {
        //在发送请求之前做些什么
        const tokenStore = useTokenStore()
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    }
);

//添加响应拦截器
instance.interceptors.response.use(
    result => {
        if (result.data.code == 0) {
            return result.data;
        }
        ElMessage.error(result.data.message ? result.data.message : '服务异常');
        //异步操作的状态转换为失败
        return Promise.reject(result.data);
    },
    err => {
        //判断响应状态码是否为401未登录
        if (err.response.status == 401) {
            ElMessage.error('请先登录')
            router.push('/login')//跳转到登录页    
        } else {

            ElMessage.error('服务异常')
            //异步的状态转化成失败的状态
        }
        return Promise.reject(err);
    }
)

export default instance;