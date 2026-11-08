import request from '@/utils/request.js'

//调用注册接口的函数
export const userRegisterservice = (registerData) => {
    const params = new URLSearchParams()
    for (const key in registerData) {
        params.append(key, registerData[key])
    }
    return request({
        url: '/user/register',
        method: 'POST',
        data: params
    })
}
//调用登录接口的函数
export const userLoginservice = (loginData) => {
    const params = new URLSearchParams()
    for (const key in loginData) {
        params.append(key, loginData[key])
    }
    return request({
        url: '/user/login',
        method: 'POST',
        data: params
    })
}

//调用获取用户信息接口的函数
export const userInfoService = () => {
    return request({        
        url: '/user/userInfo',
        method: 'GET',
    })
}   
//调用修改用户信息接口的函数
export const userUpdateService = (updateData) => {
    return request({
        url: '/user/update',
        method: 'PUT',
        data: updateData
    })
}
//调用修改用户头像接口的函数
export const userUpdateAvatarService = (avatarUrl) => {
    return request({
        url: '/user/updateAvatar',
        method: 'patch',
        params: { avatarUrl }      // 放 query，参数名要和后端一致
    })
}
//调用重置密码接口的函数
export const userUpdatePwdService = (pwdData) => {
    return request({
        url: '/user/updatePwd',
        method: 'patch',
        data: pwdData
    })
}
