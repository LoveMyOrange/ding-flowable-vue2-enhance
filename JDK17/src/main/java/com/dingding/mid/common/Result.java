package com.dingding.mid.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@Schema(name = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@Schema(name = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@Schema(name = "返回代码")
	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */
	@Schema(name = "返回数据对象")
	private T result;

	/**
	 * 返回数据对象 data
	 */
	@Schema(name = "返回数据对象")
	private T data;

	/**
	 * 时间戳
	 */
	@Schema(name = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {

	}

	public Result<T> success(String message) {
		this.message = message;
		this.code = CommonConstants.SC_OK_200;
		this.success = true;
		return this;
	}

	@Deprecated
	public static Result<Object> ok() {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	@Deprecated
	public static Result<Object> ok(String msg) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setMessage(msg);
		return r;
	}

	@Deprecated
	public static Result<Object> ok(Object data) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> OK() {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	public static<T> Result<T> OK(T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setResult(data);
		return r;
	}


	public static<T> Result<T> OK_data(T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
//		r.setResult(data);
		r.setData(data);
		return r;
	}

	public static<T> Result<T> OK(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstants.SC_OK_200);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}

	public static Result<Object> error(String msg) {
		return error(CommonConstants.SC_INTERNAL_SERVER_ERROR_500, msg);
	}

	public static Result<Object> error(int code, String msg) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = CommonConstants.SC_INTERNAL_SERVER_ERROR_500;
		this.success = false;
		return this;
	}

	@JsonIgnore
	private String onlTable;

}
