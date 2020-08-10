## 简介

这是使用`Spring Booot `搭建的个人博客，持久层使用`JPA`，前端模板引擎使用`Thymeleaf`，ui框架使用`Semantic-UI`，数据库使用`MySql`。项目的前大部分是我根据李仁密老师的[视频]( https://www.bilibili.com/video/BV13t411T72J )下完成的，后期加了简单评论审核功能，增加友情链接页面。

[演示地址](blog.yalexin.top)

## 创建管理用户的方法：

由于本博客设计服务于个人博客，并未提供注册功能，加上`MD5`加密功能是后期加入的，因此创建用户的方法会有些麻烦，具体操作如下：

1. 打开`src/main/resources/templates/admin/login.html`， 修改`checkForm`函数，将加密后的表单数据打印出来，具体修改为：

   ```javascript
   function checkForm () {
       var pwd = $("[name='password_input']").val();
       var md5_pasw = md5(pwd);
       $("[name='password']").val(md5_pasw);
       console.log($("[name='password']").val()); // 这里是修改的地方
       return false; // 这里是修改的地方
   }
   ```

   然后运行项目（在这之前，你需要成功配置数据库），接着访问项目路径下的`\admin`页面，来到登录页面，输入你想要创建的用户名和用户密码，右键，审查元素，点击登录，观察控制台的输出，这个字符串即加密后的数据。

   2. 在`util`包下的`MarkdownUtils.java`中，在`main`函数中调用`System.out.println(MarkdownUtils.markdownToHtmlExtensions("test"));`,其中`test`是前端控制台打印出来的加密字符串，观察控制台输出，该输出就是最终存到数据库的用户密码。
   3. 使用工具连接到你的数据库，在`t_user`表中插入一条记录，该记录的用户名就是你想创建的用户名，密码是你刚刚两次加密后的字符串。
   4. 存到数据库后，将第一步中的函数的返回值改为`true`，否则表单无法提交。

## 涉及的技术或者项目

- [`Spring Booot 2.1.15.RELEASE `]( https://spring.io/projects/spring-boot )

- [Thymeleaf 3.0.11.RELEASE]( https://www.thymeleaf.org/ )

- [`MySql 5.6`]( https://www.mysql.com/ )

- [`editormd（Markdown编辑器）`](https://github.com/pandao/editor.md)

-  [`fancybox`](http://fancyapps.com/fancybox/)

- [`prism（代码高亮）`](https://prismjs.com/)

- [`qrcode`]( https://github.com/jeromeetienne/jquery-qrcode )

- [`semanticCalendar`](http://github.com/semantic-org/semantic-ui/)

-  [`tocbot`]( https://github.com/tscanlin/tocbot )

- [`waypoints`]( https://github.com/imakewebthings/waypoints )

- [`alert`]( https://github.com/ydq/alert )

- [`Typo.css`]( https://typo.sofi.sh/ )

- [`timeline`]( https://codepen.io/mo7amedk7alid29/pen/dRoMwo )

- [`animate.css`]( https://github.com/animate-css/animate.css )