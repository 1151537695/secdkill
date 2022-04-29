package shu.xyj.secdkill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import shu.xyj.secdkill.mapper.UserMapper;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.MD5Util;
import shu.xyj.secdkill.utils.ValidatorUtil;
import shu.xyj.secdkill.vo.LoginVo;
import shu.xyj.secdkill.vo.RespBean;
import shu.xyj.secdkill.vo.RespBeanEnum;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public RespBean doLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 服务器内部也要参数校验
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        if (!ValidatorUtil.isMobile(mobile)) {
            return RespBean.error(RespBeanEnum.PHONEPATTER_ERROR);
        }

        System.out.println("根据userMapper查询用户");
        User user = userMapper.selectById(mobile);

        if(null == user) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        if(!checkPassword(password, user)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        return RespBean.success();
    }

    private boolean checkPassword(String pwd, User user) {
        if(MD5Util.serverPassToDBPass(pwd, user.getSalt()).equals(user.getPassword())) return true;
        return false;
    }
}
