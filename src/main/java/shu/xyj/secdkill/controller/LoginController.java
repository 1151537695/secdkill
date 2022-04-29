package shu.xyj.secdkill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.vo.LoginVo;
import shu.xyj.secdkill.vo.RespBean;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(LoginVo loginVo) {
        return userService.doLogin(loginVo);
    }

}
