package com.fibo.rule.server.dao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("t_app")
public class App {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String appName;

    private String appCode;

    private String descriptions;

    private Integer status;

    private LocalDateTime createTime;

    private Long createUser;

    private LocalDateTime updateTime;

    private Long updateUser;

    private Integer delFlag;
    
    @TableField(exist = false)
    private List<String> clients;
}
