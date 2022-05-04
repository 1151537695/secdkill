package shu.xyj.secdkill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SecdkillGoods implements Serializable {

    private static final long serialVersionUID = 1l;

    private Long id;

    private Long goodsId;

    private BigDecimal secdkillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}
