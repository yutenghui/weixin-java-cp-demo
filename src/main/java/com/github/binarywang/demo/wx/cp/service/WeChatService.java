package com.github.binarywang.demo.wx.cp.service;

/**
 * @Author: yth
 * @Date: 2024/3/21 18:35
 * @Version 1.0
 * @Description
 */
public interface WeChatService {

    public String getAccessToken();

    public void send(String touser,String content);

    public void send(String touser,String content,Integer agentid);

}
