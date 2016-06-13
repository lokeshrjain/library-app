package com.library.app.category.resource;

import static com.library.app.common.model.StandardOperationResult.*;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.service.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.JsonWriter;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.HttpCodes;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.ResourceMessage;

/**
 * Category resource rest APIs
 * 
 * @author Lokesh
 * @version 0.1
 *
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final ResourceMessage RESOURCE_MAESSAGE = new ResourceMessage("category");

	@Inject
	private CategoryServices categoryServices;

	@Inject
	private CategoryJsonConverter categoryJsonConverter;

	@POST
	public Response add(final String body) {
		logger.debug("Add new category with body {}", body);
		Category category = categoryJsonConverter.convertFrom(body);

		OperationResult result = null;
		HttpCodes httpCode = HttpCodes.CREATED;
		try {
			category = categoryServices.add(category);
			result = OperationResult.success(JsonUtils.getJSonElementWithId(category.getId()));
		} catch (final FieldNotValidException ex) {
			logger.error("Category name may not be null");
			httpCode = HttpCodes.VALIDATION_ERROR;
			result = getOperationResultInvalidField(RESOURCE_MAESSAGE, ex);
		} catch (final CategoryExistentException ex) {
			final String msg = "The category already exists with given neme";
			logger.error(msg, ex);
			httpCode = HttpCodes.VALIDATION_ERROR;
			result = getOperationResultExistent(RESOURCE_MAESSAGE, "name");
		}

		logger.debug("Returning the operation result after adding category: {}", result);
		return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJosn(result)).build();
	}

	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") final Long id, final String body) {
		logger.debug("update category with body {}", body);

		final Category category = categoryJsonConverter.convertFrom(body);
		category.setId(id);
		OperationResult result = null;
		HttpCodes httpCode = HttpCodes.OK;
		try {
			categoryServices.update(category);
			result = OperationResult.success();
		} catch (final FieldNotValidException ex) {
			logger.error("Category name may not be null");
			httpCode = HttpCodes.VALIDATION_ERROR;
			result = getOperationResultInvalidField(RESOURCE_MAESSAGE, ex);
		} catch (final CategoryExistentException ex) {
			logger.error("The category already exists with given neme", ex);
			httpCode = HttpCodes.VALIDATION_ERROR;
			result = getOperationResultExistent(RESOURCE_MAESSAGE, "name");
		} catch (final CategoryNotFoundException ex) {
			logger.error("Category with given id not found", ex);
			httpCode = HttpCodes.NOT_FOUND;
			result = getOperationResultNotFound(RESOURCE_MAESSAGE);
		}

		logger.debug("Returning the operation result after udating category: {}", result);
		return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJosn(result)).build();
	}

	@GET
	@Path("/{id}")
	public Response findCategoryById(@PathParam("id") final Long id) {
		logger.debug("Finding Category by id");
		ResponseBuilder responseBuilder;

		try {
			final Category category = categoryServices.findById(id);
			final OperationResult result = OperationResult
					.success(categoryJsonConverter.convertToJsonElement(category));
			logger.debug("Returning the operation result after find category {}", category);
			responseBuilder = Response.status(HttpCodes.OK.getCode()).entity(OperationResultJsonWriter.toJosn(result));
		} catch (final CategoryNotFoundException ex) {
			logger.error("Category not found for given id", id);
			responseBuilder = Response.status(HttpCodes.NOT_FOUND.getCode());
		}

		return responseBuilder.build();
	}

	@GET
	public Response findAllCategories() {
		logger.debug("Finding all categories");
		final ResponseBuilder responseBuilder;
		if (categoryServices == null) {
			System.out.println("categoryServices is NULL");
		}
		final List<Category> categories = categoryServices.findAll();

		logger.debug("Returning the operation result after finding {} categories", categories.size());
		final JsonElement jsonWithPagingAndEnteris = getJsonElementWithPagingAndEnteries(categories);

		return Response.status(HttpCodes.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEnteris))
				.build();
	}

	private JsonElement getJsonElementWithPagingAndEnteries(final List<Category> categories) {
		final JsonObject jsonWithPagingAndEnteries = new JsonObject();

		final JsonObject jsonPaging = new JsonObject();
		jsonPaging.addProperty("totalRecords", categories.size());

		jsonWithPagingAndEnteries.add("paging", jsonPaging);
		jsonWithPagingAndEnteries.add("entries", categoryJsonConverter.convertToJsonElement(categories));
		return jsonWithPagingAndEnteries;
	}

	public void setCategoryServices(final CategoryServices categoryServices) {
		this.categoryServices = categoryServices;
	}

	public void setCategoryJsonConverter(final CategoryJsonConverter categoryJsonConverter) {
		this.categoryJsonConverter = categoryJsonConverter;
	}

}
