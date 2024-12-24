package com.github.binarywang.demo.wx.cp.dto.flow;

import lombok.Data;

/**
 * @Author: yth
 * @Date: 2024/9/5 15:47
 * @Version 1.0
 * @Description
 */
@Data
public class LlmResponse {

    private String node_type;

    private LlmFlowData data;
}
