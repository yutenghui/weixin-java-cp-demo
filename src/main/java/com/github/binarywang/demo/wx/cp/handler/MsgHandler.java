package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.builder.TextBuilder;
import com.github.binarywang.demo.wx.cp.dto.ChatAsk;
import com.github.binarywang.demo.wx.cp.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Component
@Slf4j
public class MsgHandler extends AbstractHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ThreadPoolExecutor otherThreadPoolExecutor;

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                    WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        String message = wxMessage.getContent();
        String content = "";
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
        }

        otherThreadPoolExecutor.submit(
            () -> {
                chat(wxMessage);
            }
        );

        log.info("结束请求时间："+System.currentTimeMillis());
        return new TextBuilder().build(content, wxMessage, cpService);

    }

    public void chat(WxCpXmlMessage wxMessage){
        final String msgType = wxMessage.getMsgType();
        String message = wxMessage.getContent();
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
        }
        String openId = wxMessage.getFromUserName();
        if (!msgType.equals(WxConsts.XmlMsgType.EVENT)) {
            ChatAsk chatAsk = new ChatAsk();
            chatAsk.setMessage(message);
            chatAsk.setSender(openId);
            chatService.chat(chatAsk);
        }

    }

}
