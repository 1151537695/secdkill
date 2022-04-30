package shu.xyj.secdkill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private IUserService userService;

    // 一定要用 @CookieValue("userTicket") 获取指定 cookie 的值
//    @RequestMapping("/toList")
//    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userToken") String ticket) {
//        log.info("登录成功，跳转到商品列表页面..");
//        // 没有登录需要先登录
//        if (ticket == null) {
//            return "login";
//        }
//
//        // 使用redis 实现分布式session
//        // User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(request, response, ticket);
//
//
//        if (null == user) {
//            return "login";
//        }
//
//        model.addAttribute("user", user);
//        return "goodsList";
//    }


    // 使用 WebMVC 对参数进行校验，避免需要对每个方法都判断用户是否登录(这个地方还是不懂，User是怎么样传进来的，如果有多个参数，如何都进行判断呢？希望自己可以用拦截器进行修改)
    @RequestMapping("/toList")
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "userToken");

        User user = userService.getUserByCookie(request, response, token);

        model.addAttribute("user", user);
        return "goodsList";
    }
}
