# baikeSpider

百度百科网页内容爬虫,将数据记录到`mongoDB`和`MySQL`中

Author: [严唯嘉](http://www.yanweijia.cn)

## 使用方法
请看`com.baikespider.main.Main`
```shell
java -jar baikespider.jar 线程个数 开始编号 结束编号
如:
    java -jar baikespider.jar 50 1 10000
```

配置文件请看`src/main/resources/`文件夹

jar包的生成请运行 maven 的 package 命令 `mvn package`

|||
|---|---|
|爬虫页面解析|Jsoup|
|持久层|Mybatis & MongoDB|
|日志记录|Log4J|
|项目构建|Maven|