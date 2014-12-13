package com.bin.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 访问数据的DAO接口;
 * 里面定义好访问数据表的各种方法;
 * 可以由QueryRunnner和Hibernate等去实现;
 * @param  T:DAO 处理的实体类的类型;
 */
public interface IDAO<T> {
	/**
	 * insert,update,delete
	 * @param connection    数据库连接;
	 * @param sql			sql语句;
	 * @param args			填充占位符;
	 */
	public void update(Connection connection ,String sql, Object ... args) ;

	/**
	 * 返回一个数值;
	 * @throws SQLException 
	 */
	public T get(Connection connection ,String sql, Object ... args) throws SQLException ;
	
	/**
	 * 返回T的一个集合;
	 */
	public List<T> getForList(Connection connection ,String sql, Object ... args) ;
	
	/**
	 * 返回一个值.结果集的第一行的第一列;
	 */
	public <E> E getForValue(Connection connection ,String sql, Object ... args) ;
	
	/**
	 * 批量处理的方法;
	 * @param args   :填充占位符的Object[]的可变参数;
	 */
	public void batch(Connection connection ,String sql, Object[] ... args) ;
	
}
