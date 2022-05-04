package shu.xyj.secdkill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.xyj.secdkill.dto.OrderDto;
import shu.xyj.secdkill.pojo.Order;
import shu.xyj.secdkill.pojo.SecdkillOrder;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IGoodsService;
import shu.xyj.secdkill.service.IOrderService;
import shu.xyj.secdkill.service.ISecdkillOrderService;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.GoodsVo;
import shu.xyj.secdkill.vo.RespBean;
import shu.xyj.secdkill.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/secdkill")
public class SecdkillController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISecdkillOrderService secdkillOrderService;

    @Autowired
    private IOrderService orderService;

    /**
     *  压力测试结果
     *  windows 1000*10 跑三次  QPS: 2450          同时发现超卖现象，以及存在多个订单，需要加锁
     */
    @RequestMapping(value = "/doSecdkill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecdkill(Long goodsId, HttpServletRequest request, HttpServletResponse response) {

        // 用户只有登录了才能秒杀，所以也要加入用户登录判断
        User user = getUser(request, response);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date nowDate = new Date();

        OrderDto dto = new OrderDto();

        // 判断是否处于秒杀时间段中
        if (nowDate.before(goodsVo.getStartDate())) {
            // 秒杀还没有开始
            log.info("秒杀还没有开始..");
            return RespBean.error(RespBeanEnum.NOTCURRENTDATE_ERROR);
        } else if (nowDate.after(goodsVo.getEndDate())) {
            // 秒杀已经结束
            log.info("秒杀已经结束..");
            return RespBean.error(RespBeanEnum.NOTCURRENTDATE_ERROR);
        }

        // 判断库存是否足够
        if (goodsVo.getStockCount() <= 0) {
            return RespBean.error(RespBeanEnum.STOCKNOTENOUGHT_ERROR);
        }

        // 限流抢购，每个人每件商品只能抢一次
        SecdkillOrder secdkillOrder = secdkillOrderService.findSecdkillOrderByUserAndGoods(user.getId(), goodsId);

        if (null != secdkillOrder) {
            return RespBean.error(RespBeanEnum.ALREADYKILL_ERROR);
        }

        // 开启秒杀，生产相应的订单
        log.info("可以秒杀..");
        Order order = orderService.secdkill(user, goodsVo);

        dto.setGoods(goodsVo);
        dto.setOrder(order);

        return RespBean.success(dto);
    }


    @RequestMapping("/doSecdkill2")
    public String doSecdkill2(Model model, Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        log.info("SecdkillController ---> doSecdkill");

        // 用户只有登录了才能秒杀，所以也要加入用户登录判断
        User user = getUser(request, response);
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("秒杀的商品: {}", goodsVo);


        Date nowDate = new Date();
        // 判断是否处于秒杀时间段中
        if (nowDate.before(goodsVo.getStartDate())) {
            // 秒杀还没有开始
            log.info("秒杀还没有开始..");
            model.addAttribute("errorMsg", RespBeanEnum.NOTCURRENTDATE_ERROR);
            return "secdkillFail";
        } else if (nowDate.after(goodsVo.getEndDate())) {
            // 秒杀已经结束
            log.info("秒杀已经结束..");
            model.addAttribute("errorMsg", RespBeanEnum.NOTCURRENTDATE_ERROR);
            return "secdkillFail";
        }

        // 判断库存是否足够
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("errorMsg", RespBeanEnum.STOCKNOTENOUGHT_ERROR);
            return "secdkillFail";
        }

        // 限流抢购，每个人每件商品只能抢一次
        SecdkillOrder secdkillOrder = secdkillOrderService.findSecdkillOrderByUserAndGoods(user.getId(), goodsId);

        if (null != secdkillOrder) {
            model.addAttribute("errorMsg", RespBeanEnum.ALREADYKILL_ERROR);
            return "secdkillFail";
        }

        // 开启秒杀，生产相应的订单
        log.info("可以秒杀..");
        Order order = orderService.secdkill(user, goodsVo);

        model.addAttribute("goods", goodsVo);
        model.addAttribute("order", order);

        return "orderDetail";
    }




    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "userToken");
        return userService.getUserByCookie(request, response, token);
    }
}
