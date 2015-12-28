package com.nhl.bootique.cayenne;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.Before;
import org.junit.Test;

import com.nhl.bootique.jdbc.DataSourceFactory;

public class ServerRuntimeFactoryTest {

	private DataSourceFactory mockDSFactory;

	@Before
	public void before() {
		this.mockDSFactory = mock(DataSourceFactory.class);
	}

	@Test
	public void testCreateCayenneRuntime_NoName() {
		ServerRuntimeFactory factory = new ServerRuntimeFactory();
		factory.setDatasource("ds1");

		ServerRuntime runtime = factory.createCayenneRuntime(mockDSFactory);
		try {
			DataDomain domain = runtime.getDataDomain();
			assertEquals("cayenne", domain.getName());

			assertEquals(1, domain.getDataNodes().size());
			assertNotNull(domain.getDefaultNode());
			assertEquals("cayenne", domain.getDefaultNode().getName());

		} finally {
			runtime.shutdown();
		}
	}

	@Test
	public void testCreateCayenneRuntime_Name() {
		ServerRuntimeFactory factory = new ServerRuntimeFactory();
		factory.setDatasource("ds1");
		factory.setName("me");

		ServerRuntime runtime = factory.createCayenneRuntime(mockDSFactory);
		try {

			DataDomain domain = runtime.getDataDomain();
			assertEquals("me", domain.getName());

			assertEquals(1, domain.getDataNodes().size());
			assertNotNull(domain.getDefaultNode());
			assertEquals("me", domain.getDefaultNode().getName());

		} finally {
			runtime.shutdown();
		}
	}

	@Test
	public void testCreateCayenneRuntime_Config() {
		ServerRuntimeFactory factory = new ServerRuntimeFactory();
		factory.setDatasource("ds1");
		factory.setConfig("cayenne-project.xml");

		ServerRuntime runtime = factory.createCayenneRuntime(mockDSFactory);
		try {

			DataDomain domain = runtime.getDataDomain();
			assertNotNull(domain.getEntityResolver().getDbEntity("db_entity"));

		} finally {
			runtime.shutdown();
		}
	}

	@Test
	public void testCreateCayenneRuntime_NoConfig() {
		ServerRuntimeFactory factory = new ServerRuntimeFactory();
		factory.setDatasource("ds1");

		// explicitly set config to null...
		factory.setConfig(null);

		ServerRuntime runtime = factory.createCayenneRuntime(mockDSFactory);
		try {

			DataDomain domain = runtime.getDataDomain();
			assertTrue(domain.getEntityResolver().getDbEntities().isEmpty());

		} finally {
			runtime.shutdown();
		}
	}
}