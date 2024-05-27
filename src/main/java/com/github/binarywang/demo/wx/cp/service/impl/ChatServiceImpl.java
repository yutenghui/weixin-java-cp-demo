package com.github.binarywang.demo.wx.cp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.cp.dto.ChatAnswer;
import com.github.binarywang.demo.wx.cp.dto.ChatAsk;
import com.github.binarywang.demo.wx.cp.dto.ChatResponse;
import com.github.binarywang.demo.wx.cp.dto.Generation;
import com.github.binarywang.demo.wx.cp.service.ChatService;
import com.github.binarywang.demo.wx.cp.service.WeChatService;
import com.github.binarywang.demo.wx.cp.utils.JsonUtils;
import com.github.binarywang.demo.wx.cp.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.TimeUnit;

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

    @Autowired
    private WeChatService weChatService;

    @Override
    public void chat(ChatAsk chatAsk){
        SseEmitter emitter = new SseEmitter(300000L);
        String param = JsonUtils.toJson(chatAsk);
        Request request = OkHttpUtils.buildRequest(modelChatUrl, param);
        OkHttpClient okHttpClient = buildSSEClient(request, emitter, chatAsk);
        execute(okHttpClient,request,emitter);
    }


    public void analysisResponseStr(ChatAsk chatAsk,String res){
        String name = chatAsk.getSender();
        log.info("chat response："+res);
        String content = null;
        if (!StringUtils.isEmpty(res)){
            ChatResponse chatResponse = JSONObject.parseObject(res, ChatResponse.class);
            ChatAnswer chatAnswer = chatResponse.getData();
            Integer status = chatResponse.getStatus();
            String event = chatResponse.getEvent();
            Generation generation = chatAnswer.getGenerations().get(0);
            String reference = generation.getGenerationInfo().getReference();
            String text = generation.getText();
            //结束标志
            if (status == 1){
                if ("llm".equals(event)){
                    if (StringUtils.isNotBlank(reference)){
                        text = text + "\n\n参考" + reference;
                    }else {
                        text = text + "\n\n此回复基于大模型算法自动生成。";
                    }
                }

            }
            content = text;
        }else {
            content = "智能助手开小差了，请稍后再试";
        }
        log.info("推送消息至企业微信");
        weChatService.send(name,content);
    }



    public OkHttpClient buildSSEClient(Request request, SseEmitter emitter,ChatAsk chatAsk) {

        OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder().connectTimeout(300 * 1000, TimeUnit.MILLISECONDS)
            .readTimeout(300 * 1000, TimeUnit.MILLISECONDS)
            .build();

        EventSource.Factory factory = EventSources.createFactory(okHttpClient);
        EventSourceListener eventSourceListener = new EventSourceListener() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onOpen(final EventSource eventSource, final okhttp3.Response response) {
                log.info("sse open");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onEvent(final EventSource eventSource, final String id, final String type, final String data) {
                log.info("流式返回字段信息为:{}", data);
                try {
                    analysisResponseStr(chatAsk,data);
                } catch (Exception ioException) {
                    emitter.complete();
                    eventSource.cancel();
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onClosed(final EventSource eventSource) {
                emitter.complete();
                eventSource.cancel();
                log.info("结束关闭");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onFailure(final EventSource eventSource, final Throwable t, final okhttp3.Response response) {
                log.error("流式连接失败失败信息:{}", t.getMessage(), t);
                analysisResponseStr(chatAsk,"");
                emitter.complete();
                eventSource.cancel();
                log.info("失败关闭");
            }
        };
        //创建事件
        factory.newEventSource(request, eventSourceListener);
        return okHttpClient;
    }


    private void execute(OkHttpClient client, Request request, SseEmitter emitter) {
        try {
            client.newCall(request).execute();
        } catch (Exception ioException) {
            log.error("{}.exception: {}", ioException.getMessage(), ioException);
            emitter.complete();
        }

    }
}
