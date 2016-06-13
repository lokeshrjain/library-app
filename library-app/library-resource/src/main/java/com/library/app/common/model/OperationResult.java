package com.library.app.common.model;

/**
 * Common result entity
 * 
 * @author Lokesh
 *
 */
public class OperationResult {

	public boolean success;

	public String errorCode;

	public String errorDescription;
	public Object entity;

	/**
	 * @param entity
	 */
	public OperationResult(final Object entity) {
		this.success = true;
		this.entity = entity;
	}

	public OperationResult(final String errorCode, final String errorDescription) {
		this.success = false;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}

	public static OperationResult success(final Object entity) {
		return new OperationResult(entity);
	}

	public static OperationResult success() {
		return new OperationResult(null);
	}

	public static OperationResult error(final String errorCode, final String errorDescription) {
		return new OperationResult(errorCode, errorDescription);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public Object getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return "OperationResult [success=" + success + ", errorCode=" + errorCode + ", errorDescription="
				+ errorDescription + ", entity=" + entity + "]";
	}
}
