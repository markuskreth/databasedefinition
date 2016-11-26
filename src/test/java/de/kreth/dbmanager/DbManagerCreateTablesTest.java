package de.kreth.dbmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DbManagerCreateTablesTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateSqlStatement() {
		List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		TableDefinition def = new TableDefinition("testtable", columns);
		String sql = DbManager.createSqlStatement(def);
		assertNotNull(sql);
		System.out.println(sql);
		assertEquals("CREATE TABLE testtable (\n" + "	_id INTEGER primary key AUTO_INCREMENT\n" + ")", sql);
	}

}
