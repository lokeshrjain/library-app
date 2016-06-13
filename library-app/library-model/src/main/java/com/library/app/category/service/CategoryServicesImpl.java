package com.library.app.category.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.common.exception.FieldNotValidException;

@Stateless
public class CategoryServicesImpl implements CategoryServices {

	@Inject
	private Validator validator;

	@Inject
	private CategoryRepository categoryRepository;

	@Override
	public Category add(final Category category) {
		validate(category);
		// add and return category
		return categoryRepository.add(category);
	}

	@Override
	public void update(final Category category) {
		validate(category);

		if (!categoryRepository.existsById(category.getId())) {
			throw new CategoryNotFoundException();
		}

		categoryRepository.update(category);

	}

	@Override
	public Category findById(final long id) {

		final Category category = categoryRepository.findById(id);

		if (category == null) {
			throw new CategoryNotFoundException();
		}
		return category;
	}

	@Override
	public List<Category> findAll() {
		// default order field for category
		return categoryRepository.findAll("name");
	}

	private void validate(final Category category) {
		validateFields(category);

		// check for existent before add
		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}
	}

	private void validateFields(final Category category) {
		// validate category before add
		final Set<ConstraintViolation<Category>> errors = validator.validate(category);
		final Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
		if (itErrors.hasNext()) {
			final ConstraintViolation<Category> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

	public CategoryRepository getCategoryRepository() {
		return categoryRepository;
	}

	public void setCategoryRepository(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(final Validator validator) {
		this.validator = validator;
	}

}
