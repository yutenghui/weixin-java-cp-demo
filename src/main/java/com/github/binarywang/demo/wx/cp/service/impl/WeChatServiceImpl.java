package com.github.binarywang.demo.wx.cp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.cp.constant.QiyeWXConstant;
import com.github.binarywang.demo.wx.cp.dto.ChatAnswer;
import com.github.binarywang.demo.wx.cp.dto.WeixinMessage;
import com.github.binarywang.demo.wx.cp.service.WeChatService;
import com.github.binarywang.demo.wx.cp.utils.OkHttpUtils;
import com.github.binarywang.demo.wx.cp.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yth
 * @Date: 2024/3/21 18:36
 * @Version 1.0
 * @Description
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String corpid = "ww4832047240e12766";
    private static final String corpsecret = "MDgooEqpsmTKem6HFrH_EBC16cqDANG1lMm-wua_MI8";
    private static final String accessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    private static final Integer agentid = 1000036;
    private static final String messageSendUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    @Override
    public String getAccessToken(){
        String accessToken = "";
        Object cacheObject = redisUtil.getCacheObject(QiyeWXConstant.HR_ACCESS_TOKEN);
        if (cacheObject != null){
            accessToken = String.valueOf(cacheObject);
        }else {
            log.info("请求access_token");
            Map<String,Object> params = new HashMap<>();
            params.put("corpid", corpid);
            params.put("corpsecret", corpsecret);
            String result = OkHttpUtils.get(accessTokenUrl, null, params);
            if (StringUtils.isEmpty(result)){
                log.error("获取access_token失败");
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            accessToken = jsonObject.getString("access_token");
            redisUtil.setCacheObject(QiyeWXConstant.HR_ACCESS_TOKEN,accessToken,30, TimeUnit.MINUTES);
        }
        return accessToken;
    }


    @Override
    public void send(String touser, String content){
        String accessToken = getAccessToken();
        log.info("推送用户："+touser);
        log.info("推送消息："+content);
        Map<String,Object> text = new HashMap<>();
        text.put("content",content);
        WeixinMessage weixinMessage = new WeixinMessage();
        weixinMessage.setTouser(touser);
        weixinMessage.setMsgtype("text");
        weixinMessage.setAgentid(agentid);
        weixinMessage.setText(text);
        String res = OkHttpUtils.post(messageSendUrl + accessToken, JSON.toJSONString(weixinMessage));
        if (StringUtils.isEmpty(res)){
            log.error("推送消息到企业微信失败");
        }
    }
}
