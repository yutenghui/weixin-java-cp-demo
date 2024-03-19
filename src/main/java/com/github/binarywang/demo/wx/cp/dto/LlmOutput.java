
package com.github.binarywang.demo.wx.cp.dto;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LlmOutput {

    @SerializedName("model_name")
    private String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
