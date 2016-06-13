package com.library.app.category.repository;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontest.util.DBCommandTransactionalExecutor;

public class CategoryRepositoryUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;

	@Before
	public void InitTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		categoryRepository = new CategoryRepository();
		categoryRepository.setEm(em);

		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	/*
	 * dBCommandTransactionalExecutor .executeCommand(new DBCommand<Long>() {
	 * public Long execute() { return categoryRepository.add(Java()).getId(); }
	 * });
	 */
	@Test
	public void addCategoryAndFindIt() {
		final Long categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		assertThat(categoryAddedId, is(notNullValue()));

		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));

	}

	@Test
	public void findCategoryByIdNoFound() {
		final Category category = categoryRepository.findById(999L);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByNullId() {
		final Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));

	}

	@Test
	public void updateCategory() {
		final Long categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		assertThat(categoryAddedId, is(notNullValue()));

		final Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);

		categoryAfterAdd.setName(network().getName());

		dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});

		final Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(network().getName())));
	}

	@Test
	public void findAllCategories() {

		dBCommandTransactionalExecutor.executeCommand(() -> {
			listOfCategories().forEach(categoryRepository::add);
			return null;
		});

		final List<Category> categories = categoryRepository.findAll("name");
		assertThat(categories.size(), is(equalTo(3)));
		assertThat(categories.get(0).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(network().getName())));
	}

	@Test
	public void alreadyExistsForAdd() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return null;
		});
		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(network()), is(equalTo(false)));
	}

	@Test
	public void alreadyExistsCategoryWithId() {
		final Category network = dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return categoryRepository.add(network());
		});

		assertThat(categoryRepository.alreadyExists(network), is(equalTo(false))); // as it is same object

		network.setName(java().getName());
		assertThat(categoryRepository.alreadyExists(network), is(equalTo(true)));
		network.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExists(network), is(equalTo(false)));
	}

	@Test
	public void existsById() {
		final Category network = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(network());
		});

		assertThat(categoryRepository.existsById(network.getId()), is(equalTo(true)));
		assertThat(categoryRepository.existsById(999L), is(equalTo(false)));

	}
}
