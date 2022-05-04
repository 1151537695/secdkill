package shu.xyj.secdkill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.xyj.secdkill.mapper.SecdkillGoodsMapper;
import shu.xyj.secdkill.pojo.SecdkillGoods;
import shu.xyj.secdkill.service.ISecdkillGoodsService;

@Service
public class SecdkillGoodsServiceImpl implements ISecdkillGoodsService {

    @Autowired
    private SecdkillGoodsMapper secdkillGoodsMapper;

    @Override
    public SecdkillGoods findSecdkillGoodsByGoodsId(Long id) {
        return secdkillGoodsMapper.findSecdkillGoodsByGoodsId(id);
    }

    @Override
    public void updateById(SecdkillGoods secdkillGoods) {
        secdkillGoodsMapper.updateById(secdkillGoods);
    }

    @Override
    public boolean updateByIdIfStockExist(SecdkillGoods secdkillGoods) {
        return secdkillGoodsMapper.updateByIdIfStockExist(secdkillGoods);
    }
}
