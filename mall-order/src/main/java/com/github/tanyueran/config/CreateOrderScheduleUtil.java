package com.github.tanyueran.config;

import com.github.tanyueran.dto.OrderTaskDto;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.service.CakeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
public class CreateOrderScheduleUtil {

    // 任务队列
    private static Map<String, OrderTaskDto> tasks = null;

    // 限定时间值
    @Value("${order.limitStatus0Time}")
    private Long limitTime;

    @Autowired
    private CakeOrderService cakeOrderService;

    @PostConstruct
    public void init() {
        log.info("==========================limit:" + limitTime);
        List<CakeOrder> orders = cakeOrderService.getAllStatus0Order();
        if (orders == null) return;
        Map<String, OrderTaskDto> tasks = Collections.synchronizedMap(new HashMap<>());
        if (orders.size() != 0) {
            orders.forEach(item -> {
                OrderTaskDto dto = new OrderTaskDto();
                dto.setStartTime(item.getCreateTime());
                dto.setOrderId(item.getId());
                tasks.put(item.getId(), dto);
            });
        }
        this.tasks = tasks;
    }


    // 添加任务
    public synchronized static void addTask(String orderId, OrderTaskDto orderTaskDto) {
        tasks.put(orderId, orderTaskDto);
    }

    @Scheduled(cron = "0/10 0/1 * * * ?")
    public void run() {
        if (tasks == null) return;
        Iterator<String> iterator = tasks.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            long start = tasks.get(next).getStartTime().getTime();
            long now = new Date().getTime();
            if ((now - start) >= limitTime) {
                new Thread(() -> {
                    log.info("开始改变订单【" + next + "】的状态");
                    // 改变状态
                    Boolean ok = cakeOrderService.orderCreatedOvertime(next);
                    if (ok) {
                        log.info("状态改成成功");
                        // 删除任务
                        iterator.remove();
                        // 发送消息
                    }
                }).run();
            }
        }
    }
}
