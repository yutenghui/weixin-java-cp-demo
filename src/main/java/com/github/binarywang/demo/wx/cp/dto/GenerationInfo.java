
package com.github.binarywang.demo.wx.cp.dto;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GenerationInfo {

    @SerializedName("finish_reason")
    private String finishReason;

    private String reference;

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

}
