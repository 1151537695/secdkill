package shu.xyj.secdkill.service;

import shu.xyj.secdkill.pojo.SecdkillGoods;

public interface ISecdkillGoodsService {
    public SecdkillGoods findSecdkillGoodsByGoodsId(Long id);

    void updateById(SecdkillGoods secdkillGoods);

    boolean updateByIdIfStockExist(SecdkillGoods secdkillGoods);
}
