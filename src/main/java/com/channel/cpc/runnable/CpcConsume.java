package com.channel.cpc.runnable;

import com.channel.cpc.constants.ConstantMaps;
import com.channel.cpc.dto.CpcReportDto;
import com.channel.cpc.handler.CpcReportHandler;
import com.channel.cpc.server.ResourceManager;
import com.channel.cpc.util.GsonUtils;
import com.channel.cpc.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class CpcConsume implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    private String name;

    public CpcConsume(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        long spendTime = 0L;

        while (true) {
            int time;

            Integer sendTime = ConstantMaps.sendTimeMap.get(name);
            if (sendTime == null || sendTime < 1) {
                time = 1;
            } else if (sendTime > 600000) {
                time = 600000;
            } else {
                time = sendTime;
            }

            long sleepTime = 2*time - spendTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.error("send thread sleep fail", e);
                }
            }

            try {

                LinkedBlockingQueue<CpcReportDto> queue = ResourceManager.getCpcMap().get(name);

                if (queue == null) {
                    logger.info("{} queue is stop", name);
                    break;
                }

                String status = ConstantMaps.cpcSwitchMap.get(name);
                if (!"1".equals(status)) {
                    logger.info("{} queue is stop", name);
                    break;
                }

                if (queue.size() == 0) {
                    logger.info("{} queue size is 0", name);
                    spendTime = 0L;
                    continue;
                } else {
                    logger.info("{} queue size is {}", name, queue.size());
                }

                Date start = new Date();
                int sum;
                int queueSize = queue.size();
                int sendNum = ConstantMaps.sendNumMap.get(name);
                if (queueSize > 0 && sendNum > 0) {
                    if (queueSize % sendNum > 0) {
                        sum = queueSize / sendNum + 1;
                    } else {
                        sum = queueSize / sendNum;
                    }
                } else {
                    sum = 0;
                }

                if (sum > 100) {
                    sum = 100;
                }

                for (int i = 0; i < sum; i++) {
                    if(queue==null){
                        break;
                    }
                    CpcReportDto dto = queue.poll();
                    if (dto == null) {
                        continue;
                    }
                    CpcReportHandler handler = (CpcReportHandler) SpringUtils.getBean("cpcReportHandler");
                    handler.report(dto);
                    logger.info("send cpc success :{} ,sleep time:{} s", GsonUtils.pojoToJson(dto), sleepTime);
                }
                spendTime = new Date().getTime() - start.getTime();

            } catch (Exception e) {
                logger.error("send cpc fail", e);
            }

        }

    }
}
