package com.l_h.cd.pub.db.mybatis.dialect;

public class MySqlDialect extends Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	public String getLimitString(String sql, boolean hasOffset) {
		return new StringBuffer().append(trim(sql)).append(hasOffset ? " limit ?,?" : " limit ?").append(SQL_END_DELIMITER).toString();
	}

	public String getLimitString(String sql, int offset, int limit) {
		StringBuffer sbf = new StringBuffer();
		sql = trim(sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " "));
		sbf.append(trim(sql));
		if (offset > 0) {
			sbf.append(" limit ").append(offset).append(',').append(limit).append(SQL_END_DELIMITER);
		}
		else {
			sbf.append(" limit ").append(limit).append(SQL_END_DELIMITER);
		}
		return sbf.toString();
	}

	public boolean supportsLimit() {
		return true;
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}
}
