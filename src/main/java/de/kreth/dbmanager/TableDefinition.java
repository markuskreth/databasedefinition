package de.kreth.dbmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Immutable <br>
 * Der Liste von Spalten wird <i>immer</i> eine Spalte mit Namen
 * {@link #COLUMN_ID_NAME} hinzugefügt, die als Primärschlüssel dient.
 * 
 * @author markus
 *
 */
public class TableDefinition {
	public static final String COLUMN_ID_NAME = "id";
	private final String tableName;
	private final Collection<ColumnDefinition> columns;

	public TableDefinition(String tableName, DatabaseType type,
			List<ColumnDefinition> columns) {
		this.tableName = tableName;
		ColumnDefinition id = new ColumnDefinition(DataType.INTEGER,
				COLUMN_ID_NAME, type.autoIncrementIdType);
		List<ColumnDefinition> def = new ArrayList<ColumnDefinition>();
		def.add(id);
		def.addAll(columns);
		this.columns = Collections.unmodifiableCollection(def);
	}

	public String getTableName() {
		return tableName;
	}

	public Collection<ColumnDefinition> getColumns() {
		return columns;
	}

	@Override
	public String toString() {
		return tableName + "[" + String.join(",", columns.stream()
				.map(m -> m.toString()).collect(Collectors.toList())) + "]";
	}
}
