FROM registry.cn-shanghai.aliyuncs.com/yry-test/tomcat8:v1.0.0
ADD trade-push-server/target/trade-push-server.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
