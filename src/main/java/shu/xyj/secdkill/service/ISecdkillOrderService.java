package shu.xyj.secdkill.service;

import shu.xyj.secdkill.pojo.SecdkillOrder;

public interface ISecdkillOrderService {
    SecdkillOrder findSecdkillOrderByUserAndGoods(Long userId, Long goodsId);

    void save(SecdkillOrder secdkillOrder);
}
