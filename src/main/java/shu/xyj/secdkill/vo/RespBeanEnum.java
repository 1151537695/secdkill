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
    ;

    private final Integer code;
    private final String message;
}
