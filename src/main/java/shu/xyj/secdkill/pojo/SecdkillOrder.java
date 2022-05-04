package shu.xyj.secdkill.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SecdkillOrder implements Serializable {

    private static final long serialVersionUID = 1l;

    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

}
