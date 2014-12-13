package com.bin.jdbc.practice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class ReviewTest {
	/**
	 * Connection代表应用程序和数据库的一个链接;
	 * 可以使用类加载器加载类路径下的文件;
	 */
	@Test
	public void testGetConnection() throws ClassNotFoundException, SQLException, IOException{
		getConnection();
	}

	private Connection getConnection() throws IOException, ClassNotFoundException,
			SQLException {
		// 1.准备4个字符串;
		String user = "root" ;
		String password = "root" ;
		String jdbcUrl = "jdbc:mysql://localhost:3306/atguigu" ;
		String driverClass = "com.mysql.jdbc.Driver" ;
		// 读取jdbc.properties文件;
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		properties.get("user") ;// 如此去得到一个键值对;
		
		// 2.加载驱动;
		Class.forName(driverClass) ;
		// 3.DriverManager.getConnection();获取链接;
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password) ;
		return connection ;
	}
	
	/**
	 * 
	 */
	@Test
	public void testStatement(){
		Connection connection = null ;
		java.sql.Statement statement = null ;
		
		try {
			// 1.获取数据库连接;
			connection = getConnection() ;
			// 2.调用Connection对象的createStatement()获取Statement对象;
			statement = connection.createStatement() ;
			// 3.准备SQl
			String sqlString = "update customers set name = 'jerry' " +
			"where id = 5" ;
			// 4.发送Sql语句:调用Statement对象的executeUpdate(sql)方法;
			statement.executeUpdate(sqlString) ;
			// 5.关闭流资源;
			releaseDB(null, statement, connection); ;
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void releaseDB(ResultSet resultSet,Statement statement,Connection connection	){
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testResultSet() {
		Connection connection = null ;
		Statement statement = null ;
		ResultSet resultSet = null ;
		
		try {
			connection = getConnection() ;
			statement = connection.createStatement() ;
			String sql = "Select * from customers" ;
			resultSet = statement.executeQuery(sql) ;
			while (resultSet.next()) {
				int id = resultSet.getInt(1) ;
				String nameString = resultSet.getString(2) ;
				String email = resultSet.getString(3) ;
				Date date = resultSet.getDate(4) ;
				System.out.println(id);
				System.out.println(nameString);
				System.out.println(email);
				System.out.println(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			releaseDB(resultSet, statement, connection);
		}
	}

}
