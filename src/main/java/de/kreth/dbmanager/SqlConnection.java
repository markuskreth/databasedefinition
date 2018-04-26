package de.kreth.dbmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlConnection implements Database {

	private final Connection con;
	private final Logger logger;
	private TableDefinition version;
	
	public SqlConnection(Connection connection, DatabaseType dbType) {
		super();
		this.con = connection;
		this.logger = LoggerFactory.getLogger(getClass());

		ColumnDefinition colVersion = new ColumnDefinition(DataType.INTEGER,
				"version", "NOT NULL");
		ArrayList<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		columns.add(colVersion);
		this.version = new TableDefinition("version", dbType, columns);
	}

	private void tryConnectVersionTable() {	
		try {
			Statement stm = con.createStatement();
			String sql = DbManager.createSqlStatement(version);
			stm.execute(sql);
		} catch (SQLException e) {
			logger.warn("Error creating version table", e);
		}
	}

	@Override
	public void beginTransaction() throws SQLException {
		con.setAutoCommit(false);
	}

	@Override
	public void setTransactionSuccessful() throws SQLException {
		if (!con.getAutoCommit())
			con.commit();
		con.setAutoCommit(true);
	}

	@Override
	public void endTransaction() throws SQLException {
		if (!con.getAutoCommit())
			con.rollback();
		con.setAutoCommit(true);
	}

	@Override
	public void execSQL(String sql) throws SQLException {
		con.createStatement().execute(sql);
	}

	@Override
	public void execSQL(String sql, Object[] bindArgs) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getVersion() {
		int version = 0;
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			rs = stm.executeQuery("SELECT version FROM version");
			if (rs.next())
				version = rs.getInt("version");

		} catch (SQLException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error on Database fetch version, version 0?", e);
			}
			tryConnectVersionTable();
		} finally {
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					if (logger.isDebugEnabled()) {
						logger.debug("Error on Database close", e);
					}
				}
		}

		return version;
	}

	@Override
	public long insert(String table, List<DbValue> values) throws SQLException {
		return 0;
	}

	@Override
	public boolean needUpgrade(int newVersion) {
		return false;
	}

	@Override
	public Iterator<Collection<DbValue>> query(String table, String[] columns)
			throws SQLException {
		return null;
	}

	@Override
	public void setVersion(int version) {
		Statement stm = null;
		boolean autoCommit = true;
		try {
			autoCommit = con.getAutoCommit();
			con.setAutoCommit(false);
			stm = con.createStatement();
			int rows = stm
					.executeUpdate("UPDATE version SET version = " + version);
			if (rows == 1)
				con.commit();
			else
				con.rollback();
		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("Error on database rollback after exception "
						+ e.getMessage(), e1);
			}
			logger.error("Error updating verions", e);
		} finally {
			try {
				con.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				logger.info("Error restoring autocommit to " + autoCommit, e);
			}
		}

	}

	@Override
	public int delete(String table, String whereClause, String[] whereArgs)
			throws SQLException {
		return 0;
	}

}
