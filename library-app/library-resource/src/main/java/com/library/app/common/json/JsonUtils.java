package com.library.app.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JsonUtils {

	private JsonUtils() {
	}

	public static JsonElement getJSonElementWithId(final Long id) {
		final JsonObject jsonIdObject = new JsonObject();
		jsonIdObject.addProperty("id", id);
		return jsonIdObject;
	}
}
