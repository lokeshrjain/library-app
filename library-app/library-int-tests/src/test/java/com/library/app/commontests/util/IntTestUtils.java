package com.library.app.commontests.util;

import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Ignore;

import com.library.app.common.model.HttpCodes;
import com.library.app.commontests.utils.JsonTestUtils;

@Ignore
public class IntTestUtils {

	public static Long addElementWithFileAndGetId(final ResourceClient resourceClient, final String pathResource,
			final String mainFolder, final String fileName) {
		final Response response = resourceClient.resourcePath(pathResource).postWithFile(
				getPathFileRequest(mainFolder, fileName));
		return assertResponseIsCreatedAndGetId(response);
	}

	public static String findById(final ResourceClient resourceClient, final String pathResource, final Long id) {
		final Response response = resourceClient.resourcePath(pathResource + "/" + id).get();
		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		return response.readEntity(String.class);
	}

	private static Long assertResponseIsCreatedAndGetId(final Response response) {
		assertThat(response.getStatus(), is(equalTo(HttpCodes.CREATED.getCode())));
		final Long id = JsonTestUtils.getIdFromJson(response.readEntity(String.class));
		assertThat(id, is(notNullValue()));
		return id;
	}
}
