package com.library.app.category.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.library.app.category.model.Category;

@Stateless
public class CategoryRepository {

	@PersistenceContext
	private EntityManager em;

	public void setEm(final EntityManager em) {
		this.em = em;
	}

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findById(final Long id) {
		if (id == null) {
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(final Category category) {
		em.merge(category);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(final String orderFieldName) {
		return em.createQuery("Select e from Category e order by " + orderFieldName).getResultList();
	}

	public boolean alreadyExists(final Category category) {
		final StringBuilder jpql = new StringBuilder();

		jpql.append("select 1 from Category where name= :name");
		if (category.getId() != null) {
			jpql.append(" And id != :id");
		}

		final Query jpqlQuery = em.createQuery(jpql.toString());
		jpqlQuery.setParameter("name", category.getName());
		if (category.getId() != null) {
			jpqlQuery.setParameter("id", category.getId());
		}

		return jpqlQuery.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(final Long id) {
		final String jpql = "select 1 from Category where id= :id";
		final Query jpqlQuery = em.createQuery(jpql);
		jpqlQuery.setParameter("id", id);

		return jpqlQuery.setMaxResults(1).getResultList().size() > 0;
	}

}
