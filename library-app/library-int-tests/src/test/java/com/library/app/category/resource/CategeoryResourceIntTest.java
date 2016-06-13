package com.library.app.category.resource;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static com.library.app.commontests.util.IntTestUtils.*;
import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static com.library.app.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCodes;
import com.library.app.commontests.util.ResourceClient;
import com.library.app.commontests.utils.ResourceDefinitions;

@RunWith(Arquillian.class)
public class CategeoryResourceIntTest {
	@ArquillianResource
	private URL url;

	private ResourceClient resourceClient;

	private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

	@Deployment
	public static WebArchive CreateDeployment() {
		return ShrinkWrap.create(WebArchive.class).addPackages(true, "com.library.app")
				.addAsResource("persistence-integration.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "bean.xml")
				.setWebXML(new File("src/test/resources/web.xml"))
				.addAsLibraries(
						Maven.resolver().resolve("com.google.code.gson:gson:2.3.1", "org.mockito:mockito-core:1.9.5")
								.withTransitivity().asFile());

	}

	@Before
	public void intiTestCases() {
		this.resourceClient = new ResourceClient(url);
		resourceClient.resourcePath("/DB").delete();
	}

	@Test
	@RunAsClient
	public void addValidCategoryAndFindIt() {
		final Long id = addCategoryAndGetId("category.json");
		findCategoryByIdAndAssertWithCategory(id, java());
	}

	@Test
	@RunAsClient
	public void addNullCategoryAndExpectError() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE)
				.postWithFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json"));
		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryNullNameResponse.json");

	}

	@Test
	@RunAsClient
	public void addExistentCategoryAndExpectError() {
		resourceClient.resourcePath(PATH_RESOURCE)
				.postWithFile(getPathFileRequest(PATH_RESOURCE, "category.json"));

		final Response response = resourceClient.resourcePath(PATH_RESOURCE)
				.postWithFile(getPathFileRequest(PATH_RESOURCE, "category.json"));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));

		assertJsonResponseWithFile(response, "CategoryExistentResponse.json");

	}

	@Test
	@RunAsClient
	public void updateValidCategory() {
		final Long javaCategoryid = addCategoryAndGetId("category.json");
		findCategoryByIdAndAssertWithCategory(javaCategoryid, java());

		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + javaCategoryid)
				.putWithFile(getPathFileRequest(PATH_RESOURCE, "categoryNetwork.json"));
		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		findCategoryByIdAndAssertWithCategory(javaCategoryid, network());

	}

	@Test
	@RunAsClient
	public void updateAlreadyExistCategory() {
		final Long id = addCategoryAndGetId("category.json");
		addCategoryAndGetId("categoryNetwork.json"); // added another category
		// findCategoryByIdAndAssertWithCategory(id, java());

		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + id)
				.putWithFile(getPathFileRequest(PATH_RESOURCE, "categoryNetwork.json")); // try to update already added
																							// category
		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryExistentResponse.json");
	}

	@Test
	@RunAsClient
	public void updateCateogryNotFound() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/111")
				.putWithFile(getPathFileRequest(PATH_RESOURCE, "categoryNetwork.json")); // try to update already added
		assertThat(response.getStatus(), is(equalTo(HttpCodes.NOT_FOUND.getCode())));
	}

	@Test
	@RunAsClient
	public void findCategoryNotFound() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/111").get();
		assertThat(response.getStatus(), is(equalTo(HttpCodes.NOT_FOUND.getCode())));
	}

	@Test
	@RunAsClient
	public void findAllCategories() {
		resourceClient.resourcePath("DB/" + PATH_RESOURCE).postWithContent("");

		final Response response = resourceClient.resourcePath(PATH_RESOURCE).get();
		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));

		assertResponseContainsTheCategories(response, 3, cleanCode(), java(), network());

	}

	private void assertResponseContainsTheCategories(final Response response, final int expectedCategorySize,
			final Category... expectedCategories) {
		final JsonObject result = JsonReader.readJosnAsObject(response.readEntity(String.class));

		final int totalRecords = result.getAsJsonObject("paging").get("totalRecords").getAsInt();
		assertThat(totalRecords, is(equalTo(expectedCategorySize)));

		final JsonArray categoryList = result.getAsJsonArray("entries");
		assertThat(categoryList.size(), is(equalTo(expectedCategories.length)));

		for (int i = 0; i < expectedCategories.length; i++) {
			final Category expectedCategory = expectedCategories[i];
			assertThat(categoryList.get(i).getAsJsonObject().get("name").getAsString(),
					is(equalTo(expectedCategory.getName())));
		}

	}

	private void findCategoryByIdAndAssertWithCategory(final Long id, final Category category) {
		final String responseGet = findById(resourceClient, PATH_RESOURCE, id);
		final JsonObject categoryAsJson = JsonReader.readJosnAsObject(responseGet);
		assertThat(JsonReader.getStringOrNull(categoryAsJson, "name"), is(equalTo(category.getName())));
	}

	private Long addCategoryAndGetId(final String fileName) {
		return addElementWithFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, fileName);
	}

	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesWithFileContent(response.readEntity(String.class),
				getPathFileResponse(PATH_RESOURCE, fileName));
	}
}
