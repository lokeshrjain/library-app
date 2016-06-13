package com.library.app.common.model;

public enum HttpCodes {
	CREATED(201), VALIDATION_ERROR(422), OK(200), NOT_FOUND(404);

	private int code;

	public int getCode() {
		return code;
	}

	private HttpCodes(final int code) {
		this.code = code;
	}

}
