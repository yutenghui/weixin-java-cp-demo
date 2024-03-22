package com.github.binarywang.demo.wx.cp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.cp.dto.ChatAnswer;
import com.github.binarywang.demo.wx.cp.dto.ChatAsk;
import com.github.binarywang.demo.wx.cp.service.ChatService;
import com.github.binarywang.demo.wx.cp.utils.JsonUtils;
import com.github.binarywang.demo.wx.cp.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: yth
 * @Date: 2024/3/19 11:35
 * @Version 1.0
 * @Description
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Value("${model.chat.url}")
    private String modelChatUrl;

    @Override
    public String chat(ChatAsk chatAsk){
        String ask = JsonUtils.toJson(chatAsk);
        String res = OkHttpUtils.post(modelChatUrl, ask);
        log.info("chat response："+res);
        if (!StringUtils.isEmpty(res)){
            ChatAnswer chatAnswer = JSONObject.parseObject(res, ChatAnswer.class);
            String text = chatAnswer.getGenerations().get(0).getText();
            text = text + "\n此回复基于大模型算法自动生成，仅供参考。";
            return text;
        }else {
            return "智能助手开小差了，请稍后再试";
        }
    }
}
