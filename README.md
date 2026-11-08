# 大事件 · 内容管理系统

一个前后端分离的文章管理后台。前端 Vue 3 + Element Plus，后端 Spring Boot 3 + MyBatis + MySQL + Redis，支持文章分类管理、文章发布与编辑、富文本正文、封面图上传（阿里云 OSS）、个人资料与头像修改、密码重置，以及基于 JWT + Redis 的登录鉴权。

## 功能

- **登录 / 注册**：JWT 签发，token 写入 Redis 并设过期时间
- **文章分类**：列表、新增、编辑、删除
- **文章管理**：分页查询、按分类与状态筛选、新增 / 编辑 / 删除、富文本正文、封面图上传
- **个人中心**：修改基本资料、更换头像、重置密码
- **鉴权**：登录拦截器统一校验 `Authorization`，未登录返回 401

## 技术栈

**前端**

| 类别 | 选型 |
| --- | --- |
| 框架 | Vue 3.5（`<script setup>`） |
| 构建 | Vite |
| UI | Element Plus 2.x + icons |
| 状态 | Pinia（持久化插件） |
| 路由 | Vue Router 4 |
| 富文本 | `@vueup/vue-quill`（Quill 2） |
| 请求 | Axios（统一拦截器） |

**后端**

| 类别 | 选型 |
| --- | --- |
| 框架 | Spring Boot 3.3.5 |
| 持久层 | MyBatis 3 + PageHelper |
| 数据库 | MySQL 8 |
| 缓存 | Redis（存放登录 token） |
| 鉴权 | java-jwt 4.0 |
| 校验 | Spring Validation + 自定义注解校验 |
| 对象存储 | 阿里云 OSS |

## 目录结构

```
.
├── sql/init.sql            # 建表脚本 + 演示数据
│
├── vue/big-event/          # 前端
│   ├── src/
│   │   ├── api/            # 接口封装
│   │   ├── assets/         # 图片、全局样式
│   │   ├── router/         # 路由
│   │   ├── stores/         # Pinia：token、用户信息
│   │   ├── utils/          # axios 实例与拦截器
│   │   └── views/          # 页面（登录、布局、文章、个人中心）
│   └── vite.config.js      # 开发代理：/api → localhost:8082
│
└── demo/GongCheng1/        # 后端
    ├── src/main/java/org/example/gongcheng1/
    │   ├── controller/     # 用户、分类、文章、文件上传
    │   ├── service/        # 业务接口与实现
    │   ├── mapper/         # MyBatis 接口 + ArticleMapper.xml
    │   ├── pojo/           # 实体类、统一返回 Result、分页 PageBean
    │   ├── config/         # Web 配置、拦截器注册
    │   ├── interceptors/   # 登录拦截器
    │   ├── anno/           # 自定义校验注解
    │   ├── validation/     # 校验实现
    │   ├── exception/      # 全局异常处理
    │   └── utils/          # JWT、阿里云 OSS 工具类
    └── src/main/resources/
        ├── application.yml
        └── org/example/gongcheng1/mapper/ArticleMapper.xml
```

## 快速开始

### 1. 准备环境

- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8
- Redis

### 2. 初始化数据库

仓库自带建表脚本 `sql/init.sql`，会创建 `big_event` 库、三张表和一份演示数据：

```bash
mysql -uroot -p < sql/init.sql
```

脚本会先删除同名表再重建，已存在的数据会被清空，请勿对生产库执行。

演示账号：

```
用户名：demo123
密码：  123456
```

### 3. 改配置

编辑 `demo/GongCheng1/src/main/resources/application.yml`，把数据库账号密码改成本机的：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/big_event
    username: root
    password: 你的数据库密码
  data:
    redis:
      host: localhost
      port: 6379
server:
  port: 8082
```

### 4. 启动后端

```bash
cd demo/GongCheng1
mvn spring-boot:run
```

或用 IDEA 打开 `demo/GongCheng1`，运行 `GongCheng1Application`。

后端跑在 `http://localhost:8082`。

### 5. 启动前端

```bash
cd vue/big-event
npm install
npm run dev
```

打开 `http://localhost:5173`。开发环境由 Vite 代理把 `/api` 转发到后端并去掉前缀，配置在 `vite.config.js`。

## 主要接口

| 模块 | 方法 | 路径 |
| --- | --- | --- |
| 用户 | POST | `/user/register` |
| 用户 | POST | `/user/login` |
| 用户 | GET | `/user/userInfo` |
| 用户 | PUT | `/user/update` |
| 用户 | PATCH | `/user/updateAvatar` |
| 用户 | PATCH | `/user/updatePwd` |
| 分类 | GET / POST / PUT / DELETE | `/category` |
| 分类 | GET | `/category/detail` |
| 文章 | GET / POST / PUT / DELETE | `/article` |
| 文章 | GET | `/article/detail` |
| 文件 | POST | `/upload` |

## 实现说明

- 登录成功后签发 JWT 并写入 Redis（1 小时过期），请求拦截器统一注入 `Authorization`
- 响应拦截器在 `code !== 0` 时提示后端返回的 `message`，401 时清除本地登录态并跳转登录页
- 文章新增与修改共用一个抽屉表单：`articleModel` 带 `id` 走 `PUT`，不带 `id` 走 `POST`
- 后端 `Article`、`User` 的 `createTime` / `updateTime` 未加 `@JsonFormat`，前端提交时必须剔除这两个字段，否则反序列化失败
- 封面图保存的是 OSS 完整地址，浏览器直接访问；部分历史数据引用了已失效的第三方 OSS 地址，重新上传即可

## 配置 OSS 凭证

`AliOssUtil` 不在代码里保存密钥，启动前需提供一对阿里云 AccessKey，二选一：

**方式一：本地配置文件（推荐）**

在 `demo/GongCheng1/src/main/resources/` 下新建 `oss.properties`：

```properties
accessKeyId=你的AccessKeyId
accessKeySecret=你的AccessKeySecret
```

该文件已在 `.gitignore` 中排除，不会被提交。

**方式二：环境变量**

设置 `OSS_ACCESS_KEY_ID` 和 `OSS_ACCESS_KEY_SECRET` 即可。

两者都没有时，程序启动会抛出明确提示，不会静默失败。

## License

仅用于学习与技术交流。
