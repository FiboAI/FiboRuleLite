package com.fibo.rule.server.dao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("t_engine")
public class Engine {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long appId;
    
    private String engineName;

    private String engineCode;
    
    private String scene;

    private String descriptions;

    private Integer bootStatus;
    
    private Integer status;

    private LocalDateTime createTime;
    
    private Long createUser;

    private LocalDateTime updateTime;

    private Long updateUser;

    private Integer delFlag;
    
    
}
