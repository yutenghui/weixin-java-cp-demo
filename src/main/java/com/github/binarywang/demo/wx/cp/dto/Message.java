
package com.github.binarywang.demo.wx.cp.dto;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Message {

    @SerializedName("additional_kwargs")
    private AdditionalKwargs additionalKwargs;
    @Expose
    private String content;
    @Expose
    private Boolean example;
    @Expose
    private String type;

    public AdditionalKwargs getAdditionalKwargs() {
        return additionalKwargs;
    }

    public void setAdditionalKwargs(AdditionalKwargs additionalKwargs) {
        this.additionalKwargs = additionalKwargs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getExample() {
        return example;
    }

    public void setExample(Boolean example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
