package shu.xyj.secdkill.vo;

import lombok.Data;
import shu.xyj.secdkill.validator.IsMobile;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    private String password;
}
