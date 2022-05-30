# 基于Nginx的负载均衡实验

## 1.测试Nginx

### 1.1 下载并安装Nginx

打开网址：

​	[nginx: download](https://nginx.org/en/download.html)

![image-20220119154852602](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119154852602.png)

下载并安装

![image-20220119154925430](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119154925430.png)

下载安装完成

### 1.2 测试

打开cmd转到当前目录

![image-20220119155017029](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119155017029.png)

```java
start nginx
```

![image-20220119155236149](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119155236149.png)

运行成功，默认端口：80

由于80端口被系统占用，改默认端口

打开conf/nginx.conf 文件并修改

![image-20220119155518880](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119155518880.png)

打开网页转到 [127.0.0.1:9050](http://127.0.0.1:9050/)

出现nginx欢迎界面，安装成功

![image-20220119160730162](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160730162.png)

## 2. 实验准备

为实现[负载均衡](https://so.csdn.net/so/search?q=负载均衡&spm=1001.2101.3001.7020)，需要使用到nginx的upstream模块

### 2.1建立web服务器

```js
var http = require('http');
var server = http.createServer(function (req,res){
    res.write("this is first test.");
    res.end();
});
server.listen(1121);
console.log("running at http://127.0.0.1:1121");
```

```js
var http = require('http');
var server = http.createServer(function (req,res){
    res.write("this is 2nd test.");
    res.end();
});
server.listen(1122);
console.log("running at http://127.0.0.1:1122");
```

```js
var http = require('http');
var server = http.createServer(function (req,res){
    res.write("this is 3 test.");
    res.end();
});
server.listen(1123);
console.log("running at http://127.0.0.1:1123");
```

```js
var http = require('http');
var server = http.createServer(function (req,res){
    res.write("this is 4th test.");
    res.end();
});
server.listen(1124);
console.log("running at http://127.0.0.1:1124");
```

运行

![image-20220119160021068](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160021068.png)
![image-20220119160024902](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160024902.png)
![image-20220119160026832](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160026832.png)
![image-20220119160028902](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160028902.png)



### 2.2 修改nginx配置文件

![image-20220119160134926](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160134926.png)



权重比全为：40

## 3.实验原理

### 3.1 正向代理

![image-20220119160445241](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160445241.png)

### 3.2 反向代理

![image-20220119160513905](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160513905.png)



### 3.2 负载均衡

通过反向代理服务器来优化网站的负载

![image-20220119160536526](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160536526.png)



### 3.3 二者区别

![img](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/305504-20161112130135639-1005446770.png)

### 3.4 常用的负载均衡策略

![image-20220119160844730](https://github.com/xylong-xd/Distributed-/blob/main/nginx/img/image-20220119160844730.png)















## 4.开始实验

### 3.1运行nginx

![image-20220119160224459](img\image-20220119160224459.png)

权重比都为40

### 3.2 轮询

![image-20220119160911681](img\image-20220119160911681.png)



![image-20220119160914501](img\image-20220119160914501.png)





![image-20220119160917091](img\image-20220119160917091.png)



![image-20220119160920161](img\image-20220119160920161.png)

### 3.3 权重

![image-20220119160934983](img\image-20220119160934983.png)

权重改为：40，30，20，10

![image-20220119161004808](img\image-20220119161004808.png)

实验结束

