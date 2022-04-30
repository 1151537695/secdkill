package shu.xyj.secdkill.service;

import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.vo.LoginVo;
import shu.xyj.secdkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService {
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(HttpServletRequest request, HttpServletResponse response, String ticket);
}
