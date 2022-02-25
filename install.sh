###
 # @Author: Jason Zhang
 # @Date: 2022-02-20 19:54:43
 # @LastEditTime: 2022-02-20 20:38:43
 # @LastEditors: Please set LastEditors
 # @Description: 
 # @FilePath: /messageboard/Users/saneryee/Desktop/recruitment-java-backend-comments-tree-homework-QigongZhang/install.sh
### 
#!/bin/bash
echo "Star Vue Server"
gnome-terminal -x sh -c "cd ./codes/web/messageboard && npm run serve"
echo "Star Spring Boot"
gnome-terminal -x sh -c "cd ./codes/server/messageboard && ./mvnw spring-boot:run"
