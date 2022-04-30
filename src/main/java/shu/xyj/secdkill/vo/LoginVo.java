package shu.xyj.secdkill.vo;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import shu.xyj.secdkill.validator.IsMobile;

@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    private String password;
}
