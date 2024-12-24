package com.github.binarywang.demo.wx.cp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.demo.wx.cp.entity.DialogueContext;
import com.github.binarywang.demo.wx.cp.entity.DialogueQuestion;
import com.github.binarywang.demo.wx.cp.mapper.DialogueQuestionMapper;
import com.github.binarywang.demo.wx.cp.service.DialogueQuestionService;
import org.springframework.stereotype.Service;


@Service
public class DialogueQuestionServiceImpl extends ServiceImpl<DialogueQuestionMapper, DialogueQuestion> implements DialogueQuestionService {

    @Override
    public long insertQuestion(Long id, DialogueContext dialogueContext) {
        DialogueQuestion dialogueQuestion = new DialogueQuestion();
        dialogueQuestion.setDialogueRecordId(id);
        dialogueQuestion.setAskContent(dialogueContext.getAskContent());
        dialogueQuestion.setAckContent(dialogueContext.getAckContent());
        dialogueQuestion.setErrorCode(dialogueContext.getErrorCode());
        dialogueQuestion.setRegenerate(dialogueContext.getRegenerate());
        dialogueQuestion.setBreakFlag(dialogueContext.getBreakFlag());
        dialogueQuestion.setSessionMode(dialogueContext.getWithHistory());
        dialogueQuestion.setEnvironmentModel(dialogueContext.getFieldType());
        dialogueQuestion.setScore(dialogueContext.getScore());
        dialogueQuestion.setAnswerSource(dialogueContext.getAnswerSource());
        dialogueQuestion.setPlotData(dialogueContext.getPlotData());
        dialogueQuestion.setKgAns(dialogueContext.getKgAns());
        dialogueQuestion.setImageBase64(dialogueContext.getImageBase64());
        dialogueQuestion.setSearchAns(dialogueContext.getSearchAns());
        save(dialogueQuestion);
        return dialogueQuestion.getId();
    }
}
