package com.library.app.common.model;

import com.library.app.common.exception.FieldNotValidException;

public final class StandardOperationResult {

	private StandardOperationResult() {
	}

	public static OperationResult getOperationResultExistent(final ResourceMessage resourceMessage,
			final String fieldName) {
		return OperationResult.error(resourceMessage.getKeyOfResourceExistent(),
				resourceMessage.getMessageOfResourceExistent(fieldName));
	}

	public static OperationResult getOperationResultInvalidField(final ResourceMessage resourceMessage,
			final FieldNotValidException ex) {
		return OperationResult.error(resourceMessage.getKeyOfInvalidField(ex.getFieldName()),
				ex.getMessage());
	}

	public static OperationResult getOperationResultNotFound(final ResourceMessage resourceMessage) {
		return OperationResult.error(resourceMessage.getKeyOfResourceNotFound(),
				resourceMessage.getMessageOfResourceNotFound());
	}
}
