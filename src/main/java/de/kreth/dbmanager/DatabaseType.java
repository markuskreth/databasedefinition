package de.kreth.dbmanager;

public enum DatabaseType {

	MYSQL("primary key AUTO_INCREMENT"), HSQLDB("IDENTITY");

	public final String autoIncrementIdType;

	private DatabaseType(String autoIncrementIdType) {
		this.autoIncrementIdType = autoIncrementIdType;
	}

}
