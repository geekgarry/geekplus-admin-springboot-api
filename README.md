## geekplus-admin-springboot-api
#### Springboot后台管理系统,使用SpringBoot+mybatis+reids，权限框架采用shiro，结合jwt实现用户认证，逆向工程生成模版
Springboot后台管理系统,使用SpringBoot+mybatis+reids，权限框架采用shiro，逆向工程生成模版 项目中只有最基本的系统管理的部分，需要增加功能利用freemarker模版生成代码
权限管理部分采用shiro，jwt验证实现无状态的token认证 项目采取前后端分离的开发，前端使用vue框架开发 
项目使用websocket主动推送消息，代码文件common/core/socket/WebSocketServer.java，相关配置在framework/config中，注意websocket使用要配置SSL
> 主要模块有用户管理，角色管理，菜单权限管理，系统通知，日志管理，部门管理，代码生成等

> 用户登录密码默认为123456
##### [前端项目] [https://github.com/geekgarry/geekplus-admin-vue-ui](https://github.com/geekgarry/geekplus-admin-vue-ui)

##### [管理系统] shiro+session版本 [https://github.com/geekgarry/geekplus-admin-shiro](https://github.com/geekgarry/geekplus-admin-shiro)

