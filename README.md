# secdkill
秒杀系统

2022/4/28
搭建spring boot项目，并实现访问成功


2022/4/29
完成登录功能，具体包括两次MD5+椒盐加密，对手机号格式校验
    - 两次MD5的目的：第一次为了密码在网络中传输不是明文；第二次为了服务器密码不容易泄露；封装相应的工具类
    - 实现 RespBean 和 RespBeanEnum 更好的封装返回对象
    - 封装校验工具类，后端校验手机格式等

遇到的问题：
    - MySQL url写错了，导致一致不成功
    - mapper.xml 中传入的参数要与接口类中的参数名称保持一致；且语句最后需要一个";"号
