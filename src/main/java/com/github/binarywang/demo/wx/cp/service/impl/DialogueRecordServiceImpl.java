package com.github.binarywang.demo.wx.cp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.demo.wx.cp.entity.DialogueContext;
import com.github.binarywang.demo.wx.cp.entity.DialogueRecord;
import com.github.binarywang.demo.wx.cp.mapper.DialogueRecordMapper;
import com.github.binarywang.demo.wx.cp.service.DialogueQuestionService;
import com.github.binarywang.demo.wx.cp.service.DialogueRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;



@Service
@Slf4j
public class DialogueRecordServiceImpl extends ServiceImpl<DialogueRecordMapper, DialogueRecord> implements DialogueRecordService {

    @Autowired
    private DialogueQuestionService dialogueQuestionService;

    @Override
    public long processDialogueContext(DialogueContext dialogueContext) {
        DialogueRecord dialogueRecord = selectByRobotAndSession(dialogueContext.getSender(), dialogueContext.getConversationId());
        if (dialogueRecord == null) {
            DialogueRecord dialogueRecordNew = new DialogueRecord();
            dialogueRecordNew.setUserCode(dialogueContext.getSender());
            dialogueRecordNew.setSessionCode(dialogueContext.getConversationId());
            dialogueRecordNew.setDialogueCount(1);
            save(dialogueRecordNew);
            long questionId = dialogueQuestionService.insertQuestion(dialogueRecordNew.getId(), dialogueContext);
            return questionId;
        } else {
            dialogueRecord.setDialogueCount(dialogueRecord.getDialogueCount() + 1);
            updateById(dialogueRecord);
            long questionId = dialogueQuestionService.insertQuestion(dialogueRecord.getId(), dialogueContext);
            return questionId;
        }
    }

    private DialogueRecord selectByRobotAndSession(String userCode, String sessionCode) {
        return baseMapper.selectOne(Wrappers.<DialogueRecord>query().lambda()
            .eq(DialogueRecord::getUserCode, userCode)
            .eq(DialogueRecord::getSessionCode, sessionCode)
            .eq(DialogueRecord::getDelFlag, 0));
    }
}
