<!--
 * @Author: Jason Zhang
 * @Date: 2022-02-20 11:20:21
 * @LastEditTime: 2022-02-21 08:51:29
 * @LastEditors: Please set LastEditors
 * @Description: Sumbission Files
 * @FilePath: /messageboard/Users/saneryee/Desktop/recruitment-java-backend-comments-tree-homework-QigongZhang/Submission.md
-->

# Submission

## Dev Environments:

- MacBookPro 15 with macOS Monterey 12.2.1

### Tools
- VSCode 1.64.2
- IntelliJ IDEA Community Edition 2021.3.2
- Postman
- chrome

### Backend:
- Java 8
- SpringBoot 2.6.3
- SpringSecurity
- Database: H2
- jjwt 0.11.2
- junit 4
- maven
- docker


### Frontend:
- Vue3
- Vuex4
- vee-validate
- axios
- bootstrap


## Install & Run

### Start SpringBoot Server

    cd ./codes/server/messageboard 
    ./mvnw spring-boot:run

端口：8080

### Start Vue3 Server

    cd ./codes/server/messageboard && ./mvnw spring-boot:run
    npm run serve

端口：8081

### Linux 环境下启动

    chmod +x install.sh
    ./install.sh

### Database

- H2数据库控制台：http://localhost:8080/h2-console

- 预插入6条Comment数据供测试: 


    
```
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Alan', 'This is Alan''s first comment', 0, '2022-02-18 16:39:00');

INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Alan', 'This is Alan''s second comment', 0, '2022-02-18 16:40:00');
    
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Bob', 'This is Bob''s first comment', 0, '2022-02-18 16:41:00');
    
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Cindy', 'This is Cindy''s reply to Alan''s first comment', 1, '2022-02-18 16:42:00');
    
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Bob', 'This is Bob''s reply to Cindy''s reply', 4, '2022-02-18 16:43:00');
    
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('David', 'This is David''s reply to Alan''s first comment', 1, '2022-02-18 16:44:00');
```
    



## Functions and Status:

- 用户注册 
  - 状态：**完成**
- 用户登录
  - 状态：*部分完成*
  - remember me 不完善
- 发表留言
  - 状态：**完成**
  - UI 需要改进
- 评论
  - 状态：*部分实现*
  - 前端回复部分需要增加模块化处理，待完成。
- 查看留言
  - 状态：*部分实现*
  - UI 和 性能需要测试和改进
- 其他功能：
  - 命令行初始化  
    - 状态：*部分完成*
    - 前端和后端需要分别执行一条命令
    - Liunx 可以实现一条命令启动两个服务。
  - 用户权限部分
    - 状态：*部分完成*
    - 前端还需要测试和改进
  - 数据库自行建表
    - 状态：**完成** 
  - 后端增加 Docker 支持
    - **完成**
---

*非常荣幸参加此实用而专业的笔试，谢谢！*

*Jason Zhang 2022-02-21 08:34*
