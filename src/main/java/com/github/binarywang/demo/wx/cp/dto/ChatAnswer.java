
package com.github.binarywang.demo.wx.cp.dto;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ChatAnswer {

    @Expose
    private List<Generation> generations;
    @SerializedName("llm_output")
    private LlmOutput llmOutput;

    public List<Generation> getGenerations() {
        return generations;
    }

    public void setGenerations(List<Generation> generations) {
        this.generations = generations;
    }

    public LlmOutput getLlmOutput() {
        return llmOutput;
    }

    public void setLlmOutput(LlmOutput llmOutput) {
        this.llmOutput = llmOutput;
    }

}
