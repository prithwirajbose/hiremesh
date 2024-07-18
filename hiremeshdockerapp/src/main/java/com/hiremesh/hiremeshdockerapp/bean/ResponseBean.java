package com.hiremesh.hiremeshdockerapp.bean;

public class ResponseBean {
	private boolean success = true;
	private Object result;
	private String error;

	public ResponseBean(Exception exception) {
		this.success = false;
		this.error = exception.getMessage();
	}
	public ResponseBean(Object result) {
		this.success = true;
		this.result = result;
	}

	public ResponseBean(boolean success, String errorMessage) {
		this.success = false;
		this.error = errorMessage;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
