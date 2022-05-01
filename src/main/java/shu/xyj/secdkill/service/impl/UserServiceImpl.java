package shu.xyj.secdkill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shu.xyj.secdkill.exception.GlobalException;
import shu.xyj.secdkill.mapper.UserMapper;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.utils.MD5Util;
import shu.xyj.secdkill.utils.UUIDUtil;
import shu.xyj.secdkill.vo.LoginVo;
import shu.xyj.secdkill.vo.RespBean;
import shu.xyj.secdkill.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("UserServiceImpl   doLogin..");

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        log.info("用户名及密码: " + mobile + ";  " + password);

        // 服务器内部也要参数校验
        // 使用 validation 组件进行校验
//        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.PHONEPATTER_ERROR);
//        }

        User user = userMapper.selectById(mobile);

        log.info("查询到的用户信息: " + user);

        if(null == user) {
            // return RespBean.error(RespBeanEnum.LOGIN_ERROR);

            // 自定义异常类抛出异常，在全局异常中返回错误信息
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        log.info("查到的用户为空..");


        if(!checkPassword(password, user)) {
            // return RespBean.error(RespBeanEnum.LOGIN_ERROR);

            // 自定义异常类抛出异常，在全局异常中返回错误信息
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }


        // 登录成功设置 session 和 cookie
        String token = UUIDUtil.uuid();

        // 用 redis 实现分布式 session，不直接使用 session或者spring session
        //request.getSession().setAttribute(token, user);
        redisTemplate.opsForValue().set("user:" + token, user);

        CookieUtil.setCookie(request, response, "userToken", token);

        return RespBean.success();
    }

    @Override
    public User getUserByCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        if (null == token) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + token);
        if(null != user) {
            CookieUtil.setCookie(request, response, "userToken", token);
        }
        return user;
    }


    private boolean checkPassword(String pwd, User user) {
        if(MD5Util.serverPassToDBPass(pwd, user.getSalt()).equals(user.getPassword())) return true;
        return false;
    }
}
