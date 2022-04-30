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


2022/4/30
添加 validation 组件，并自定义注解校验手机号码规则(不是很熟)
定义全局异常类和异常处理类，在异常处理类中解决各种异常(不是很熟)
添加单机cookie和session，保留登录的用户信息；登录成功跳转到商品列表页面
利用spring session 实现分布式session(添加redis、spring session依赖)
直接使用redis实现分布式session(用户退出登录时必须清除redis中的session，否则每次登录时都会产生随机产生一个UUID，导致每次登录时都会在redis中添加值，导致内存不够)
使用WebMVCConfig实现对接口使用前用户登录状态的判断(但这种不能理解，还存在疑惑，后面可以改用拦截器的方法)
自己实现了拦截器，并对/goods/*下的所有方法进行拦截，如果没有登录的话，会提示没有登录

注意点：
    - 获取cookie时要添加注解，@CookieValue("cookie的名称")
    - 使用redisTemplate时，注意key和value的序列化以及不同序列化之间的区别(编写redis的配置类)
        - JdkSerializationRedisSerializer
        - Jackson2JsonRedisSerializer
        - GenericJackson2JsonRedisSerializer
    - 使用拦截器实现用户未登录拦截功能，需要跳转到登录页面，需要区别请求转发和重定向的区别
        - request.getRequestDispatcher(URI).forward(request, response);
        - response.sendRedirect("/login/toLogin");


分布式session解决方案：
    - session复制，分发到其它机器上
        - 优点：无需修改代码，只需要修改tomcat配置
        - 缺点：session传输需要时间，会占用内网带宽；机器数量越多，性能下降越快；每台机器都保存，冗余存储
    - 前端存储：
        - 优点：不占用服务器资源
        - 缺点：存在安全风险；每次都要携带用户信息，占用外网带宽；数据大小受限制；
    - session粘滞(通过Hash可以每次把请求发送到同一台机器上)
        - 优点：无需修改代码，可以水平扩展
        - 缺点：增加新机器会重新Hash，导致重新登录；服务器重启需要重新登录；
    - 数据库存储
        - 优点：安全，容易水平扩展
        - 缺点：系统复杂度变高