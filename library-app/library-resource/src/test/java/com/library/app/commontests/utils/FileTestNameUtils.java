package com.library.app.commontests.utils;

import org.junit.Ignore;

@Ignore
public class FileTestNameUtils {
	public static final String PATH_REQUEST = "/request/";
	public static final String PATH_RESPONSE = "/response/";

	private FileTestNameUtils() {
	}

	public static String getPathFileRequest(final String mainFolder, final String fileName) {
		return mainFolder + PATH_REQUEST + fileName;
	}

	public static String getPathFileResponse(final String mainFolder, final String fileName) {
		return mainFolder + PATH_RESPONSE + fileName;
	}
}
