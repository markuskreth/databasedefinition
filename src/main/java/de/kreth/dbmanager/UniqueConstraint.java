package de.kreth.dbmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueConstraint {
	private List<String> names = new ArrayList<>();
	
	public UniqueConstraint(ColumnDefinition... defs) {
		for(ColumnDefinition c: defs) {
			names.add(c.getColumnName());
		}
	}
	
	public List<String> getNames() {
		return Collections.unmodifiableList(names);
	}
}
