# cake-mall
个人蛋糕商城项目（后台版）
>[前端版](https://github.com/tanyueran/cake-mall-web)

# 模块分布及功能介绍
```text
mall-gateway
网关、认证过滤（微服务）

mall-generator
代码生成工具

mall-admin
登录、注册，用户管理模块（微服务）

mall-order
订单管理（微服务）

mall-cake
蛋糕管理（微服务）

mall-utils
一些独立于业务的服务（文件上传下载，消息服务）（微服务）

mall-common
一些公用的工具类

```

## 技术选型环境等

### 后台技术及版本

| 名称                 | 版本          | 说明                          |
| -------------------- | ------------- | ----------------------------- |
| spring boot          | 2.2.2.RELEASE |                               |
| spring-cloud         | Hoxton.SR1    |                               |
| spring-cloud-alibaba | 2.1.0.RELEASE |                               |
| mybatis              |               | 持久层框架                    |
| mybatis-plus         |               | mybatis 插件                  |
| druid                |               | 数据库连接池                  |
| mysql                | 5.7.30        | 数据库                        |
| fastjson             | 1.2.74        | json转换工具                  |
| hutool-all           | 5.3.10        | 一个工具函数jar               |
| swagger2             | 2.9.2         | api接口展示工具               |
|                      |               |                               |
| lombok               | 1.18.16       |                               |
| minio                |               | 文件的存储服务                |
| nacos                | 1.3.0         | alibaba的服务注册发现和config |
| seata                | 0.9.0         | 分布式事务                    |



### 前台技术及版本

| 名称             | 版本   | 说明                  |
| ---------------- | ------ | --------------------- |
| react            | 17.0.1 |                       |
| react-router     | 5.2.0  | react路由管理工具     |
| react-loadable   | 5.5.0  | react组件异步加载工具 |
| react-redux      | 7.2.2  | react的redux结合      |
| redux            | 5.2.0  | 状态管理工具          |
| redux-thunk      | 2.3.0  | redux 异步控制工具    |
| antd             | 4.7.3  | react的组件库         |
| scss             |        |                       |
| mocker           |        |                       |
| axios            |        | ajax工具函数          |
| create-react-app |        | react脚手架创建工具   |
|                  |        |                       |

