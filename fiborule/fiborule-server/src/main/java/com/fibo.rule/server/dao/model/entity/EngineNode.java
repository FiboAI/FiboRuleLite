package com.fibo.rule.server.dao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_engine_node")
public class EngineNode {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long engineId;

    private String nodeName;

    private String beanName;

    private String nodeCode;
    
    private Integer nodeType;

    private String preNodes;

    private String nextNodes;

    private String nodeConfig;
    
    private String nodeX;

    private String nodeY;

    private String nodeClazz;

    private String clazzName;

    private String nodeGroup;

    //后续节点对应的分支值-[{"key":"Y",value:"node1"}]
    private String nextNodeValue;

    private Integer status;

    private LocalDateTime createTime;

    private Long createUser;

    private LocalDateTime updateTime;

    private Long updateUser;

    private Integer delFlag;
}
