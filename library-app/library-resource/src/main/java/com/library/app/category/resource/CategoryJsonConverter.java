package com.library.app.category.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;

/**
 * This is a helper class to convert json to object
 * 
 * @author Lokesh
 * @version 0.1
 *
 */

@ApplicationScoped
public class CategoryJsonConverter {
	Logger logger = LoggerFactory.getLogger(getClass());

	public Category convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readJosnAsObject(json);
		final Category category = new Category();
		category.setName(JsonReader.getStringOrNull(jsonObject, "name"));
		return category;
	}

	public JsonElement convertToJsonElement(final Category category) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", category.getId());
		jsonObject.addProperty("name", category.getName());
		return jsonObject;
	}

	public JsonArray convertToJsonElement(final List<Category> categories) {
		final JsonArray jsonArray = new JsonArray();
		for (final Category category : categories) {
			jsonArray.add(convertToJsonElement(category));
		}
		return jsonArray;
	}

}
