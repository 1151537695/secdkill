package shu.xyj.secdkill.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xixi");
        return "hello";
    }


    // 用来 Jmeter 测试，不用用户需要返回不同的信息
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(HttpServletRequest request, HttpServletResponse response) {
        User user = getUser(request, response);
        return RespBean.success(user);
    }


    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "userToken");
        return userService.getUserByCookie(request, response, token);
    }
}
