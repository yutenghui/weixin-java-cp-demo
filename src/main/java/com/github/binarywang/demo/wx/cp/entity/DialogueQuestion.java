package com.github.binarywang.demo.wx.cp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjiaxing
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "tb_dialogue_question",autoResultMap = true)
public class DialogueQuestion extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对话记录主键
     */
    private Long dialogueRecordId;

    /**
     * 问题编码
     */
    private String questionCode;

    /**
     * 请求内容
     */
    private String askContent;

    /**
     * 回复内容
     */
    private String ackContent;

    /**
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 执行时间（毫秒）
     */
    private Long exeTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private Integer commentType;


    /**
     * 问题类型
     */
    private String questionType;

    /**
     * 是否合规（0是，1否，2可优化）
     */
    private Integer legalFlag;


    /**
     * 跟进人
     */
    private String followPeople;

    /**
     * 标签核对
     */
    private String lableRemark;

    /**
     * 是否重新生成
     * 0否/1是
     */
    private Integer regenerate;

    /**
     * 中断标志（0否1是）
     */
    private Integer breakFlag;

    /**
     * 1删除
     */
    private Integer userDelFlag;

    /**
     * 会话模式（0单轮，1多轮）
     */
    private Integer sessionMode;

    /**
     * 环境模式 （0：财报 1：百科）
     */
    private Integer environmentModel;

    /**
     * 图片base64
     */
    private String  imageBase64;

    /**
     * 问题评分
     */
    private Integer score;
    /**
     * 答案来源
     */
    private String answerSource;

    /**
     * 表格返回信息
     */
    private String plotData;

    /**
     * 表格返回信息
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

}
