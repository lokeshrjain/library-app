package com.library.app.category.service;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.common.exception.FieldNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServicesUTest {
	private CategoryServices categoryServices;

	@Mock
	private CategoryRepository categoryRepository;

	@Before
	public void initTestCase() {
		categoryServices = new CategoryServicesImpl();
		((CategoryServicesImpl) categoryServices)
				.setValidator(Validation.buildDefaultValidatorFactory().getValidator());
		((CategoryServicesImpl) categoryServices).setCategoryRepository(categoryRepository);
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("A");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("A category with long name is not allowed max 25 cahrs only please");
	}

	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true);
		categoryServices.add(java());
	}

	@Test
	public void addValidCategory() {
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(CategoryWithId(java(), 1L));
		final Category categoryAdded = categoryServices.add(java());
		assertThat(categoryAdded.getId(), is(equalTo(1L)));
	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("A");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("This is a long name where you have more than 25 chars");
	}

	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistent() {
		when(categoryRepository.alreadyExists(CategoryWithId(java(), 1L))).thenReturn(true);
		categoryServices.update(CategoryWithId(java(), 1L));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotExist() {
		when(categoryRepository.alreadyExists(CategoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(false);

		categoryServices.update(CategoryWithId(java(), 1L));
	}

	@Test
	public void updateCategoryWithValidName() {
		when(categoryRepository.alreadyExists(CategoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(true);

		categoryServices.update(CategoryWithId(java(), 1L));
		verify(categoryRepository).update(CategoryWithId(java(), 1L));
	}

	@Test
	public void findCategoryById() {
		when(categoryRepository.findById(1L)).thenReturn(CategoryWithId(java(), 1L));

		final Category category = categoryServices.findById(1L);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));

	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findById(1L)).thenReturn(null);
		final Category category = categoryServices.findById(1L);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findAllNoCatgeory() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());
		final List<Category> categories = categoryServices.findAll();
		assertThat(categories.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name")).thenReturn(Arrays.asList(java(), network(), cleanCode()));
		final List<Category> categories = categoryServices.findAll();

		assertThat(categories.isEmpty(), is(equalTo(false)));

	}

	private void addCategoryWithInvalidName(final String name) {
		try {
			categoryServices.add(new Category(name));
			fail("An excetion was expected to be thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	private void updateCategoryWithInvalidName(final String name) {
		try {
			categoryServices.update(new Category(name));
			fail("An exception was expected hence failed");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));

		}
	}
}
