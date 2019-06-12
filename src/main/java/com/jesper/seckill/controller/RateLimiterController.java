package com.jesper.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tim on 2019/6/5.
 */
@Controller
@RequestMapping("/rateLimiter")
public class RateLimiterController {
    //基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(0.1); //设置为0.1为十秒获取一个令牌
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/access")
    @ResponseBody
    public String access() {
        //尝试获取令牌
        if (rateLimiter.tryAcquire()) {
            Date date = new Date();
            //模拟业务执行500毫秒
            try {

                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Date date2 = new Date();
            Long values = date.getTime() - date2.getTime();
            return "执行完aceess ********SUCCESS******* [" + sdf.format(date) + "]耗时:" + values;
        } else {
            return "@@@@@@@ aceess limit [" + sdf.format(new Date()) + "]:";
        }
    }
}
