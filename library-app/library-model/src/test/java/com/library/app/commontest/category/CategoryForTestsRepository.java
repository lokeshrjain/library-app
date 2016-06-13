package com.library.app.commontest.category;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.category.model.Category;

/**
 * Helper class to produce models for test
 * 
 * @author Lokesh
 * 
 */
@Ignore
public class CategoryForTestsRepository {

	public static Category java() {
		return new Category("java");
	}

	public static Category network() {
		return new Category("network");
	}

	public static Category cleanCode() {
		return new Category("cleancode");
	}

	public static Category CategoryWithId(final Category category, final Long id) {
		category.setId(id);
		return category;
	}

	public static List<Category> listOfCategories() {
		return Arrays.asList(java(), network(), cleanCode());
	}

}
