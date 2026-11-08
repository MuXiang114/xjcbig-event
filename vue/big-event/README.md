# 大事件后台管理系统（前端）

基于 Vue 3 + Element Plus 的文章内容管理后台，配套 Spring Boot 后端。

支持文章分类管理、文章发布与编辑、富文本正文、封面上传、个人资料修改、头像更换、密码重置，以及登录注册与 JWT 鉴权。

## 技术栈

| 类别 | 选型 |
| --- | --- |
| 框架 | Vue 3.5（`<script setup>` 组合式 API） |
| 构建 | Vite |
| UI | Element Plus 2.x + `@element-plus/icons-vue` |
| 状态 | Pinia（`pinia-plugin-persistedstate` 持久化） |
| 路由 | Vue Router 4 |
| 富文本 | `@vueup/vue-quill`（基于 Quill 2） |
| 请求 | Axios（统一封装请求 / 响应拦截器） |
| 样式 | SCSS |

## 功能模块

- **登录 / 注册**：表单校验、token 存储、登录态持久化
- **文章分类**：列表、新增、编辑、删除
- **文章管理**：分页列表、按分类与状态筛选、新增 / 编辑（抽屉表单）、封面上传（OSS）、富文本正文、删除
- **个人中心**：基本资料修改、头像更换、重置密码
- **退出登录**：二次确认后清除本地登录态并返回登录页

## 目录结构

```
src/
├── api/            # 接口封装（article.js、user.js）
├── assets/         # 图片与全局样式
├── router/         # 路由配置
├── stores/         # Pinia：token、用户信息（均持久化）
├── utils/          # axios 实例与拦截器
├── views/          # 页面
│   ├── Login.vue
│   ├── Layout.vue
│   ├── article/    # 文章分类、文章管理
│   └── user/       # 基本资料、更换头像、重置密码
├── App.vue
└── main.js
```

## 本地运行

```bash
# 1. 安装依赖
npm install

# 2. 启动开发服务器
npm run dev
```

默认访问 `http://localhost:5173`。

开发环境通过 Vite 代理把 `/api` 转发到后端 `http://localhost:8082`，配置见 `vite.config.js`：

```js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8082',
      changeOrigin: true,
      rewrite: path => path.replace(/^\/api/, '')
    }
  }
}
```

## 后端依赖

后端为 Spring Boot 3 + MyBatis + MySQL + Redis + 阿里云 OSS，仓库地址：

```
https://github.com/MuXiang114/-1-
```

启动前端前需先跑起后端，并确保：

- MySQL 中已建库 `big_event`
- Redis 已启动（默认 `localhost:6379`，用于存放登录 token）
- 后端端口为 `8082`

## 实现说明

- 登录返回的 token 写入 Redis 并设 1 小时过期，请求时通过 `Authorization` 头携带
- 请求拦截器统一注入 token；响应拦截器在 `code !== 0` 时提示后端返回的 `message`，`401` 时跳转登录页
- 文章封面走阿里云 OSS，上传接口需要登录态
- 新增与修改共用同一套抽屉表单：`articleModel` 带 `id` 走 `PUT`，不带 `id` 走 `POST`，提交前剔除后端不接受的 `createTime` / `updateTime`

## License

仅用于学习与技术交流。
