package shu.xyj.secdkill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shu.xyj.secdkill.mapper.OrderMapper;
import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.pojo.SecdkillGoods;
import shu.xyj.secdkill.pojo.SecdkillOrder;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IOrderService;
import shu.xyj.secdkill.service.ISecdkillGoodsService;
import shu.xyj.secdkill.service.ISecdkillOrderService;
import shu.xyj.secdkill.vo.GoodsVo;

import java.util.Date;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISecdkillOrderService secdkillOrderService;

    @Autowired
    private ISecdkillGoodsService secdkillGoodsService;

    @Override
    @Transactional
    public Order secdkill(User user, GoodsVo goodsVo) {
        // 秒杀商品库存 -1
        SecdkillGoods secdkillGoods = secdkillGoodsService.findSecdkillGoodsByGoodsId(goodsVo.getId());
        secdkillGoods.setStockCount(secdkillGoods.getStockCount()-1);

        // 如果不加库存判断，在多线程情况下库存会超卖
        // 利用 mysql 自动加行锁处理多线程竞争
        // secdkillGoodsService.updateById(secdkillGoods);


        // 根据库存量更新秒杀库存
        boolean result = secdkillGoodsService.updateByIdIfStockExist(secdkillGoods);

        if (!result) {
            // 更新失败，直接返回
            return null;
        }

        // 创建订单和秒杀订单

        // 上面使用mysql行锁解决库存超卖问题，但在高并发情况下仍然会存在同一个用户重复下单情况，同样在数据库层面添加唯一索引解决该问题
        // 同时需要加上 @Transactional 事务注解，可以在重复下单的情况下回滚订单表的插入
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);        // 默认没有收货地址
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);             // 限购一件
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);                 // 生成订单未支付
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        log.info("生成的订单信息: {}", order);


        // 生成秒杀订单
        SecdkillOrder secdkillOrder = new SecdkillOrder();
        secdkillOrder.setUserId(user.getId());
        secdkillOrder.setGoodsId(goodsVo.getId());
        secdkillOrder.setOrderId(order.getId());
        secdkillOrderService.save(secdkillOrder);
        log.info("生成的秒杀订单信息: {}", secdkillOrder);

        return order;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderMapper.getOrderById(orderId);
    }
}
