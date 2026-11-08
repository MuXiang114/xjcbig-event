//定义store
import { defineStore } from 'pinia'
import { ref } from 'vue'
//定义token的store
export const useTokenStore = defineStore('token', () => {
    const token = ref('')
    const setToken = (newToken) => {
        token.value = newToken
    }
    const removeToken = () => {
        token.value = ''
    }
    return { token, setToken, removeToken }

},{
    persist: true//持久化存储
});