package shu.xyj.secdkill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum RespBeanEnum {

    // 通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务器内部错误"),

    // 登陆时错误
    LOGIN_ERROR(500210, "用户名或密码错误"),
    PHONEPATTER_ERROR(500211, "手机号码格式错误"),
    Bind_ERROR(500212, "参数异常"),


    // 秒杀时错误
    NOTCURRENTDATE_ERROR(500310, "不在秒杀时间内"),
    STOCKNOTENOUGHT_ERROR(500311, "库存不足"),
    ALREADYKILL_ERROR(500312, "已经秒杀过该商品了"),
    ORDER_NOT_EXIST(500313, "找不到该订单"),
    ;



    private final Integer code;
    private final String message;
}
