package com.fibo.rule.server.enums;

public enum ErrorCodeEnum {

    //
    SERVER_ERROR(ErrorCodeEnum.ERROR_CODE + 101, "服务繁忙,请稍后再试!","The service is busy, please try again later"),
    //
    LOGIN_ERROR(ErrorCodeEnum.ERROR_CODE + 102, "登录失败","login fail"),
    //
    ERROR_TOKEN_EXPIRE(ErrorCodeEnum.ERROR_CODE + 103, "登录授权码已过期","The login authorization code has expired"),
    //
    PARAMS_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 104, "参数异常","Parameter exception"),
    CLASS_CAST_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 105, "类型转换异常","Type conversion exception"),
    NULL_POINT_EREXCEPTION(ErrorCodeEnum.ERROR_CODE + 106, "NPE问题，请联系管理员","NPE problem, please contact the administrator"),
    REFERER_ERROR(ErrorCodeEnum.ERROR_CODE + 107, "访问来源错误","Access source error"),
    UPDATE_ERROR(ErrorCodeEnum.ERROR_CODE + 108, "修改失败","Modification failed"),
    INIT_PASSWORD_NOT_LOGIN(ErrorCodeEnum.ERROR_CODE + 109, "请勿使用初始密码登录","Do not log in with the initial password"),
    CELLPHONE_IS_EXIST(ErrorCodeEnum.ERROR_CODE + 110, "手机号已存在","Mobile number already exists"),
    PASSWORD_RULE_ERROR(ErrorCodeEnum.ERROR_CODE + 111, "密码规则错误","Password rule error"),
    VERIFICATION_CODE_ERROR(ErrorCodeEnum.ERROR_CODE + 112, "登录验证码错误","Login verification code error"),
    CELLPHONE_IS_NULL(ErrorCodeEnum.ERROR_CODE + 113, "手机号不存在","Mobile number does not exist"),
    PASSWORD_ERROR(ErrorCodeEnum.ERROR_CODE + 114, "密码错误","Password error"),
    ACCOUNT_LOCKED_ERROR(ErrorCodeEnum.ERROR_CODE + 115, "账号已锁定","Account locked"),
    ACCOUNT_DISABLE_ERROR(ErrorCodeEnum.ERROR_CODE + 116, "账号已禁用","account is disabled"),
    PASSWORD_TODAY_EDITED_ERROR(ErrorCodeEnum.ERROR_CODE + 117, "今日密码已修改,请隔日修改","The password has been changed today. Please change it the next day"),
    PASSWORD_SAME_ERROR(ErrorCodeEnum.ERROR_CODE + 118, "请设置与最近不相同的密码","Please set a password that is different from the latest one"),
    PASSWORD_DELAY_ERROR(ErrorCodeEnum.ERROR_CODE + 119, "密码超过有效期,请修改密码后登录","The password has expired. Please change the password and log in"),
    ACCOUNT_UNLOGIN_LONGTIME_ERROR(ErrorCodeEnum.ERROR_CODE + 120, "账号超过预设有效期未登录","The account has exceeded the preset validity period and is not logged in"),
    ACCOUNT_STATUS_ERROR(ErrorCodeEnum.ERROR_CODE + 121, "账号状态异常","Abnormal account status"),
    ACCOUNT_LOGINERR_TIMES_ERROR(ErrorCodeEnum.ERROR_CODE + 122, "账号登录失败超过预设次数","Account login failure exceeds the preset number of times"),
    NO_ACCESS(ErrorCodeEnum.ERROR_CODE + 123, "无权访问","No access"),
    NO_KEY_VERSION(ErrorCodeEnum.ERROR_CODE + 124, "获取密钥版本失败","No key version"),
    VERIFICATION_IMG_CODE_ERROR(ErrorCodeEnum.ERROR_CODE + 125, "图形验证码错误","Graphic verification code error"),
    NO_OPERATION_PERMISSION(ErrorCodeEnum.ERROR_CODE + 126, "无操作权限","No operation permission"),

    CREATE_USER_NAME_ERROR(ErrorCodeEnum.ERROR_CODE + 201, "姓名不能为超级管理员","Name cannot be super administrator"),
    CREATE_USER_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE + 202, "账号或员工编号已存在","Account number or employee number already exists"),
    CREATE_ROLE_ADMIN_REPEAT(ErrorCodeEnum.ERROR_CODE + 203, "每个公司只能创建一个公司管理员","Only one company administrator can be created per company"),
    CREATE_ROLE_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE + 204, "角色名已存在","Role name already exists"),
    CREATE_MENU_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE + 205, "名称或编号已存在","Name or number already exists"),
    CREATE_ORGAN_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE + 206, "名称或编号已存在","Name or number already exists"),

    FIELD_TYPE_REPEAT(ErrorCodeEnum.ERROR_CODE + 114, "字段类型已存在","Field type already exists"),
    FIELD_EN_REPEAT(ErrorCodeEnum.ERROR_CODE + 115, "字段英文名已存在","Field English name already exists"),
    FIELD_CN_REPEAT(ErrorCodeEnum.ERROR_CODE + 116, "字段中文名已存在","Field Chinese name already exists"),
    FIELD_BE_USERD(ErrorCodeEnum.ERROR_CODE + 117, "字段被使用，无法修改","Field is used and cannot be modified"),
    FIELD_BE_LOOP(ErrorCodeEnum.ERROR_CODE + 118, "指标不能循环引用","Field cannot be referenced circularly"),
    FIELD_BUILTIN_EDIT(ErrorCodeEnum.ERROR_CODE + 119, "内置指标不能操作","Built in field cannot be modified"),
    FIELD_BUILTIN_DELETE(ErrorCodeEnum.ERROR_CODE + 120, "内置指标不能删除","Built in field cannot be delete"),
    RULE_UPLOAD_ERROR(ErrorCodeEnum.ERROR_CODE+123,"规则导入失败","Rule import failed"),
    DECISION_TABLES_CODE_REPEAT(ErrorCodeEnum.ERROR_CODE + 124, "决策表代码已存在","Decision table code already exists"),
    DECISION_TABLES_SAVE_ERROR(ErrorCodeEnum.ERROR_CODE+125,"决策表保存失败","Failed to save decision table"),
    DECISION_TABLES_UPDATE_ERROR(ErrorCodeEnum.ERROR_CODE+126,"决策表修改状态失败","Failed to modify the status of decision table"),
    DECISION_TABLES_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE + 127, "决策表名称已存在","Decision table name already exists"),
    DECISION_TREE_SAVE_ERROR(ErrorCodeEnum.ERROR_CODE+130,"决策树保存失败","Failed to save decision tree"),
    DECISION_TREE_UPDATE_ERROR(ErrorCodeEnum.ERROR_CODE+131,"决策树修改状态失败","Failed to modify the status of decision tree"),
    LIST_OPERATION_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE+132,"集合操作名称重复","Duplicate collection operation name"),
    LIST_OPERATION_CODE_REPEAT(ErrorCodeEnum.ERROR_CODE+133,"集合操作代码重复","Duplicate set operation code"),
    TAG_SAVE_ERROR(ErrorCodeEnum.ERROR_CODE+134,"标签保存失败","Label saving failed"),
    TAG_UPDATE_ERROR(ErrorCodeEnum.ERROR_CODE+135,"标签修改状态失败","Label modification status failed"),
    ARGS_JSON_TYPE_ERROR(ErrorCodeEnum.ERROR_CODE + 301, "JSON类型转换错误","JSON type conversion error"),
    INTERFACE_SAVE_ERROR(ErrorCodeEnum.ERROR_CODE+302,"接口保存失败","Interface saving failed"),
    SUCCESS("00000000", "执行成功","success"),
    GET_REDIS_SOURCE_ERROR(ErrorCodeEnum.ERROR_CODE + 303, "获取Redis数据源失败","Failed to get redis data source"),
    GET_DATABASE_FIELD_ERROR(ErrorCodeEnum.ERROR_CODE + 304, "获取数据库指标失败","Failed to get database indicators"),
    RUNNER_CUSTOM_ERROR(ErrorCodeEnum.ERROR_CODE + 305, "执行自定义失败","Failed to execute customization"),
    GET_INTERFACE_FIELD_ERROR(ErrorCodeEnum.ERROR_CODE + 306, "获取接口指标错误","Error getting interface index"),
    SQL_FIELD_HAVE_RISK(ErrorCodeEnum.ERROR_CODE+307,"存在有风险sql关键词" ,"Risky SQL keywords"),
    INTERFACE_ADDRESS_PARAM_ERROR(ErrorCodeEnum.ERROR_CODE+308,"接口地址或参数有误" ,"The interface address or parameter is incorrect"),
    RULE_CODE_REPEAT(ErrorCodeEnum.ERROR_CODE+309,"规则code重复" ,"This code already exists"),
    RULE_NAME_REPEAT(ErrorCodeEnum.ERROR_CODE+310,"规则名称重复" ,"This name already exists"),
    RULE_VERSION_CODE_REPEAT(ErrorCodeEnum.ERROR_CODE+311,"版本名称重复" ,"This version name already exists"),
    RULE_VERSION_HAS_REF(ErrorCodeEnum.ERROR_CODE+312,"该规则版本被引用" ,"This rule version is referenced"),



    RUNNER_PROMOTION_TEST_ERROR(ErrorCodeEnum.ERROR_CODE + 401, "执行活动测试出错","Error executing promotion test"),


    SELECT_ERROR(ErrorCodeEnum.ERROR_CODE + 501, "查询异常!","Query exception"),
    INSERT_ERROR(ErrorCodeEnum.ERROR_CODE + 502, "新增异常!","New exception"),
    FILE_EMPTY(ErrorCodeEnum.ERROR_CODE + 503, "文件不能为空!","File cannot be empty"),
    FIELD_EMPTY(ErrorCodeEnum.ERROR_CODE + 504, "字段不可为空!","Field cannot be empty"),
    PARAM_EMPTY(ErrorCodeEnum.ERROR_CODE + 505, "参数不可为空!","Parameter cannot be empty"),
    RECORD_EMPTY(ErrorCodeEnum.ERROR_CODE + 506, "记录不能为空!","Record cannot be empty"),
    PROMOTION_NOT_EXIST(ErrorCodeEnum.ERROR_CODE + 507, "活动不存在!","Promotion does not exist"),
    MERCHANT_NOT_EXIST(ErrorCodeEnum.ERROR_CODE + 508, "商户不存在!","Merchant does not exist"),
    MODEL_NOT_EXIST(ErrorCodeEnum.ERROR_CODE + 509, "模型不存在!","Model does not exist"),
    END_TIME_BEFORE_START_TIME(ErrorCodeEnum.ERROR_CODE + 510, "生效时间不能大于失效时间","The effective time cannot be greater than the expiration time"),
    END_TIME_BEFORE_CURRENT_TIME(ErrorCodeEnum.ERROR_CODE + 511, "失效时间不能小于当前时间","The expiration time cannot be less than the current time"),
    FIELD_INCOMPLETE(ErrorCodeEnum.ERROR_CODE + 512, "字段信息不完整","Incomplete field information"),
    MERCHANT_BLACK_TEMPLATE_ERROR(ErrorCodeEnum.ERROR_CODE + 513, "商户黑名单模板数据错误","Merchant blacklist template data error"),
    MERCHANT_TEMPLATE_ERROR(ErrorCodeEnum.ERROR_CODE + 514, "商户模板数据错误","Merchant template data error"),
    MERCHANT_LEVEL_NOT_MATCH(ErrorCodeEnum.ERROR_CODE + 515, "商户等级不匹配","Merchant level mismatch"),
    TIME_FORMAT_ERROR(ErrorCodeEnum.ERROR_CODE + 516, "时间格式错误","Time format error"),

    /**报文标准化转换异常*/
    CONVERT_Transaction_BODY_ERROR(ErrorCodeEnum.ERROR_CODE + 201, "报文标准化转换异常"),
    /**命中卡号全局黑名单*/
    CARD_GLOBAL_BLACK_LIST(ErrorCodeEnum.ERROR_CODE + 202, "命中卡号全局黑名单"),
    /**命中商户全局黑名单*/
    MERCHANT_GLOBAL_BLACK_LIST(ErrorCodeEnum.ERROR_CODE + 203, "命中商户全局黑名单"),
    /**活动匹配异常*/
    ACTIVE_MATCHING_ERROR(ErrorCodeEnum.ERROR_CODE + 204, "活动匹配异常"),
    /**活动执行异常*/
    ACTIVE_EXECUTE_ERROR(ErrorCodeEnum.ERROR_CODE + 205, "活动执行异常"),
    /**活动结果组装异常*/
    ACTIVE_ASSEMABLE_ERROR(ErrorCodeEnum.ERROR_CODE + 206, "活动结果组装异常"),

    /**交易金额不正确*/
    Transaction_POINTS_ERROR(ErrorCodeEnum.ERROR_CODE + 207,"交易金额不正确"),

    /**获取活动基本信息失败*/
    GET_PROMOTION_BASEINFO_ERROR(ErrorCodeEnum.ERROR_CODE + 210,"获取活动基本信息失败"),
    /**获取活动限额规则失败*/
    GET_PROMOTION_QUOTA_RULE_ERROR(ErrorCodeEnum.ERROR_CODE + 211,"获取活动限额规则失败"),
    /**获取策略中心规则失败*/
    GET_STRATEGY_RULE_ERROR(ErrorCodeEnum.ERROR_CODE + 212,"获取策略中心规则失败"),

    /**存在未处理/未补偿的交易记录*/
    HAVE_UNTREATED_Transaction_RECORD(ErrorCodeEnum.ERROR_CODE + 301,"存在未处理/未补偿的交易记录"),


    STRATEGY_CACHE_NOT_FOUND(ErrorCodeEnum.ERROR_CODE+310,"策略缓存未找到"),

    /**返现规则配置错误*/
    RETURN_MONEY_RULE_CONFIG_ERROR(ErrorCodeEnum.ERROR_CODE + 401, "返现规则配置错误"),

    /**消息队列指标配置错误*/
    FIELD_KAFKA_CONFIG_ERROR(ErrorCodeEnum.ERROR_CODE + 402, "消息队列指标配置错误");

    /**
     * 默认ERROR_CODE.<br>
     * 按公司要求8位长度，前两位产品。
     */
    public static final String ERROR_CODE = "01000";

    private String code;
    private String message;
    private String enUsMessage;

    private ErrorCodeEnum(String code, String message, String enUsMessage) {
        this.code = code;
        this.message = message;
        this.enUsMessage = enUsMessage;
    }

    private ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getEnUsMessage() {
        return enUsMessage;
    }

}
