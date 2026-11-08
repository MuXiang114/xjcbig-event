import { defineStore } from "pinia";
import { ref } from "vue";

const useUserinfostore = defineStore('userinfo', () => {
    // 定义用户信息的状态
    const info = ref({});
    // 设置用户信息的方法
    const setinfo = (newinfo) => {
        info.value = newinfo;
    }
    // 清除用户信息的方法
    const removeinfo = () => {
        info.value = {};
    }
    // 清除用户信息的方法
    const clearinfo = () => {
        info.value = {};
    }
    return { info, setinfo, removeinfo, clearinfo }

}, { persist: true })

export { useUserinfostore }
