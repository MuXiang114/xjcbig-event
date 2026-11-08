<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token.js'
import { userUpdatePwdService } from '@/api/user.js'

const router = useRouter()
const tokenStore = useTokenStore()
const formRef = ref()

// 重置密码表单数据（字段名用下划线，和后端 params.get 保持一致）
const resetModel = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})

// 校验规则
const rules = {
    old_pwd: [
        { required: true, message: '请输入原密码', trigger: 'blur' },
        { pattern: /^\S{5,16}$/, message: '密码长度应为 5~16 位', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { pattern: /^\S{5,16}$/, message: '密码长度应为 5~16 位', trigger: 'blur' }
    ],
    re_pwd: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== resetModel.value.new_pwd) {
                    callback(new Error('两次输入的密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
}

// 提交重置密码
const resetPassword = async () => {
    // 先校验，不通过直接返回
    const valid = await formRef.value.validate().catch(() => false)
    if (!valid) return

    try {
        await userUpdatePwdService(resetModel.value)
        ElMessage.success('密码修改成功，请重新登录')
        // 后端已删除旧 token，这里清空本地并跳登录页
        tokenStore.removeToken()
        router.push('/login')
    } catch {
        // 原密码错误、两次不一致等错误提示已由 request 拦截器统一弹出，这里无需重复提示
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-form ref="formRef" :model="resetModel" :rules="rules" label-width="100px" size="large"
            style="max-width: 480px">
            <el-form-item label="原密码" prop="old_pwd">
                <el-input v-model="resetModel.old_pwd" type="password" show-password placeholder="请输入原密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="new_pwd">
                <el-input v-model="resetModel.new_pwd" type="password" show-password placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="re_pwd">
                <el-input v-model="resetModel.re_pwd" type="password" show-password placeholder="请再次输入新密码"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="resetPassword">修改密码</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>
