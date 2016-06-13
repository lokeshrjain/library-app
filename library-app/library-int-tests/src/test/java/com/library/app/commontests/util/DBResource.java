package com.library.app.commontests.util;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

import com.library.app.commontest.util.TestPersistenceEJB;

@Path("/DB")
public class DBResource {

	@Inject
	private TestPersistenceEJB testPesistenceEJB;

	@DELETE
	public void deleteAll() {

		testPesistenceEJB.deleteAll();
	}
}
