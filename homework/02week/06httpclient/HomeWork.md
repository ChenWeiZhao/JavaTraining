（必做）写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 GitHub

1.打开 Spring 官网: https://spring.io/
2.找到 Projects --> Spring Initializr: https://start.spring.io/
3.填写项目信息, 生成 maven 项目; 下载并解压。
4.Idea或者Eclipse从已有的Source导入Maven项目。
5.搜索依赖， 推荐 mvnrepository: https://mvnrepository.com/
6.搜索 OkHttp 或者 HttpClient，然后在 pom.xml 之中增加对应的依赖。
7.使用OkHttp
    7.1 查找OkHttp官网: https://square.github.io/okhttp/
    7.2 参照官方示例编写代码: OkHttpUtils.java
8.使用HttpClient
    8.1 查找官网: http://hc.apache.org/
    8.2 参照官方示例编写代码: HttpClientHelper.java
    8.3 执行如果报错, 根据提示，增加 commons-logging 或者其他日志依赖。
9.执行与测试.


OkhttpdemoApplication.java