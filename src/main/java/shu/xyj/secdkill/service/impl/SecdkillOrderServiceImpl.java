package shu.xyj.secdkill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.xyj.secdkill.mapper.SecdkillOrderMapper;
import shu.xyj.secdkill.pojo.SecdkillOrder;
import shu.xyj.secdkill.service.ISecdkillOrderService;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SecdkillOrderServiceImpl implements ISecdkillOrderService {

    @Autowired
    private SecdkillOrderMapper secdkillOrderMapper;


    @Override
    public SecdkillOrder findSecdkillOrderByUserAndGoods(Long userId, Long goodsId) {
        log.info("SecdkillOrderServiceImpl ---> findSecdkillOrderByUserAndGoods");

        Map<String, Long> map = new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("goodsId", goodsId);

        return secdkillOrderMapper.findSecdkillOrderByUserAndGoods(map);
    }

    @Override
    public void save(SecdkillOrder secdkillOrder) {
        secdkillOrderMapper.insert(secdkillOrder);
    }
}
