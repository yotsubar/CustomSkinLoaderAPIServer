一个离线模式的[万能皮肤加载器](https://github.com/xfl03/MCCustomSkinLoader) 的皮肤站服务端，支持[CustomSkinLoaderAPI](https://github.com/xfl03/CustomSkinLoaderAPI).

**注意: 仅用于离线模式.**
只要知道了玩家id，任何人都可以更改这个玩家的皮肤

**运行环境**

JDK(JRE) 1.8

**用法**

这是一个SpringBoot应用，只要执行jar包就可以了。

`java -jar mc-customskinloader-server-0.0.1-SNAPSHOT.jar`

程序启动之后，就可以用浏览器访问 http://{ip}/, 上传皮肤贴图。

把 http://{ip}/ 加到 [ExtraList](https://github.com/xfl03/CustomSkinLoaderAPI/blob/master/ExtraList/ExtraList-zh_CN.md) 里边就可以了。

如果需要修改服务器的配置，可以在jar包目录下新建一个config目录，在config目录里边新建 application.yml , 写入相关配置即可

application.yml
~~~
## 数据库连接信息，不要更改 ##
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: 'jdbc:h2:./db/skin'
    username: skin
    password: McSkin@0124!
  jpa:
    hibernate:
      ddl-auto: update

## 贴图文件的保存目录 ##
app:
  textures:
    base: textures/
## 修改服务器端口 ##
server:
  port: 80
~~~