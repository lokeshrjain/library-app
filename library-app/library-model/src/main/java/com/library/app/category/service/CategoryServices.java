package com.library.app.category.service;

import java.util.List;

import javax.ejb.Local;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.common.exception.FieldNotValidException;

@Local
public interface CategoryServices {

	Category add(Category category) throws FieldNotValidException, CategoryExistentException;

	void update(Category category) throws FieldNotValidException, CategoryNotFoundException;

	Category findById(long l) throws CategoryNotFoundException;

	List<Category> findAll();

}