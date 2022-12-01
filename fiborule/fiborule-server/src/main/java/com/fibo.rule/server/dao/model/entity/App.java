package com.fibo.rule.server.dao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_app")
public class App {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String appName;

    private String appCode;

    private String descriptions;

    private Integer status;

    private LocalDateTime createTime;

    private Integer createUser;

    private LocalDateTime updateTime;

    private Integer updateUser;

    private Integer delFlag;
}
