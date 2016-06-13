package com.library.app.commontests.category;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.library.app.category.model.Category;
import com.library.app.category.service.CategoryServices;

@Path("/DB/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResourceDB {

	@Inject
	private CategoryServices categoryServices;

	@GET
	public List<Category> findAll() {
		return categoryServices.findAll();
	}

	@POST
	public void addAll() {
		listOfCategories().forEach(categoryServices::add);
	}
}
