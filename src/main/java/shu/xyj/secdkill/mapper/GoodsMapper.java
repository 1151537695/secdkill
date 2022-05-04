package shu.xyj.secdkill.mapper;

import org.springframework.stereotype.Repository;
import shu.xyj.secdkill.vo.GoodsVo;

import java.util.List;

@Repository
public interface GoodsMapper {
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
