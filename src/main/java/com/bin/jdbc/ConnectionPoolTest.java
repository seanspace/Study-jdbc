package com.bin.jdbc;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionPoolTest {

	/**
	 * 数据库连接池,有的容器会实现.DBCP是apache提供的开源组件包实现;
	 * 开源的数据库连接池实现:DBCP
	 * 1. 加入jar包    Apache下的commons工具库下
	 * 2. 创建数据库连接池.
	 */
	@Test
	public void testDBCP() throws SQLException{
		BasicDataSource dataSource = null ;
		// 创建DBCP数据源示例;
		dataSource = new BasicDataSource() ;
		
		// 2.为数据源设置必须的属性;
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql:///atguigu");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		// 2.1指定数据源的可选属性.
		// 1).指定数据库连接池的初始化连接数的个数;
		dataSource.setInitialSize(10);
		
		// 2).指定最大连接数:同一个时刻同时向数据库申请的连接数;不存在最小连接数.
		dataSource.setMaxTotal(50);// 这个方法和老版本名字有点不一样.setMaxActive();
		
		// 3).连接池空闲状态最少可以保留多少连接:
		dataSource.setMinIdle(3);
		// 4).连接池空闲状态最多可以保留多少连接:
		dataSource.setMaxIdle(10);
		// 5).等待连接池分配连接的最长时间:ms;超过就抛出异常;
		dataSource.setMaxWaitMillis(1000);
		// 3.从数据源示例获取数据库连接.
		Connection connection = dataSource.getConnection() ;
		System.out.println(connection);
		
		
	}
	
	
	/**
	 * DataSource工厂
	 * dbcp.properties文件的配置属性名必须和BasicDatasource
	 * 的属性一致,包括大小写;
	 */
	@Test
	public void testDBCPDataSourceFactory() throws Exception{
		Properties properties = new Properties() ;
		InputStream inputStream = JDBCTest.class.getClassLoader()
				.getResourceAsStream("dbcp.properties") ;
		properties.load(inputStream);
		final DataSource dataSource = BasicDataSourceFactory.createDataSource(properties) ;
		System.out.println(dataSource.getConnection());
		
		BasicDataSource basicDataSource = (BasicDataSource) dataSource ;
		System.out.println(basicDataSource.getMaxWaitMillis());
		
		System.out.println(dataSource.getConnection());
		System.out.println(dataSource.getConnection());
		System.out.println(dataSource.getConnection());
		// 第5个连接超时,因为最大连接数设置为5了.
		Connection connection2 = dataSource.getConnection() ;
		System.out.println("5  " + connection2);
		
		new Thread(){
			public void run() {
				Connection connection;
				try {
					// 这个线程等待5秒的超时时间.外面的线程3秒后会释放连接,所以能得到连接;
					connection = dataSource.getConnection();
					System.out.println(connection.getClass());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}.start();
		
		Thread.sleep(3000);
		
		connection2.close();
	}
	
	/**
	 * 开源的数据库连接池:C3P0;
	 * 官网:http://www.mchange.com/projects/c3p0/
	 * 1.c3p0.jar
	 */
	@Test
	public void testC3P0() throws PropertyVetoException, SQLException{
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql:///atguigu" );
		cpds.setUser("root");                                  
		cpds.setPassword("root"); 
		System.out.println(cpds.getConnection());
	}
	
	/**
	 * 配置文件方式.
	 * 1.创建:c3p0-config.xml文件.参考文档Appenddix B:Configuation File
	 */
	@Test
	public void testC3P0WithConfigFile() throws SQLException{
		DataSource dataSource = 
				new ComboPooledDataSource("helloc3p0") ;// <named-config name="helloc3p0"> 
		System.out.println(dataSource.getConnection());
		
		ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) dataSource ;
		System.out.println(comboPooledDataSource);
	}
	
	
	
}
