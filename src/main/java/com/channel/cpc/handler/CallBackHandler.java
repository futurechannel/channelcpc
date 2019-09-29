package com.channel.cpc.handler;

import com.channel.cpc.constants.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Created by gq on 2018/4/15.
 */

@Component
public class CallBackHandler {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private RestTemplate template;

    public String callBack(String url) {
        ResponseEntity<String> result;
        try {
            result=template.getForEntity(url,String.class);
        }catch (Exception e){
            logger.error("回调失败,url:"+url,e);
            return Constants.CALL_BACK_FAIL;
        }

        if(result==null|| !HttpStatus.OK.equals(result.getStatusCode())){
            logger.error("回调失败,url:"+url);
            return Constants.CALL_BACK_FAIL;
        }

        logger.info("回调渠道:url:{"+url+"},body:{"+result.getBody()+"}");
        return Constants.CALL_BACK_SUC;
    }

}
