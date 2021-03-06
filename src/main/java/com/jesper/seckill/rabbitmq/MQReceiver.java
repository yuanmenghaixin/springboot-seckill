package com.jesper.seckill.rabbitmq;

import com.jesper.seckill.bean.SeckillOrder;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.service.OrderService;
import com.jesper.seckill.service.SeckillService;
import com.jesper.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 2018/5/29.
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);


    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("接收--receive() message:" + message);
        try {
            SeckillMessage m = RedisService.stringToBean(message, SeckillMessage.class);
            User user = m.getUser();
            long goodsId = m.getGoodsId();
            GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
            int stock = goodsVo.getStockCount();
            if (stock <= 0) {
                return;
            }
            //判断重复秒杀
            SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
            if (order != null) {
                return;
            }
            //减库存 下订单 写入秒杀订单
            seckillService.seckillHandle(user, goodsVo);
        }catch(Exception e){
            log.info("处理异常"+e);
        }
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info("接收--receiveTopic1() topic  queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info("接收--receiveTopic2() topic  queue2 message:" + message);
    }
}
