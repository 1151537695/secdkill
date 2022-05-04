package shu.xyj.secdkill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.vo.GoodsVo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    private User user;
    private GoodsVo goods;
    private int remainSeconds;
    private int secKillStatus;
}
