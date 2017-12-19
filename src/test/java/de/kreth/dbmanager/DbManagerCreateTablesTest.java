package de.kreth.dbmanager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DbManagerCreateTablesTest {
    @Test
    public void testCreateSqlStatement() {
        List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
        TableDefinition        def     = new TableDefinition("testtable", columns);
        String                 sql     = DbManager.createSqlStatement(def);

        assertNotNull(sql);
        assertEquals("CREATE TABLE testtable (\n" + "	_id INTEGER primary key AUTO_INCREMENT\n" + ")", sql);
    }

    @Test
    public void testCreateTable2Columns() {
        List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

        columns.add(new ColumnDefinition(DataType.VARCHAR100, "name", " NOT NULL"));
        columns.add(new ColumnDefinition(DataType.DATETIME, "theDate"));

        TableDefinition def = new TableDefinition("testtable", columns);
        String          sql = DbManager.createSqlStatement(def);

        assertNotNull(sql);
        assertEquals("CREATE TABLE testtable (\n"
        		+ "	_id INTEGER primary key AUTO_INCREMENT,\n"
        		+ "	name VARCHAR(100)  NOT NULL,\n"
        		+ "	theDate DATETIME\n" + ")",
                     sql);
    }

    @Test
    public void testCreateTable2ColumnsUnique() {
        List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

        columns.add(new ColumnDefinition(DataType.VARCHAR100, "name", " NOT NULL"));
        columns.add(new ColumnDefinition(DataType.DATETIME, "theDate"));

        UniqueConstraint unique = new UniqueConstraint(columns.get(0), columns.get(1));
        TableDefinition def = new TableDefinition("testtable", columns, unique);
        String          sql = DbManager.createSqlStatement(def);

        assertNotNull(sql);

        String expected = "CREATE TABLE testtable (\n" 
        		+ "	_id INTEGER primary key AUTO_INCREMENT,\n"
        		+ "	name VARCHAR(100)  NOT NULL,\n" 
        		+ "	theDate DATETIME,\n"
        		+ "	CONSTRAINT UNIQUE (name,theDate)\n"
        		+ ")";

        assertEquals(expected, sql);
    }

    @Before
    public void setUp() throws Exception {}
}


//~ Formatted by Jindent --- http://www.jindent.com
