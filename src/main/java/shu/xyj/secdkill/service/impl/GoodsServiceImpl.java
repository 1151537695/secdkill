package shu.xyj.secdkill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.xyj.secdkill.mapper.GoodsMapper;
import shu.xyj.secdkill.mapper.SecdkillGoodsMapper;
import shu.xyj.secdkill.service.IGoodsService;
import shu.xyj.secdkill.vo.GoodsVo;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public List<GoodsVo> findGoodsVoList() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
