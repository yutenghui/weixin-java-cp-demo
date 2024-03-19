
package com.github.binarywang.demo.wx.cp.dto;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Generation {

    @SerializedName("generation_info")
    private GenerationInfo generationInfo;
    @Expose
    private Message message;
    @Expose
    private String text;
    @Expose
    private String type;

    public GenerationInfo getGenerationInfo() {
        return generationInfo;
    }

    public void setGenerationInfo(GenerationInfo generationInfo) {
        this.generationInfo = generationInfo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
