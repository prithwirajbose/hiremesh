package com.hiremesh.interviewbot.common;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseBean<T> {
	private String requestId;
	private boolean success = true;
	private Object error;
	private T result;
	private String message;

	private ResponseBean() {
		this.requestId = UUID.randomUUID().toString();
	}

	public ResponseBean(AppRequestContext ctx) {
		this.requestId = ctx.getRequestId();
	}

	public ResponseBean(T result, AppRequestContext ctx) {
		this.requestId = ctx.getRequestId();
		this.result = result;
	}

	public ResponseBean(boolean success, T resultOrError, AppRequestContext ctx) {
		this.requestId = ctx.getRequestId();
		this.success=success;
		if (success == true) {
			this.result = resultOrError;
		} else {
			this.error = resultOrError;
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
