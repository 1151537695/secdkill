package shu.xyj.secdkill.mapper;

import org.springframework.stereotype.Repository;
import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.vo.GoodsVo;

@Repository
public interface OrderMapper {

    void insert(Order order);

    Order getOrderById(Long orderId);
}
