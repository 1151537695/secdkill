package shu.xyj.secdkill.mapper;

import org.springframework.stereotype.Repository;
import shu.xyj.secdkill.pojo.SecdkillOrder;

import java.util.Map;

@Repository
public interface SecdkillOrderMapper {
    SecdkillOrder findSecdkillOrderByUserAndGoods(Map<String, Long> map);

    void insert(SecdkillOrder secdkillOrder);
}
