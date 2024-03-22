package com.github.binarywang.demo.wx.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeixinMessage {

    private String touser;
    private String msgtype;
    private Integer agentid;
    private Map<String,Object> text;
}
