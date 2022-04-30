package shu.xyj.secdkill.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import shu.xyj.secdkill.exception.GlobalException;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.RespBeanEnum;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器 --- > 处理请求之 前 拦截");

        String token = CookieUtil.getCookieValue(request, "userToken");

        log.info("token的值 -------->  " + token);

        if (StringUtils.isEmpty(token)) {
//            request.getRequestDispatcher("/login").forward(request, response);
            log.info("token为空，重定向..");
            response.sendRedirect("/login/toLogin");
            return false;
        }

        User user = userService.getUserByCookie(request, response, token);

        log.info("User from redis: " + user);

        if (null != user) {
            CookieUtil.setCookie(request, response, "userToken", token);
        } else {
//            request.getRequestDispatcher("/login").forward(request, response);
            response.sendRedirect("/login/toLogin");
            return false;
        }
        log.info("拦截器继续执行 ------ ");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("拦截器 --- > 处理请求之 后 拦截");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("拦截器 --- > 响应后执行..");
    }
}
