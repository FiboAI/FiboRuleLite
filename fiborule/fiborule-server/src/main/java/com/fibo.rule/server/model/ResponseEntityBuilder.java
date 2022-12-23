package com.fibo.rule.server.model;

import com.alibaba.druid.util.StringUtils;
import com.fibo.rule.server.enums.ErrorCodeEnum;

public final class ResponseEntityBuilder {
    /**
     * 返回成功.
     */
    public static final String RESPONSE_OK = "1";
    /**
     * 返回失败.
     */
    public static final String RESPONSE_FAIL = "0";

    /**
     * 构造一个失败响应.<br>
     * 用于构造一个比较用见或是常用的失败响应。<br>
     *
     * @param enums 常见错误枚举类的一个实例.<br>
     * @return
     */
    public static <T> ResponseEntityDto<T> buildErrorResponse(ErrorCodeEnum enums) {
        ResponseEntityDto<T> entity = new ResponseEntityDto<>();
        entity.setStatus(RESPONSE_FAIL);
        entity.setError(String.valueOf(enums.getCode()));
        entity.setMsg(enums.getMessage());
        return entity;
    }

//	/**
//	 * 构造一个失败响应.<br>
//	 *
//	 * @inputParam enums
//	 * @return
//	 */
//	@Deprecated
//	public static ResponseEntity<String> buildErrorResponse(ErrorCodeEnum enums) {
//		ResponseEntity<String> model = new ResponseEntity<String>();
//		model.setStatus(RESPONSE_FAIL);
//		model.setError(String.valueOf(enums.getVersionCode()));
//		model.setMsg(enums.getMessage());
//		return model;
//	}

    /**
     * 构造一个失败响应.<br>
     *
     * @param error   失败错误编码.<br>
     * @param message 失败错误说明.<br>
     * @return
     */
    public static <T> ResponseEntityDto<T> buildErrorResponse(String error, String message) {
        ResponseEntityDto<T> entity = new ResponseEntityDto<>();
        entity.setStatus(RESPONSE_FAIL);
        if (StringUtils.isEmpty(error) || !StringUtils.isNumber(error)) {
            error = "401";
        }
        entity.setError(error);
        entity.setMsg(message);
        return entity;
    }

    /**
     * 构造一个正常响应.<br>
     * <p>
     * 响应数据.<br>
     *
     * @return
     */
    public static <T> ResponseEntityDto<T> buildNormalResponse() {
        //规范：所有正常请求(status="1",error="01000000"),code与pageCount属性被废弃.
        return new ResponseEntityDto<>(RESPONSE_OK, "00000000");
    }

    /**
     * 构造一个正常响应.<br>
     *
     * @param data 响应数据.<br>
     * @return
     */
    public static <T> ResponseEntityDto<T> buildNormalResponse(T data) {
        ResponseEntityDto<T> entity = buildNormalResponse();
        entity.setData(data);
        return entity;
    }

    /**
     * 构造一个错误响应.<br>
     *
     * @param data 响应数据.<br>
     * @return
     */
    public static <T> ResponseEntityDto<T> buildUnNormalResponse(T data, ErrorCodeEnum enums) {
        ResponseEntityDto<T> entity = buildErrorResponse(enums);
        entity.setData(data);
        return entity;
    }
//	public static <T> ResponseEntityDto<Map<String,T>> buildNormalResponse(String dictKey,T data) {
//		Map<String,T> result = new HashMap<>();
//		result.put(dictKey, data);
//		
//		ResponseEntityDto<Map<String,T>> model = buildNormalResponse();
//		model.setData(result);
//		return model;
//	}

    public static <T> Boolean isSuccess(ResponseEntityDto<T> entity) {
        return entity.getStatus().equals(RESPONSE_OK);
    }

    public static <T> Boolean isSuccess2(ResponseEntityDto<T> entity) {
        return entity.getStatus().equals(RESPONSE_OK) && entity.getError().equals("00000000");
    }

    /**
     * 适用于接口返回data为null时业务不成功的场景
     */
    public static <T> Boolean isSuccess3(ResponseEntityDto<T> entity) {
        return entity.getStatus().equals(RESPONSE_OK) && entity.getError().equals("200") && entity.getData() != null;
    }

    public static <T> T getEntity(ResponseEntityDto<T> entity) {
        if (isSuccess(entity)) {
            return (T) entity.getData();

        }
        return null;
    }

}
