package com.github.binarywang.demo.wx.cp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.demo.wx.cp.entity.DialogueContext;
import com.github.binarywang.demo.wx.cp.entity.DialogueQuestion;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangjiaxing
 * @since 2023-03-23
 */
public interface DialogueQuestionService extends IService<DialogueQuestion> {

    long insertQuestion(Long id, DialogueContext dialogueContext);

}
