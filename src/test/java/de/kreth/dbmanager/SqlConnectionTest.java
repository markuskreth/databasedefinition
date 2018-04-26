package de.kreth.dbmanager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

public class SqlConnectionTest {

	private Connection connection;
	private Statement statement;
	private ResultSet rs;
	
	@Before
	public void initMocks() throws SQLException {
		connection = mock(Connection.class);
		statement = mock(Statement.class);
		rs = mock(ResultSet.class);
		when(connection.createStatement()).thenReturn(statement);
	}

	@Test
	public void testTransactionSuccessfull() throws SQLException {
		SqlConnection con = new SqlConnection(connection, DatabaseType.HSQLDB);
		con.beginTransaction();
		verify(connection, times(1)).setAutoCommit(false);
		con.setTransactionSuccessful();
		verify(connection, times(1)).commit();
		verify(connection, times(1)).setAutoCommit(true);
	}

	@Test
	public void testTransactionRollback() throws SQLException {
		SqlConnection con = new SqlConnection(connection, DatabaseType.HSQLDB);
		con.beginTransaction();
		verify(connection, times(1)).setAutoCommit(false);
		con.endTransaction();
		verify(connection, times(1)).rollback();
		verify(connection, times(1)).setAutoCommit(true);
	}

	@Test
	public void testGetVersion() throws SQLException {
		when(rs.next()).thenReturn(false);
		when(statement.executeQuery(anyString())).thenReturn(rs);
		SqlConnection con = new SqlConnection(connection, DatabaseType.HSQLDB);
		int version = con.getVersion();
		assertEquals(0, version);
	}

	@Test
	public void testGetVersionWithoutTable() throws SQLException {
		when(statement.executeQuery(anyString())).thenThrow(SQLException.class);

		SqlConnection con = new SqlConnection(connection, DatabaseType.HSQLDB);
		int version = con.getVersion();
		assertEquals(0, version);
	}
	
	@Test
	public void testSetVersionSuccessfull() throws SQLException {
		SqlConnection con = new SqlConnection(connection, DatabaseType.HSQLDB);
		when(statement.executeUpdate(anyString())).thenReturn(1);
		con.setVersion(2);
		verify(connection, times(1)).commit();
	}
}
