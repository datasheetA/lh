package com.l_h.cd.pub.db.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import com.l_h.cd.pub.db.mybatis.dialect.Dialect;
import com.l_h.cd.pub.db.mybatis.dialect.MySqlDialect;
import com.l_h.cd.pub.db.mybatis.dialect.OracleDialect;

@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	public static Logger logger = Logger.getLogger(PaginationInterceptor.class);
	private static String dialectTypeProperty = ""; // 数据库方言

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);
		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
			return invocation.proceed();
		}
		
		//Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		Dialect.Type databaseType = null;
		try {
			// databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
			databaseType = Dialect.Type.valueOf(dialectTypeProperty.toUpperCase());
		}
		catch (Exception e) {
			// ignore
		}

		if (databaseType == null) {
			throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + dialectTypeProperty);
		}

		Dialect dialect = null;
		switch (databaseType) {
			case MYSQL:
				dialect = new MySqlDialect();
				break;
			case ORACLE:
				dialect = new OracleDialect();
				break;
		}

		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		logger.debug("生成分页SQL : " + boundSql.getSql());

		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		dialectTypeProperty = properties.getProperty("dialect");
		if (dialectTypeProperty == null || dialectTypeProperty.equals("")) {
			try {
				throw new PropertyException("dialect property is not found!");
			}
			catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}
}
