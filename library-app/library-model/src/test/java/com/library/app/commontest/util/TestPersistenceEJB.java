package com.library.app.commontest.util;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;

import com.library.app.category.model.Category;

@Ignore
@Stateless
public class TestPersistenceEJB {

	@PersistenceContext
	private EntityManager em;

	public static final List<Class<?>> ENTITIES_TO_REMOVE = Arrays.asList(Category.class);

	public void deleteAll() {
		ENTITIES_TO_REMOVE.forEach(e -> deleteAllForEntity(e));
	}

	private void deleteAllForEntity(final Class<?> e) {
		final List<Object> rows = em.createQuery("Select e from " + e.getSimpleName() + " e").getResultList();
		rows.forEach(obj -> em.remove(obj));
	}

}
