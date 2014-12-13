package com.bin.jdbc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class DAO {
	
	/**
	 * 增删改;
	 */
	public void update(String sql,Object ... args) {
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		
		try {
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sql) ;
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, preparedStatement, connection); ;
		}
	}
	
	/**
	 * 查询单行数据;
	 */
	public <T> T get(Class<T> clazz,String sql , Object ... args) {
		T entity = null ;
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null ;
		try {
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sql) ;
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				Map<String, Object> values = new HashMap<String, Object>() ;
				ResultSetMetaData rsmd = resultSet.getMetaData() ;
				int columnCount = rsmd.getColumnCount() ;
				
				for (int i = 0; i < columnCount; i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1) ;
					String columnValue = (String) resultSet.getObject(i + 1) ;
					values.put(columnLabel, columnValue);
					
				}
				
				entity = clazz.newInstance() ;
				
				for (Map.Entry<String, Object> entry:values.entrySet()) {
					String propertyName = entry.getKey() ;
					Object value = entry.getValue() ;
					// 用反射,设置属性.
					BeanUtils.setProperty(entity, propertyName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTool.releaseDB(resultSet, preparedStatement, connection);
		}
		
		return entity ;
	}
	
	/**
	 * get()方法重构之后的.
	 */
	public <T> T get1(Class<T> clazz,String sql, Object ... args){
		List<T> result = getForList(clazz, sql, args) ;
		if (result.size() > 0) {
			return result.get(0) ;
		}
		return null ;
	}
	
	/**
	 * 查询结果集.
	 */
	public <T> List<T> getForList(Class<T> clazz, String sql,Object ... args) {
		List<T> list = null ;
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null ;
		try {
			// 得到结果集.
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sql) ;
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet = preparedStatement.executeQuery() ;
			// 处理结果集,得到Map对应的list,其中一个Map对象就是一条记录.
			List<Map<String, Object>> values = handleResultSetToMMapList(resultSet);
			// 把Map转换为T类型对应的集合.
			list = transferMapToBeanList(clazz, values);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(resultSet, preparedStatement, connection);
		}
		
		return list;
	}

	/**
	 * 把Map转换为T类型对应的集合.
	 */
	private <T> List<T> transferMapToBeanList(Class<T> clazz, List<Map<String, Object>> values) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> list = new ArrayList<>();
		T bean = null;
		if (list.size() > 0) {
			for (Map<String, Object> m:values) {
				bean = clazz.newInstance() ;
				for (Map.Entry<String, Object> entry:m.entrySet()) {
					String propertyName = entry.getKey() ;
					Object value = entry.getValue() ;
					BeanUtils.setProperty(bean, propertyName, value);
				}
				list.add(bean) ;
			}
		}
		return list ;
	}

	/**
	 * 处理结果集,得到Map的list,其中一个Map对象对应一条记录.
	 */
	private List<Map<String, Object>> handleResultSetToMMapList(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> values = new ArrayList<>() ;
		
		List<String> labels = getColumnLabels(resultSet) ;
		Map<String, Object> map = null ;
		
		while (resultSet.next()) {
			map = new HashMap<>() ;
			for (int i = 0; i < labels.size(); i++) {
				String columnLabel = labels.get(i) ;
				Object value = resultSet.getObject(i + 1) ;
				
				map.put(columnLabel, value) ;
			}
			values.add(map) ;
			
		}
		return values;
	}
	
	private List<String> getColumnLabels(ResultSet rs) throws SQLException {
		List<String> labels = new ArrayList<>() ;
		ResultSetMetaData rsmdData = rs.getMetaData() ;
		for (int i = 0; i < rsmdData.getColumnCount(); i++) {
			String columnLabel = rsmdData.getColumnLabel(i + 1) ;
			labels.add(columnLabel) ;
		}
		
		return labels ;
	}
	
	/**
	 * 查询单个值.
	 */
	@SuppressWarnings("unchecked")
	public <E> E getForValue(String sql, Object ... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null ;
		try {
			// 得到结果集.
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sql) ;
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet = preparedStatement.executeQuery() ;
			if (resultSet.next()) {
				return (E) resultSet.getObject(1) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(resultSet, preparedStatement, connection);
		}
		return null ;
	}

}
