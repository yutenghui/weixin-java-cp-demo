package com.github.binarywang.demo.wx.cp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
@TableName("tb_dialogue_record")
public class DialogueRecord extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户标识
     */
    private String userCode;

    /**
     * 机器人编码
     */
    private String robotCode;

    /**
     * 机器人名称
     */
    private String robotName;

    /**
     * 会话标识
     */
    private String sessionCode;

    /**
     * 对话轮数
     */
    private Integer dialogueCount;

    /**
     * 删除标识 0:存在 1:不存在
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 渠道
     */
    private String channel;

}
