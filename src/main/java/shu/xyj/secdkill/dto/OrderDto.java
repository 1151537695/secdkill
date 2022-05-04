package shu.xyj.secdkill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.vo.GoodsVo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private GoodsVo goods;
    private Order order;
    private String errorMsg;
}
