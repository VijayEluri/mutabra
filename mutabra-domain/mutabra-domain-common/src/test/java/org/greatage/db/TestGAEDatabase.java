package org.greatage.db;

import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.greatage.db.gae.GAEDatabase;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class TestGAEDatabase extends Assert {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private DatastoreService dataStore;
	private Database database;

	@BeforeMethod
	public void setUp() {
		helper.setUp();
		dataStore = DatastoreServiceFactory.getDatastoreService();
		database = new GAEDatabase(dataStore);
	}

	@AfterMethod
	public void tearDown() {
		database = null;
		dataStore = null;
		helper.tearDown();
	}

	@Test
	public void gae_insert() {
		assertNotExist(new Query("company"));
		assertNotExist(new Query("department"));
		assertNotExist(new Query("employee"));

		final ChangeLog changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("1")
						.insert("company").set("name", "company1").end()
						.insert("company").set("name", "company2").end()
						.insert("company").set("name", "company3").end()
						.end();

				begin("2", "author")
						.insert("department").set("name", "department1").set("company", 10l).end()
						.end();

				begin("2", "author").comment("test employee")
						.insert("employee").set("name", "employee1").set("department", 10l).end()
						.end();
			}
		};

		database.update(changeLog);

		assertCount(new Query("company"), 3);
		assertExist(new Query("company")
				.addFilter("name", Query.FilterOperator.EQUAL, "company1"));
		assertExist(new Query("company")
				.addFilter("name", Query.FilterOperator.EQUAL, "company2"));
		assertExist(new Query("company")
				.addFilter("name", Query.FilterOperator.EQUAL, "company3"));

		assertCount(new Query("department"), 1);
		assertExist(new Query("department")
				.addFilter("name", Query.FilterOperator.EQUAL, "department1")
				.addFilter("company", Query.FilterOperator.EQUAL, 10l));

		assertCount(new Query("employee"), 1);
		assertExist(new Query("employee")
				.addFilter("name", Query.FilterOperator.EQUAL, "employee1")
				.addFilter("department", Query.FilterOperator.EQUAL, 10l));
	}

	@Test
	public void gae_update() {
		assertNotExist(new Query("company"));
		assertNotExist(new Query("department"));
		assertNotExist(new Query("employee"));

		Entity entity = new Entity("company");
		entity.setProperty("name", "company1");
		dataStore.put(entity);
		entity = new Entity("company");
		entity.setProperty("name", "company2");
		dataStore.put(entity);
		entity = new Entity("company");
		entity.setProperty("name", "company3");
		dataStore.put(entity);

		entity = new Entity("department");
		entity.setProperty("name", "department1");
		dataStore.put(entity);
		entity = new Entity("department");
		entity.setProperty("name", "department2");
		dataStore.put(entity);

		entity = new Entity("employee");
		entity.setProperty("name", "employee1");
		dataStore.put(entity);

		final ChangeLog changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("1")
						.update("company").set("name", "company").end()
						.end();

				begin("2", "author").comment("test employee")
						.update("employee").set("name", "employee111").where("department").equal(100l).end().end()
						.end();

				begin("3", "author")
						.update("department").set("name", "department11").where("name").equal("department1").end().end()
						.end();
			}
		};

		database.update(changeLog);

		assertCount(new Query("company"), 3);
		assertCount(new Query("company")
				.addFilter("name", Query.FilterOperator.EQUAL, "company"), 3);

		assertCount(new Query("department"), 2);
		assertExist(new Query("department")
				.addFilter("name", Query.FilterOperator.EQUAL, "department11"));
		assertNotExist(new Query("department")
				.addFilter("name", Query.FilterOperator.EQUAL, "department1"));

		assertCount(new Query("employee"), 1);
		assertNotExist(new Query("employee")
				.addFilter("name", Query.FilterOperator.EQUAL, "employee111"));
	}

	@Test
	public void gae_delete() {
		assertNotExist(new Query("company"));
		assertNotExist(new Query("department"));
		assertNotExist(new Query("employee"));

		Entity entity = new Entity("company");
		entity.setProperty("name", "company1");
		dataStore.put(entity);
		entity = new Entity("company");
		entity.setProperty("name", "company2");
		dataStore.put(entity);
		entity = new Entity("company");
		entity.setProperty("name", "company3");
		dataStore.put(entity);

		entity = new Entity("department");
		entity.setProperty("name", "department1");
		dataStore.put(entity);
		entity = new Entity("department");
		entity.setProperty("name", "department2");
		dataStore.put(entity);

		entity = new Entity("employee");
		entity.setProperty("name", "employee1");
		dataStore.put(entity);

		final ChangeLog changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("1")
						.delete("company").end()
						.end();

				begin("2", "author").comment("test employee")
						.delete("employee").where("department").equal(100l).end().end()
						.end();

				begin("3", "author")
						.delete("department").where("name").equal("department2").end().end()
						.end();

			}
		};

		database.update(changeLog);

		assertNotExist(new Query("company"));

		assertCount(new Query("department"), 1);
		assertExist(new Query("department")
				.addFilter("name", Query.FilterOperator.EQUAL, "department1"));
		assertNotExist(new Query("department")
				.addFilter("name", Query.FilterOperator.EQUAL, "department2"));

		assertCount(new Query("employee"), 1);
	}

	@Test
	public void gae_twice_update() {
		assertNotExist(new Query("company"));

		final ChangeLog changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("1")
						.insert("company").set("name", "company1").end()
						.insert("company").set("name", "company2").end()
						.insert("company").set("name", "company3").end()
						.end();

				begin("2")
						.delete("company").where("name").equal("company1").end().end()
						.end();
			}
		};

		database.update(changeLog);

		assertCount(new Query("company"), 2);
		assertNotExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company1"));
		assertExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company2"));
		assertExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company3"));

		database.update(changeLog);

		assertCount(new Query("company"), 2);
		assertNotExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company1"));
		assertExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company2"));
		assertExist(new Query("company").addFilter("name", Query.FilterOperator.EQUAL, "company3"));
	}

	@Test
	public void gae_not_ended_statement() {
		assertNotExist(new Query("company"));

		ChangeLog changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("1").insert("company").set("name", "company1");
				begin("2").insert("company").set("name", "company2");
			}
		};

		database.update(changeLog);
		assertCount(new Query("company"), 2);

		changeLog = new ChangeLog() {
			@Override
			protected void init() {
				begin("3").insert("company").set("name", "company3").end();
				begin("4").insert("company").set("name", "company4").end();
			}
		};

		database.update(changeLog);
		assertCount(new Query("company"), 4);
	}

	private void assertExist(final Query query) {
		assertCount(query, 1, "Requested entity doesn't exist");
	}

	private void assertNotExist(final Query query) {
		assertCount(query, 0, "Requested entity exists");
	}

	private void assertCount(final Query query, final int expected) {
		assertCount(query, expected, null);
	}

	private void assertCount(final Query query, final int expected, final String message) {
		final int count = dataStore.prepare(query).countEntities(FetchOptions.Builder.withDefaults());
		assertEquals(count, expected, message);
	}
}
