package com.github.binarywang.demo.wx.cp.dto;

import lombok.Data;

/**
 * @Author: yth
 * @Date: 2024/3/19 10:51
 * @Version 1.0
 * @Description
 */
@Data
public class ChatAsk {

    private String message;

    private String sender;

    private String rasa_url = "";

    private MetaData metadata = new MetaData();

    private Integer stream = 1;

    private Integer agentid;

}
