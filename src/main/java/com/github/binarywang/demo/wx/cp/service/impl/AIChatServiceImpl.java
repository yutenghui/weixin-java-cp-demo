package com.github.binarywang.demo.wx.cp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.cp.dto.*;
import com.github.binarywang.demo.wx.cp.dto.flow.LlmFlowData;
import com.github.binarywang.demo.wx.cp.dto.flow.LlmResponse;
import com.github.binarywang.demo.wx.cp.entity.DialogueContext;
import com.github.binarywang.demo.wx.cp.service.ChatService;
import com.github.binarywang.demo.wx.cp.service.DialogueRecordService;
import com.github.binarywang.demo.wx.cp.service.WeChatService;
import com.github.binarywang.demo.wx.cp.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yth
 * @Date: 2024/8/28 18:40
 * @Version 1.0
 * @Description
 */
@Service
@Slf4j
public class AIChatServiceImpl implements ChatService {

    @Value("${model.chat.massUrl}")
    private String modelChatUrl;

    @Value("${model.chat.apikey}")
    private String apikey;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private DialogueRecordService dialogueRecordService;

    @Override
    public void chat(ChatAsk chatAsk){
        SseEmitter emitter = new SseEmitter(300000L);
        DialogueContext dialogueContext = initDialogueContext(chatAsk);
        ChatReq chatReq = new ChatReq();

        Inputs inputs = new Inputs();
        inputs.setQuery(chatAsk.getMessage());
        chatReq.setInputs(inputs);

        chatReq.setQuery(chatAsk.getMessage());
        chatReq.setStreaming(true);
        chatReq.setConversation_id(dialogueContext.getConversationId());
        String param = JSON.toJSONString(chatReq);
        log.info("调用模型参数："+param);
        Request request = OkHttpUtils.buildRequest(modelChatUrl, param);

        OkHttpClient okHttpClient = buildSSEClient(request, emitter, dialogueContext);
        execute(okHttpClient,request,emitter);
    }


    public void analysisResponseStr(String res, DialogueContext dialogueContext){
        String name = dialogueContext.getSender();
        StringBuilder stringBuffer = new StringBuilder(dialogueContext.getAckContent());
        log.info("chat response："+res);
        String content = null;
        if (!StringUtils.isEmpty(res)){
            LlmResponse llmResponse = JSONObject.parseObject(res, LlmResponse.class);
            String type = llmResponse.getNode_type();
            if (type.equals("End")){
                content = llmResponse.getData().getOutputs().getAnswer();
                stringBuffer.append(content);
                dialogueContext.setAckContent(stringBuffer.toString());

                log.info("推送消息至企业微信");
                weChatService.send(name,dialogueContext.getAckContent(),dialogueContext.getAgentid());
                processResSuccess(dialogueContext);
            }
        }else {
            content = "智能助手开小差了，请稍后再试";
            weChatService.send(name,content,dialogueContext.getAgentid());
        }
    }



    public OkHttpClient buildSSEClient(Request request, SseEmitter emitter,DialogueContext dialogueContext) {

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
                    analysisResponseStr(data,dialogueContext);
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
                emitter.complete();
                eventSource.cancel();
                log.error("失败关闭");
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


    public DialogueContext initDialogueContext(ChatAsk chatAsk) {
        DialogueContext dialogueContext = new DialogueContext();
        dialogueContext.setSender(chatAsk.getSender());
        dialogueContext.setAgentid(chatAsk.getAgentid());
        dialogueContext.setAskContent(chatAsk.getMessage());
        dialogueContext.setConversationId(chatAsk.getSender().hashCode()+ DateFormatUtils.format(new Date(),"yyyyMMdd"));
        return dialogueContext;
    }


    private void processResSuccess(DialogueContext dialogueContext) {

        long questionId = dialogueRecordService.processDialogueContext(dialogueContext);
        dialogueContext.setQuestionId(questionId);
    }
}
