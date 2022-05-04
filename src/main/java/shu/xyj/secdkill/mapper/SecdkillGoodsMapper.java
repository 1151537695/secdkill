package shu.xyj.secdkill.mapper;

import org.springframework.stereotype.Repository;
import shu.xyj.secdkill.pojo.SecdkillGoods;

@Repository
public interface SecdkillGoodsMapper {
    public SecdkillGoods findSecdkillGoodsByGoodsId(Long id);

    void updateById(SecdkillGoods secdkillGoods);

    boolean updateByIdIfStockExist(SecdkillGoods secdkillGoods);
}
