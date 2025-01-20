
package com.github.binarywang.demo.wx.cp.dto;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ChatReq {

    private Inputs inputs;

    private String query;

    private Boolean streaming;

    private Boolean withHistory;

}
