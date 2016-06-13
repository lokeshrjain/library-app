package com.library.app.category.resource;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static com.library.app.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.service.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.HttpCodes;
import com.library.app.commontests.utils.ResourceDefinitions;

@RunWith(MockitoJUnitRunner.class)
public class CategoryResourceUTest {

	public static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

	private CategoryResource categoryResource;

	@Mock
	private CategoryServices categoryServices;

	@Before
	public void initTestCase() {
		categoryResource = new CategoryResource();
		categoryResource.setCategoryServices(categoryServices);
		final CategoryJsonConverter categoryJsonConverter = new CategoryJsonConverter();
		categoryResource.setCategoryJsonConverter(categoryJsonConverter);

	}

	@Test
	public void addValidCategory() {
		when(categoryServices.add(java())).thenReturn(CategoryWithId(java(), 1L));

		final Response response = categoryResource
				.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "newCategory.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCodes.CREATED.getCode())));
		assertThat(response.getEntity().toString(), is(equalTo("{\"id\":1}")));

	}

	@Test
	public void addExistentCategory() {
		when(categoryServices.add(java())).thenThrow(new CategoryExistentException());
		final Response response = categoryResource
				.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "newCategory.json")));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryExistentResponse.json");
	}

	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesWithFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
	}

	@Test
	public void addCategoryWithNullName() {
		when(categoryServices.add(new Category()))
				.thenThrow(new FieldNotValidException("name", "may not be null"));
		final Response response = categoryResource
				.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryNullNameResponse.json");

	}

	@Test
	public void updateValidCategory() {
		final Response response = categoryResource
				.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		assertThat(response.getEntity().toString(), is(equalTo("")));

		verify(categoryServices).update(CategoryWithId(java(), 1L));
	}

	@Test
	public void updateExistentCategory() {
		doThrow(new CategoryExistentException()).when(categoryServices).update(CategoryWithId(java(), 1L));

		final Response response = categoryResource
				.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryExistentResponse.json");
	}

	@Test
	public void updateNotFoundCategory() {
		doThrow(new CategoryNotFoundException()).when(categoryServices).update(CategoryWithId(java(), 1L));

		final Response response = categoryResource
				.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.NOT_FOUND.getCode())));
		assertJsonResponseWithFile(response, "CategoryNotFoundResponse.json");
	}

	@Test
	public void updateCategoryWithNullName() {
		doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices)
				.update(CategoryWithId(new Category(), 1L));
		final Response response = categoryResource
				.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));

		assertThat(response.getStatus(), is(equalTo(HttpCodes.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "CategoryNullNameResponse.json");

	}

	@Test
	public void findCategory() {
		when(categoryServices.findById(1L)).thenReturn(CategoryWithId(java(), 1L));
		final Response response = categoryResource.findCategoryById(1L);

		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		assertJsonResponseWithFile(response, "CategoryFoundResponse.json");

	}

	@Test
	public void findCategoryNotFound() {
		doThrow(new CategoryNotFoundException()).when(categoryServices).findById(1L);
		final Response response = categoryResource.findCategoryById(1L);

		assertThat(response.getStatus(), is(equalTo(HttpCodes.NOT_FOUND.getCode())));

	}

	@Test
	public void findAllNoCategory() {
		when(categoryServices.findAll()).thenReturn(new ArrayList<Category>());
		final Response response = categoryResource.findAllCategories();

		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		assertJsonResponseWithFile(response, "emptyListofCategoryResponse.json");

	}

	@Test
	public void findAllTwoCategories() {
		when(categoryServices.findAll())
				.thenReturn(Arrays.asList(CategoryWithId(java(), 1L), CategoryWithId(network(), 2L)));
		final Response response = categoryResource.findAllCategories();

		assertThat(response.getStatus(), is(equalTo(HttpCodes.OK.getCode())));
		assertJsonResponseWithFile(response, "ListofCategoriesResponse.json");

	}
}
