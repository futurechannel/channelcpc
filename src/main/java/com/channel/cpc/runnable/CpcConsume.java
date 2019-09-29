package com.channel.cpc.runnable;

import com.channel.cpc.constants.ConstantMaps;
import com.channel.cpc.constants.Constants;
import com.channel.cpc.dto.CpcReportDto;
import com.channel.cpc.handler.CpcReportHandler;
import com.channel.cpc.server.ResourceManager;
import com.channel.cpc.util.GsonUtils;
import com.channel.cpc.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class CpcConsume implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    private String name;

    public CpcConsume(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        while (true) {
            int time;

            Integer sendTime = ConstantMaps.sendTimeMap.get(name);
            if (sendTime == null || sendTime < 1) {
                time = 1;
            } else if (sendTime > 600) {
                time = 600;
            } else {
                time = sendTime;
            }
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                logger.error("send thread sleep fail", e);
            }

            try {

                LinkedBlockingQueue<CpcReportDto> queue = ResourceManager.getCpcMap().get(name);

                if (queue == null) {
                    logger.info("{} queue is stop", name);
                    break;
                }

                String status = ConstantMaps.cpcSwitchMap.get(name);
                if(!"1".equals(status)){
                    if(queue.size()<1||queue.size()>50){
                        ResourceManager.getCpcMap().remove(name);
                        logger.info("{} queue is stop", name);
                        break;

                    }
                }

                if (queue.size() == 0) {
                    logger.info("{} queue size is 0", name);
                    continue;
                } else {
                    logger.info("{} queue size is {}", name, queue.size());
                }

                CpcReportDto dto = queue.poll();


                CpcReportHandler handler = (CpcReportHandler) SpringUtils.getBean("cpcReportHandler");
                handler.report(dto);
                logger.info("send cpc success :{} ,sleep time:{} s", GsonUtils.pojoToJson(dto), time);

            } catch (Exception e) {
                logger.error("send cpc fail", e);
            }

        }

    }
}
