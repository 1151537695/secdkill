package shu.xyj.secdkill.service;

import org.springframework.stereotype.Service;
import shu.xyj.secdkill.vo.LoginVo;
import shu.xyj.secdkill.vo.RespBean;

public interface IUserService {
    RespBean doLogin(LoginVo loginVo);
}
