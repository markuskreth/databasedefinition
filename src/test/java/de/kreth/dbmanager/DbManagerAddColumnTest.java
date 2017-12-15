package de.kreth.dbmanager;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DbManagerAddColumnTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAddOneColumn() {

		TableDefinition def = new TableDefinition("testtable", new ArrayList<ColumnDefinition>());

		String sql = DbManager.createSqlAddColumns(def, new ColumnDefinition(DataType.DATETIME, "deleted", " DEFAULT null"));
		assertEquals("ALTER TABLE testtable\n\tADD COLUMN deleted DATETIME  DEFAULT null;", sql);
	}

	@Test
	public void testAddTwoColumns() {

		TableDefinition def = new TableDefinition("testtable", new ArrayList<ColumnDefinition>());

		List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		columns.add(new ColumnDefinition(DataType.DATETIME, "deleted", " DEFAULT null"));
		columns.add(new ColumnDefinition(DataType.VARCHAR25, "theType"));
		String sql = DbManager.createSqlAddColumns(def, columns.toArray(new ColumnDefinition[]{}));
		assertEquals(
				"ALTER TABLE testtable\n\tADD COLUMN deleted DATETIME  DEFAULT null,\n\tADD COLUMN theType VARCHAR(25);",
				sql);
	}

}
