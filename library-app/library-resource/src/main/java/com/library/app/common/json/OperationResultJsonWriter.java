package com.library.app.common.json;

import com.google.gson.JsonObject;
import com.library.app.common.model.OperationResult;

/**
 * Helper class to write/convert operation result into json
 * 
 * @author Lokesh
 *
 */
public final class OperationResultJsonWriter {

	private OperationResultJsonWriter() {
	}

	public static String toJosn(final OperationResult result) {
		return JsonWriter.writeToString(getObject(result));
	}

	private static Object getObject(final OperationResult result) {
		if (result.isSuccess()) {
			return getJsonSuccess(result);
		}
		return getJsonError(result);
	}

	private static Object getJsonSuccess(final OperationResult result) {
		return result.getEntity();
	}

	private static JsonObject getJsonError(final OperationResult result) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("errorCode", result.getErrorCode());
		jsonObject.addProperty("errorDescription", result.getErrorDescription());

		return jsonObject;
	}
}
