<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router'
const router = useRouter()

//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
const registerData = ref({
    username: '',
    password: '',
    rePassword: ''
})
// 检查两次输入的密码是否一致
const checkRepassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== registerData.value.password) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}
// 表单验证规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度5-16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度5-16位非空字符', trigger: 'blur' }],
    rePassword: [
        { validator: checkRepassword, trigger: 'blur' },
    ]
}
//运用函数调用注册接口
import { userRegisterservice,userLoginservice } from '@/api/user.js'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
const register = async () => {
    let result = await userRegisterservice(registerData.value)
    /*if (result.code == 0) {
        alert(result.msg ? result.msg : '注册成功，请登录')
    } else {
        alert('注册失败，请重试')
    }*/
   ElMessage.success(result.msg ? result.msg : '注册成功，请登录')  
}
const login = async () => {
    let result = await userLoginservice(registerData.value)
    /*if (result.code == 0) {
        alert(result.msg ? result.msg : '登录成功')
    } else {
        alert('登录失败，请重试')
    }*/
   ElMessage.success(result.msg ? result.msg : '登录成功')  
   //将token存储到pinia中
   tokenStore.setToken(result.data)
   //用路由跳转
   router.push('/')

}
//函数清空数据模型数据
const clearregisterData = () => {
    registerData.value.username = ''
    registerData.value.password = ''
    registerData.value.rePassword = ''
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearregisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <!-- 绑定数据和校验复用注册代码 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">
                        登录
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearregisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
            url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>
