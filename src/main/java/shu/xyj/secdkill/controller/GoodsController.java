package shu.xyj.secdkill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;
import shu.xyj.secdkill.dto.DetailDto;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.service.IGoodsService;
import shu.xyj.secdkill.service.IUserService;
import shu.xyj.secdkill.utils.CookieUtil;
import shu.xyj.secdkill.vo.GoodsVo;
import shu.xyj.secdkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;        // 渲染页面

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

    /**
     *  压力测试结果：
     *  windows 3000*10 跑三次  QPS: 3100
     *  aliyun  1000*10 跑三次  QPS: 330
     *
     *  页面缓存优化  QPS: 5649
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 用 redis 缓存页面进行优化
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            // 已经存在缓存，可以直接返回
            return html;
        }

        // 缓存中还没有存在页面，需要加载数据，渲染页面，并把结果放到 redis 缓存中
        User user = getUser(request, response);
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.findGoodsVoList();
        model.addAttribute("goodsList", goodsList);

        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);

        if (!StringUtils.isEmpty(html)) {
            // 放入缓存中，设置缓存的过期时间，可能存在问题，看readme的困难1
            valueOperations.set("goodsList", html, 5, TimeUnit.SECONDS);
        }

        return html;

    }

//    public String toList(Model model, HttpServletRequest request, HttpServletResponse response) {
//        User user = getUser(request, response);
//        model.addAttribute("user", user);
//
//        List<GoodsVo> goodsVoList = goodsService.findGoodsVoList();
////        goodsVoList.stream().forEach(System.out::println);
//
//        model.addAttribute("goodsList", goodsVoList);
//        return "goodsList";
//    }


    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(@PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        log.info("/goods/detail/{goodsId} ====>>>>");

        User user = getUser(request, response);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        int secKillStatus = 0;      // 秒杀状态
        int remainSeconds = 0;      // 秒杀剩余时间
        if (nowDate.before(startDate)) {
            // 秒杀还没有开始
            secKillStatus = 0;
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀已经结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀活动仍在进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }

        DetailDto dto = new DetailDto();
        dto.setGoods(goodsVo);
        dto.setRemainSeconds(remainSeconds);
        dto.setSecKillStatus(secKillStatus);
        dto.setUser(user);

        log.info("商品详情信息：" + goodsVo);

        return RespBean.success(dto);
    }


    @RequestMapping("/toDetail2/{goodsId}")
    public String toDetail2(Model model, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        User user = getUser(request, response);
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        int secKillStatus = 0;      // 秒杀状态
        int remainSeconds = 0;      // 秒杀剩余时间
        if (nowDate.before(startDate)) {
            // 秒杀还没有开始
            secKillStatus = 0;
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀已经结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀活动仍在进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        log.info("商品详情信息：" + goodsVo);

        return "goodsDetail";
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "userToken");
        return userService.getUserByCookie(request, response, token);
    }
}
