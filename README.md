解放命令行的burp插件

# 功能介绍

## 2022年12月29日23:47:22

第一次提交,主要实现了两个功能,fastjson插件利用及终端快捷调用

### 终端快捷调用

![image](https://user-images.githubusercontent.com/27048404/209977213-c2bf27a9-3b55-48b2-9d43-7b58b56f8243.png)

在config配置需要调用的工具,并添加调用属性,配合占位符

- {request}表示使用请求包
- {url}表示使用当前的url
- {host}表示使用当前的host

添加保存完成之后在菜单右键可以看到工具

![image](https://user-images.githubusercontent.com/27048404/209978460-69f493a9-de9b-4daa-b717-f7508f03cdb7.png)

### fastjson利用

![image](https://user-images.githubusercontent.com/27048404/209977549-37b597cf-24f6-4ee8-95b1-23e016138eee.png)

此处实现了三个方法,回显,dnslog,注入内存马

注意: dnslog需要在config面板配置dnslog地址,并且执行完成后需要在dnslog平台手动查看


# TODO

还没想好

# 项目参考

- 参考ui设计: https://github.com/aw220/InOne
- 参考fastjson利用实现: https://github.com/skisw/fastjson-exp
- 参考命令行终端调用: https://github.com/bit4woo/knife
