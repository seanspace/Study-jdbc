package com.bin.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCTool {
	private static DataSource dataSource = null ;
	// 数据库连接池只被初始化一次.
	static {
		dataSource = new ComboPooledDataSource("helloc3p0") ;
	}
	
	@Test
	public void testJDBCTool() throws SQLException{
		Connection connection = JDBCTool.getConnectionFromPool() ;
		System.out.println(connection);
		
	}
	
	/**
	 * 释放资源.
	 */
	public static void releaseDB(ResultSet resultSet,Statement statement,Connection connection) {
		try {
			if (resultSet != null){
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (statement != null){
				// 数据库连接池的Connection对象close时,并不是真的进行关闭,而是把
				// 连接归还到数据库连接池中.
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
	}
	
	public static Connection getConnectionFromPool() throws SQLException{
		return dataSource.getConnection();
	}
	
	/**
	 * 获取数据库连接的方法;
	 */
	public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException{
		String driverClass = null ;
		String jdbcUrl = null ;
		String user = null ;
		String password = null ;
		
		InputStream in = JDBCTool.class.getClassLoader().getResourceAsStream("jdbc.properties") ;
		Properties properties = new Properties() ;
		properties.load(in);
		
		driverClass = properties.getProperty("driver") ;
		jdbcUrl = properties.getProperty("jdbcUrl") ;
		user = properties.getProperty("user") ;
		password = properties.getProperty("password") ;
		
		// 加载数据库驱动;(注册驱动)
		Class.forName(driverClass) ;
		//DriverManager.registerDriver(driverClass);
		
		Connection connection = DriverManager.getConnection(jdbcUrl,user,password) ;
		return connection ;
	}
	
	/**
	 * 提交事务.
	 */
	public static void  coommit(Connection connection) {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 回滚事务
	 */
	public static void  rollback(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 开始事务.
	 */
	public static void  beginTansaction(Connection connection) {
		if (connection != null) {
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	


}
