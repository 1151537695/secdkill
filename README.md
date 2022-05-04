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
        - JdkSerializationRedisSerializer，需要序列化的对象实现 Serializable 接口
        - Jackson2JsonRedisSerializer，要传入Object对象
        - GenericJackson2JsonRedisSerializer，
        - StringRedisSerializer，效率最高，但可读性较差
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
    - session粘滞(通过Hash可以每次把请求发送到同一台机器上)，可以通过Nginx的ip-hash策略实现
        - 优点：无需修改代码，可以水平扩展
        - 缺点：增加新机器会重新Hash，导致重新登录；服务器重启需要重新登录；
    - 数据库存储
        - 优点：安全，容易水平扩展
        - 缺点：系统复杂度变高，多了网络调用(需要请求redis)


2022/5/1
创建商品表、订单表、商品秒杀表、订单秒杀表
实现mapper接口和对应的XXXMapper.xml文件


踩过的坑：
    - 对于传输所用的Vo类，如果从数据库直接获取Vo对象或者集合时，需要在XXXMapper.xml中定义相应的resultMap，不然返回的结果里都是null值(数据库字段和Vo字段不匹配造成的)


注意点：
    - left join 和 inner join 的区别


2022/5/2
展示商品详细信息，实现最基本的秒杀功能

踩过的坑：
    - Invalid bound statement (not found)：报这个错，Mapper.java 和 Mapper.xml文件映射关系不对
        - 可能是 namespace 不对
        - 可能是 Mapper.java 下的接口名字和 Mapper.xml 中的id不一致
        - 自己的错误原因：XXXMapper.xml 文件命名中忘记写 Mapper了，写成了 XXX.xml 导致映射关系错了(在配置文件中配置的是 mapper-locations: classpath*:/mappers/*Mapper.xml)
    - 由于数据库字段 status 不小心写成了 statue，所以在秒杀过程中，库存商品成功-1，但插入订单数据时出现错误(出现数据不一致)，所以后面应该需要加上事务才行(偶然发现)
    - 通过 new Order()的方式创建订单，并通过 setXXX 设置订单的属性，最后通过简单的 insert 插入订单数据，所以当创建秒杀订单时(需要订单的id，但订单的id是主键自动生成的)，秒杀订单的 order_id 字段一直为 null
        - 解决办法：1) 根据user_id 和 goods_id 确定唯一性，查询一边订单信息，从而获取主键
                  2) 在Mapper.xml 文件中设置 useGeneratedKeys="true" keyProperty="id" 属性，其中 id 是返回的主键对应的java对象的属性(可以减少查询)


2022/5/3
windows上简单使用Jmeter对项目进行压测
CentOS7 上使用 Jmeter
    - 修改properties文件，修改编码为 UTF-8
    - 在windows上保存压测的配置，形成 XXX.jmx 文件
    - CenOS7 上运行：jmeter.sh -n -t XXX.jmx -l result.jtl
        - -n 表示不是图形化运行(Linux系统上)   
        - -t 表示运行时的配置
        - -l 结果日志的保存文件，可以拿回到windows进行查看


踩过的坑：
    - 在centOS上安装 mysql，一定要开启支持外网访问，不然会报错；在实际中会限定用户名及可以访问的ip


2022/5/4
在阿里云上安装redis，并配置后台启动(MySQL之前就配置过了)
本地打包项目复制到阿里云上运行
在阿里云服务器中使用 Jmeter 进行压测
对前端页面进行缓存
前后端分离，前端ajax请求动态的数据，减少传输所需的数据



知识点：
    - @RequestMapping(produces="text/html;charset=utf-8")   produces属性可以指定返回类型，开可以设置返回值的字符编码
      相对应的有 consumes 属性，可以指定处理请求的提交内容类型，例如 consumes="application/json"，表明方法只能处理 application/json 类型的请求

踩过的坑：
    - 由于 CookieUtil 里没有对47.97.118.120(阿里云的ip) 进行处理，所以服务器上一直无法登录成功



2022/5/5
解决库存超卖以及重复订单和秒杀订单的bug


注意点：
    - 采用更新库存时判断库存是否>0，利用mysql自动加的行锁保证高并发情况下的库存正常
        - 也可以在业务中获取库存的时候加Lock锁，只有库存>0才能下单，但这种方法在高并发情况下等待的线程太多了，效果不好
    - 利用mysql的行锁解决库存超卖问题后，仍然会存在同一个用户多次下单的情况，
        - 和上面一样，可以在获取秒杀订单表的时候加Lock锁等，这样能保证不重复下单
        - 利用 mysql 的联合唯一索引，秒杀订单中(user_id,goods_id)组合唯一索引，当重复下单时会失败
            - 注意点：这里需要对秒杀订单建立索引而不是订单表，由于订单表只是普通的订单，同一个用户是可以重复下单的，而秒杀订单则规定了只能秒杀一次
    - 采用mysql数据进行库存和防止重复下单时，由于重复下单可能会失败，所以需要用事务来保证普通订单表的回滚操作
        - 这里有想过可以把库存-1操作放到最后面，这样失败的时候就可以少回滚该部分内容。后来想想在高并发的秒杀场景下不合理，因为秒杀情况下，库存都不会很多，先判断库存是否足够可以过滤大部分请求，从而能更大程度上防止订单表的回滚

由上面引出的问题，要是秒杀规则不是只能抢购一件，如何设计比较合理？？？(可以抢购N件)





优化：
    - 前端页面缓存：(适用于不怎么变更的页面)
        - 例如每次都请求商品列表页面，可以把整个商品详情页面缓存到redis中，下次请求商品列表时，可以直接返回
        - 存在的问题，缓存过期问题？(过期时间如何设置比较好)、传输整个html页面数据量比较大(只需传动态的数据即可)
    - 对象缓存
        - 登录用户缓存，例如解决分布式session时就缓存了对象
    - 静态资源优化(js、css、图片等)，动态的数据发送ajax进行请求，减少传输所需的数据
    - CDN 优化


难点：
    - 页面缓存到redis时，需要设置过期时间，这个过期时间设置为多少比较好？在高并发的情况下，有缓存时会有缓存和数据库不一致的问题，是否有问题？