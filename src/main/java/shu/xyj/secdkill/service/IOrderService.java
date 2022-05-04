package shu.xyj.secdkill.service;

import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.vo.GoodsVo;

public interface IOrderService {
    Order secdkill(User user, GoodsVo goodsVo);

    Order getOrderById(Long orderId);
}
