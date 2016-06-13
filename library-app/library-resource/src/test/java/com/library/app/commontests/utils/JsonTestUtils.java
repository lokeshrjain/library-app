package com.library.app.commontests.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;

/**
 * Helper class to read and match json.
 * 
 * @author Lokesh
 * @version 0.1
 * 
 */
public class JsonTestUtils {

	public static String BASE_JSON_DIR = "json/";

	private JsonTestUtils() {
	}

	public static String readJsonFile(final String relativePath) {
		final InputStream is = JsonTestUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);

		try (Scanner s = new Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}

	}

	public static void assertJsonMatchesWithFileContent(final String JsonString, final String jsonFilePath) {
		assertJsonMatchesExpectedJson(JsonString, readJsonFile(jsonFilePath));
	}

	public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {

		try {
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch (final JSONException e) {
			throw new IllegalArgumentException(e);
		}

	}

	public static Long getIdFromJson(final String json) {
		final JsonObject jsonObject = JsonReader.readJosnAsObject(json);
		return JsonReader.getLongOrNull(jsonObject, "id");
	}

}
