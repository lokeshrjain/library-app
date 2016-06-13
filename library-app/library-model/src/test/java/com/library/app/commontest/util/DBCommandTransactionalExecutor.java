package com.library.app.commontest.util;

import javax.persistence.EntityManager;

public class DBCommandTransactionalExecutor {
	private EntityManager em;

	public DBCommandTransactionalExecutor(final EntityManager em) {
		this.em = em;
	}

	public <T> T executeCommand(final DBCommand<T> dBCommand) {
		try {
			em.getTransaction().begin();
			final T toReturn = dBCommand.execute();
			em.getTransaction().commit();
			em.clear();
			return toReturn;
		} catch (final Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new IllegalStateException(e);
		}

	}

}
