package shu.xyj.secdkill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Goods implements Serializable {

    private static final Long serialVersionID = 1L;

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private String goodsDetail;

    private BigDecimal goodsPrice;

    private Integer goodsStock;

}
