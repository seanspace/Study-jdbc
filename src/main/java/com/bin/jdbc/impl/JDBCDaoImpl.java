package com.bin.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.bin.jdbc.IDAO;

/**
 * 使用QueryRunner提供其具体实现;
 *
 * @param <T>
 */
public  class JDBCDaoImpl<T> implements IDAO<T> {
	private QueryRunner queryRunner = null ;
	private Class<T> type ;
	public JDBCDaoImpl() {
		queryRunner = new QueryRunner() ;
//		type = ReflectionUtil.getSuperGenericType(getClass()) ;// 这个在反射学习中用到的.
	}

	@Override
	public void update(Connection connection, String sql, Object... args) {
		return ;
	}

	@Override
	public T get(Connection connection, String sql, Object... args) throws SQLException {
		return queryRunner.query(connection, sql,new BeanHandler<T>(type),args);
	}

	@Override
	public List<T> getForList(Connection connection, String sql, Object... args) {
		return null;
	}

	@Override
	public <E> E getForValue(Connection connection, String sql, Object... args) {
		return null;
	}

	@Override
	public void batch(Connection connection, String sql, Object[]... args) {
		
	}

}
