package com.channel.cpc.web;

import com.channel.cpc.base.BaseController;
import com.channel.cpc.constants.ConstantMaps;
import com.channel.cpc.dto.BaseResult;
import com.channel.cpc.dto.CpcReportDto;
import com.channel.cpc.entity.AdvertInfo;
import com.channel.cpc.enums.ErrorCode;
import com.channel.cpc.form.AddCpcForm;
import com.channel.cpc.form.StartCpcForm;
import com.channel.cpc.form.StopCpcForm;
import com.channel.cpc.runnable.CpcConsume;
import com.channel.cpc.server.ResourceManager;
import com.channel.cpc.service.AdvertInfoService;
import com.channel.cpc.util.GsonUtils;
import com.channel.cpc.util.IdfaUtils;
import com.channel.cpc.util.IpUtils;
import com.channel.cpc.util.ThreadPoolHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gq on 2018/4/11.
 */
@Controller
@RequestMapping("/cpc")
public class CpcController extends BaseController {

    @Autowired
    private ThreadPoolHelper poolHelper;
    @Autowired
    private AdvertInfoService advertInfoService;


    @RequestMapping(value = "/add")
    @ResponseBody
    public BaseResult addCpc(@Valid AddCpcForm form) {

        logger.info("add cpc send queue req:[" + GsonUtils.pojoToJson(form) + "]");

        int cpcNum = form.getCpcNum();

        String name = form.getAppCode() + "_" + form.getAdvertCode();

        LinkedBlockingQueue<CpcReportDto> queue = ResourceManager.getCpcMap().get(name);

        if (queue != null) {

            for (int i = 0; i < cpcNum; i++) {
                CpcReportDto dto = new CpcReportDto();
                dto.setAdvertCode(form.getAdvertCode());
                dto.setAppCode(form.getAppCode());
                dto.setIdfa(IdfaUtils.generateIdfa());
                if (!StringUtils.isEmpty(form.getOtherParams())) {
                    dto.setIp(IpUtils.getValidIps(1).get(0));
                }

                if (!StringUtils.isEmpty(form.getOtherParams())) {
                    dto.setOtherParams(form.getOtherParams());
                }
                dto.setReportUrl(form.getReportUrl());
                if (!StringUtils.isEmpty(form.getToken())) {
                    dto.setToken(form.getToken());
                }

                if (!StringUtils.isEmpty(form.getOurCallBackUrl())) {
                    dto.setOurCallBackUrl(form.getOurCallBackUrl());
                }

                dto.setFrom(form.getFrom());

                try {
                    queue.put(dto);
                } catch (InterruptedException e) {
                    logger.error("{} put queue {} fail", GsonUtils.pojoToJson(dto), name);
                }

            }
        } else {
            logger.warn("{} cpc queue is not ready", name);
        }

        return new BaseResult(ErrorCode.E200);
    }


    @RequestMapping(value = "/start")
    @ResponseBody
    public BaseResult startCpc(@Valid StartCpcForm form) {

        logger.info("start cpc queue req:[" + GsonUtils.pojoToJson(form) + "]");

        int time = form.getCpcTime()*1000 / form.getCpcNum();
        String name = form.getAppCode() + "_" + form.getAdvertCode();

        AdvertInfo advertInfo = advertInfoService.findById(form.getAdvertCode(), form.getAppCode());
        if (advertInfo == null) {
            return new BaseResult(ErrorCode.E801);
        }

        if (advertInfo.getCpcCircut() == null || advertInfo.getCpcNum() == null || advertInfo.getCpcTime() == null || advertInfo.getCpcCircut() != 1) {
            return new BaseResult(ErrorCode.E904);
        }

        ConstantMaps.sendTimeMap.put(name, time);
        ConstantMaps.sendNumMap.put(name,form.getCpcNum());
        logger.info("send cpc time :{}", time);

        if (!ResourceManager.getCpcMap().containsKey(name)) {
            try {
                ResourceManager.getCpcMap().put(name, new LinkedBlockingQueue<>(150000));
                ConstantMaps.cpcSwitchMap.put(name,"1");
                poolHelper.getExecutorService().execute(new CpcConsume(name));
                poolHelper.getExecutorService().execute(new CpcConsume(name));

                logger.info("{} cpc queue start success", name);

            } catch (Exception e) {
                logger.error("{} cpc queue start fail", name);
                if (ResourceManager.getCpcMap().containsKey(name)) {
                    ResourceManager.getCpcMap().remove(name);
                    ConstantMaps.cpcSwitchMap.put(name,"0");
                }
            }

            return new BaseResult(ErrorCode.E200);
        } else {
            return new BaseResult(ErrorCode.E903);
        }
    }

    @RequestMapping(value = "/stop")
    @ResponseBody
    public BaseResult stopCpc(@Valid StopCpcForm form) {

        logger.info("stop cpc queue req:[" + GsonUtils.pojoToJson(form) + "]");
        String name = form.getAppCode() + "_" + form.getAdvertCode();

        if (ResourceManager.getCpcMap().containsKey(name)) {

            String status = ConstantMaps.cpcSwitchMap.get(name);

            if(StringUtils.isEmpty(status)||"0".equals(status)) {
                logger.info("{} cpc queue is stopping", name);
            } else {
                ConstantMaps.cpcSwitchMap.put(name,"0");
                LinkedBlockingQueue<CpcReportDto> queue = ResourceManager.getCpcMap().get(name);
                if (queue != null) {
                    queue.clear();
                    ResourceManager.getCpcMap().remove(name);
                }
                logger.info("{} cpc queue do stop", name);
            }

            return new BaseResult(ErrorCode.E200);
        } else {
            return new BaseResult(ErrorCode.E905);
        }
    }


}
