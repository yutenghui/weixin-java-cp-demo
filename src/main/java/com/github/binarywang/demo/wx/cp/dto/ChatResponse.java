package com.github.binarywang.demo.wx.cp.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: yth
 * @Date: 2024/5/23 15:29
 * @Version 1.0
 * @Description
 */
@Data
public class ChatResponse {

    @SerializedName("finish_reason")
    private String finishReason;

    private ChatAnswer data;

    private Integer status;

    private String event;
}
