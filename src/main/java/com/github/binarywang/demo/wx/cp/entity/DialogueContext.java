package com.github.binarywang.demo.wx.cp.entity;

import lombok.Data;

@Data
public class DialogueContext {



    /**
     * 请求内容
     */
    private String askContent;

    private String conversationId;

    /**
     * 回复内容
     */
    private String ackContent = "";

    /**
     * 用户标识
     */
    private String sender;

    /**
     * 问题存储标识
     */
    private Boolean storageSwitch;

    /**
     * 对话模式
     * 0 单轮对话
     * 1 多轮对话
     */
    private Integer withHistory;

    /**
     * 问答模式
     * 默认
     * 0：财报
     * 1：通用领域
     */
    private Integer fieldType;

    /**
     * 是否重新生成
     *  0否
     *  1是
     */
    private Integer regenerate;
    /**
     * 中断标志（0否1是）
     */
    private Integer breakFlag;
    /**
     * 问题评分
     */
    private Integer score;

    /**
     * 答案来源
     */
    private String answerSource;

    /**
     * 表格信息
     */
    private String plotData;

    /**
     * 表格信息
     */
    private Integer errorCode;

    /**
     * 图谱参考答案
     */
    private String kgAns;

    /**
     * 检索参考答案
     */
    private String searchAns;


    /**
     * 线程名字
     */
    private String threadName;

    /**
     * questid
     */
    private long questionId;

    /**
     * questid
     */
    private String askContents= "" ;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 渠道
     */
    private String imageBase64;

    /**
     * 请求开始时间
     */
    private long  startTime;

    private Integer agentid;
}
