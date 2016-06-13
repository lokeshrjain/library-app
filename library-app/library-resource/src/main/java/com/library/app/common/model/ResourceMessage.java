package com.library.app.common.model;

public class ResourceMessage {
	private final String resource;

	private final static String KEY_EXISTENT = "%s.existent";
	private final static String KEY_NOT_FOUND = "%s.not.found";
	private final static String MESSAGE_EXISTENT = "There is already an %s for given %s";
	private final static String MESSAGE_NOT_FOUND = "%s not found";
	private final static String KEY_INVALID_FIELD = "%s.invalidField.%s";

	public ResourceMessage(final String resource) {
		this.resource = resource;
	}

	public String getKeyOfInvalidField(final String fieldName) {
		return String.format(KEY_INVALID_FIELD, resource, fieldName);
	}

	public String getKeyOfResourceExistent() {
		return String.format(KEY_EXISTENT, resource);
	}

	public String getMessageOfResourceExistent(final String fieldName) {
		return String.format(MESSAGE_EXISTENT, resource, fieldName);
	}

	public String getKeyOfResourceNotFound() {
		return String.format(KEY_NOT_FOUND, resource);
	}

	public String getMessageOfResourceNotFound() {
		return String.format(MESSAGE_NOT_FOUND, resource);
	}

}
