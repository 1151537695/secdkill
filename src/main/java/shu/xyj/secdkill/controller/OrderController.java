package shu.xyj.secdkill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.xyj.secdkill.dto.OrderDto;
import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IGoodsService;
import shu.xyj.secdkill.service.IOrderService;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.GoodsVo;
import shu.xyj.secdkill.vo.RespBean;
import shu.xyj.secdkill.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private IGoodsService goodsService;
    
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(Long orderId, HttpServletRequest request, HttpServletResponse response) {
        User user = getUser(request, response);

        Order order = orderService.getOrderById(orderId);
        if(null == order) {
            return RespBean.error(RespBeanEnum.ORDER_NOT_EXIST);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(order.getGoodsId());

        OrderDto dto = new OrderDto();
        dto.setOrder(order);
        dto.setGoods(goods);

        return RespBean.success(dto);
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "userToken");
        return userService.getUserByCookie(request, response, token);
    }
}
