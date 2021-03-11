package utils;

public enum MySqlType {

	/* --------------数字-------------- */
	BIGINT("Long"), // BIGINT
	DECIMAL("Double"), // DECIMAL
	DOUBLE("Double"), // DOUBLE
	FLOAT("Float"), // FLOAT
	MediumInt("Integer"), // MediumInt
	INT("Integer"), // INT
	SMALLINT("Integer"), // SMALLINT
	TINYINT("Integer"), // TINYINT

	/* --------------字符串-------------- */
	CHAR("String"), // CHAR
	VARCHAR("String"), // VARCHAR
	TINYBLOB("String"), // TINYBLOB
	BLOB("String"), // BLOB
	LONGBLOB("String"), // LONGBLOB
	TINYTEXT("String"), // TINYTEXT
	TEXT("String"), // TEXT
	MEDIUMTEXT("String"), // MEDIUMTEXT
	LONGTEXT("String"), // LONGTEXT
	MEDIUMBLOB("String"), // MEDIUMBLOB

	/* --------------时间日期-------------- */
	DATE("Date"), // DATE
	TIME("Date"), // DATE
	DATETIME("Date"), // DATETIME
	TIMESTAMP("Date");// TIMESTAMP

	private String javaType;

	private MySqlType() {
	}

	private MySqlType(String javaType) {
		this.javaType = javaType;
	}

	public String getJavaType() {
		return javaType;
	}
}
