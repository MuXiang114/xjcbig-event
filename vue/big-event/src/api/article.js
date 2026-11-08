import request from '@/utils/request'
import { useTokenStore } from '@/stores/token.js'



// 获取文章分类列表
export const articleCategoryListService = () => {

    return request({
        url: '/category',
        method: 'GET',

    })
}
//添加文章分类
export const ArticleCategoryaddService = (data) => {
    return request({
        url: '/category',
        method: 'POST',
        data: data
    })
}
//更新文章分类
export const ArticleCategoryupdateService = (data) => {
    return request({
        url: '/category',
        method: 'PUT',
        data: data
    })
}
//删除文章分类
export const ArticleCategorydeleteService = (id) => {
    return request({
        url: '/category?id=' + id,
        method: 'DELETE',
    })
}

//文章列表查询
export const ArticleListService = (params) => {
    return request({
        url: '/article',
        method: 'GET',
        params: params
    })
}

//文章添加
export const ArticleAddService = (data) => {
    const tokenStore = useTokenStore()
    return request({
        url: '/article',
        method: 'POST',
        data: data,
    })
}

//文章详情查询
export const ArticleDetailService = (id) => {
    return request({
        url: '/article/detail',
        method: 'GET',
        params: { id }
    })
}

//删除文章
export const ArticleDeleteService = (id) => {
    return request({
        url: '/article',
        method: 'DELETE',
        params: { id }
    })
}
//修改文章
export const ArticleUpdateService = (data) => {
    return request({
        url: '/article',
        method: 'PUT',
        data: data
    })
}
