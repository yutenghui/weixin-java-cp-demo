package com.github.binarywang.demo.wx.cp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.demo.wx.cp.entity.DialogueContext;
import com.github.binarywang.demo.wx.cp.entity.DialogueRecord;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangjiaxing
 * @since 2023-03-23
 */
public interface DialogueRecordService extends IService<DialogueRecord> {

    long processDialogueContext(DialogueContext dialogueContext);

}
