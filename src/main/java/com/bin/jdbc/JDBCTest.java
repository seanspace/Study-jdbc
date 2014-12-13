package com.bin.jdbc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class JDBCTest {
	
	/**
	 * Driver是一个接口：数据库厂商必须提供实现的接口；能够从其中获取数据库连接；
	 * 1.加入mysql驱动；
	 * 2.
	 */
	@Test
	public void testDriver() throws SQLException{
		// 创建一个Driver实现类的对象；
		Driver driver = new com.mysql.jdbc.Driver() ;
		
		// JDBC:子协议://地址；端口号/数据库名；
		String url = "jdbc:mysql://localhost:3306/test" ;
		Properties info = new Properties() ;
		info.put("user", "root") ;
		info.put("password", "root") ;
		
		// 调用Driver接口connect（URL，info）获取数据哭连接；
		Connection connection = driver.connect(url, info) ;
		
		System.out.println(connection);
		
		
		
	}
	
	/**
	 * 通用型,数据库连接;使用Driver的connection()方法;
	 */
	public Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException{
		String driverClass = null ;
		String jdbcUrl = null ;
		String user = null ;
		String password = null ;
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties") ;
		Properties properties = new Properties() ;
		properties.load(in);
		
		driverClass = properties.getProperty("driver") ;
		jdbcUrl = properties.getProperty("jdbcUrl") ;
		user = properties.getProperty("user") ;
		password = properties.getProperty("password") ;
		
		// 通过反射得到数据库驱动；
		Driver driver = (Driver) Class.forName(driverClass).newInstance() ;
		Properties info = new Properties() ;
		info.put("user",user) ;
		info.put("password",password) ;
		
		Connection connection = driver.connect(jdbcUrl, info) ;
		
		return connection ;
	}
	
	@Test
	public void testGetConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException{
		System.out.println(getConnection()); ;
	}
	
	/**
	 * DriverManager 是驱动的管理类;
	 * 1).可以同过重载的getconnectin()方法获取数据库连接.较为方便;
	 * 2).可以实现加载多个实现驱动类,只要传入的URL不一样就可以识别;
	 */
	@Test
	public void testDriverManager() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		String driverClass = null ;
		String jdbcUrl = null ;
		String user = null ;
		String password = null ;
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties") ;
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
		System.out.println(connection);
		
	}
	
	/**
	 * 1.Statement:用于执行SQL语句的对象
	 * 1).通过Connection的createStatement()方法来获取
	 * 2).通过executeUpdate(sql)可以执行SQL语句;
	 * 3).传入的Sql可以是Insert,update,delete.但不能是select.
	 * 
	 * 2.connection,statement都是资源,都需要关闭;
	 * 3.先关闭后获取的statement,在关闭先获取的;
	 */
	@Test
	public void testStatement(){
		// 1.获取数据库连接
		Connection conn = null;
		// 4.执行插入
				//1).获取操作sql语句的statement对象;
				// 调用Connection的createStatement()方法来获取;
		Statement statement = null;
		try {
			conn = getConnection();
			// 3.准备插入的sql语句
			String sql = "insert into customers(name,email,birth) values('ABCD','abcd@atguigu.com','1990-12-12')" ;
			
			statement = conn.createStatement();
			
			//2). 调用Statement对象的executeUpdate(sql)执行SQL语句进行插入;
			statement.executeUpdate(sql) ;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭
			try {
				if (statement != null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	/**
	 * 通用的更新方法:包括Insert,update,delete
	 * 版本1
	 */
	public void update(String sql){

		// 1.获取数据库连接
		Connection conn = null;
		// 4.执行插入
				//1).获取操作sql语句的statement对象;
				// 调用Connection的createStatement()方法来获取;
		Statement statement = null;
		try {
			conn = getConnection();
			// 3.准备插入的sql语句
//			String sql = "insert into customers(name,email,birth) values('ABCD','abcd@atguigu.com','1990-12-12')" ;
			
			statement = conn.createStatement();
			
			//2). 调用Statement对象的executeUpdate(sql)执行SQL语句进行插入;
			statement.executeUpdate(sql) ;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭
			try {
				if (statement != null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	
	}
	/**
	 * 从外面传入的连接,用于控制事务.
	 * @param conn
	 * @param sql
	 */
	public void update(Connection conn,String sql){
		
		// 4.执行插入
		//1).获取操作sql语句的statement对象;
		// 调用Connection的createStatement()方法来获取;
		Statement statement = null;
		try {
			conn = getConnection();
			// 3.准备插入的sql语句
//			String sql = "insert into customers(name,email,birth) values('ABCD','abcd@atguigu.com','1990-12-12')" ;
			
			statement = conn.createStatement();
			
			//2). 调用Statement对象的executeUpdate(sql)执行SQL语句进行插入;
			statement.executeUpdate(sql) ;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭
			try {
				if (statement != null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	
	/**
	 * ResultSet结果集.封装了JDBC查询的结果;
	 * 1.调用statement查询 ,executeQuery(sql)可以得到结果集;
	 * 2.ResultSet返回的实际就是一张数据表,有一个指针指向第一条数据的前面;可以用next()方法
	 * 检测下一行是否有效,若有该方法返回true,且指针下移.相当与Iterator对象的hasNext和next()结合体;
	 * 3.当指针对位到一行时,可以通过getXxx(index)或getXxx(columnName)获取每一列的值;
	 * 4.关闭resultset;
	 */
	@Test
	public void testResultSet(){
		Connection conn = null ;
		Statement statement = null ;
		ResultSet rs = null ;
		
		try {
			conn = JDBCTool.getConnection() ;
			
			statement = conn.createStatement() ;
			
			String sql = "Select * from customers where id = 3" ;
			rs = statement.executeQuery(sql) ;
			
			// 处理结果集
			while (rs.next()){
				int id = rs.getInt(1) ;
				String name = rs.getString("name") ;
				String email = rs.getString(3) ;
				Date birth = rs.getDate(4) ;
				
				System.out.println(id) ;
				System.out.println(name) ;
				System.out.println(email) ;
				System.out.println(birth) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(rs,statement, conn);
		}
		
	}
	
	/**
	 * PreparedStatement:是Statement的子接口,可以传入带占位符的SQL语句.
	 * 并且提供了占位符变量的方法;
	 *   索引值从1开始.
	 * 1).创建preparedStatement.
	 * 2).在我创建对象的同时,必须传入Sql
	 * 3).调用PreparedStatement的Set方法,setXxx(int index,Object val)设置占位符的值.
	 * 4).执行或查询execteQuery()或executeUpdate().注意:执行时不再需要传入,SQL语句;
	 */
	@Test
	public void testPreparedStatement(){
		Connection conn = null ;
		PreparedStatement preparedStatement = null ;
		
		try {
			conn = JDBCTool.getConnection() ;
			String sql = "insert into table1 values (?,?,?)" ;
			preparedStatement = conn.prepareStatement(sql) ;
			preparedStatement.setString(1, "值1"); ;
			preparedStatement.setString(2,  "值2");
			//   
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			preparedStatement.executeUpdate() ;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null,preparedStatement, conn);
		}
	}
	
	/**
	 * 通用的更新方法;
	 */
	public static void update(String sqlString,Object ... argsObjects) {
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		
		try {
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sqlString) ;
			
			for (int i = 0; i < argsObjects.length; i++) {
				preparedStatement.setObject(i + 1, argsObjects[i]);
			}
			
			preparedStatement.executeUpdate() ;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null,preparedStatement, connection);
		}
	}
	
	/**
	 * 通用的查询方法;
	 */
	public <T> T get(Class<T> clazz, String sql,Object ... args){
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
			resultSet = preparedStatement.executeQuery() ;
			
			/*
			 * 1.先得到结果集
			 * 2.利用反射创建对象;
			 * 3.获取结果集的列的别名:idCard
			 * 4.再获取结果集的每一列的值:结合3得到一个Map,键:列的别名,值:列的值;{flowId:5,idCard:xxxx}
			 * 5.利用反射为2的对应的属性赋值:属性即为Map的键,值即为Map的值;
			 */
			if (resultSet.next()){
				// 利用反射创建对象;
				entity = clazz.newInstance() ;
				// 通过解析sql语句来判断到底选择了哪些列,以及需要为entity对象的哪些属性赋值;
				Map<String, Object> map = new HashMap<>() ;
				/*
				 * 1.得到ResultSetMetaData对象.
				 * 2.打印label
				 */
				java.sql.ResultSetMetaData rsmd = resultSet.getMetaData() ;
				for (int i = 0 ; i < rsmd.getColumnCount() ; i++) {
					String columnLabe = rsmd.getColumnLabel(i + 1) ;
					Object columnValue = resultSet.getObject(columnLabe) ;
					map.put(columnLabe, columnValue) ;
				}
				System.out.println(map);
				
				Object object = clazz.newInstance() ;
				for (Map.Entry<String, Object> entry:map.entrySet()) {
					String fieldNameString = entry.getKey() ;
					Object fieldObject = entry.getValue() ;
					
					// 通过反射设置属性.
					System.out.println(fieldNameString);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null,preparedStatement, connection);
		}
		return entity ;
	}
	@Test
	public void testGet() {
		String sql = "select flow_id flowId,type,id_card idCard,examm_card examCard,student_name studentName"
				+ "location,grade from examstudent where flow_id = '5'" ;
//		Student student = get(Object.class, sql, ) ;
		// 列的别名需要和VO的属性名一致;
		
	}
	/**
	 * ResultSetMetaData
	 * 1).what:是描述ResultSet的元数据对象.即从中可以获取到的结果集中有多少列,列名师什么
	 * 2).how:
	 * ①.得到ResultSetMetaData有哪些好用的方法:
	 *    > int getColumnCount():SQL语句中包含哪些列;
	 *    > String getColumnLabel(int colun):获取指定的列的别名,其中索引从1开始.
	 * 
	 */
	@Test
	public void testResultSetMetaData() {
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null ;
		
		try {
			String sql = "select flow_id flowId,type,id_card idCard,"
					+ "exam_card examCard,student_nae studentName,"
					+ "location,grade from examstudent where flowid = ?" ;
			connection = JDBCTool.getConnection() ;
			preparedStatement = connection.prepareStatement(sql) ;
			preparedStatement.setInt(1, 5);
			
			resultSet = preparedStatement.executeQuery() ;
			Map<String, Object> map = new HashMap<>() ;
			/*
			 * 1.得到ResultSetMetaData对象.
			 * 2.打印label
			 */
			java.sql.ResultSetMetaData rsmd = resultSet.getMetaData() ;
			for (int i = 0 ; i < rsmd.getColumnCount() ; i++) {
				String columnLabe = rsmd.getColumnLabel(i + 1) ;
				Object columnValue = resultSet.getObject(columnLabe) ;
				map.put(columnLabe, columnValue) ;
			}
			System.out.println(map);
			
			if (resultSet.next()){
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null,preparedStatement, connection);
		}
	
	}
	
	/**
	 * 取得数据库自动生成的主键值.
	 */
	@Test
	public void testGetKeyValue() {
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		try {
			connection = JDBCTool.getConnection() ;
			String sql = "insert into customers(name,email,birth)" +
					"values(?,?,?)" ;
			preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS) ;
			preparedStatement.setString(1, "abcde");
			preparedStatement.setString(2, "abcde@guigu.com");
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			
			// 通过,getGeneratedKeys()方法获取新生成的ResultSet对象.
			preparedStatement.executeUpdate() ;
			ResultSet rs = preparedStatement.getGeneratedKeys() ;
			if (rs != null && rs.next()){
				System.out.println(rs.getObject(1));
			}
			
			// 在ResultSet中只有一列.列名:GENERATED_KEY,用于存放生成的主键值.
			ResultSetMetaData rsmd = rs.getMetaData() ;
			for (int i = 0 ; i < rsmd.getColumnCount() ; i++){
				System.out.println(rsmd.getColumnName(i + 1));
				System.out.println(rsmd.getColumnLabel(i + 1));
			}
			
		} catch(Exception e){
			e.printStackTrace(); 
		} finally {
			JDBCTool.releaseDB(null, preparedStatement, connection); ;
		}
	}
	
	/**
	 * 插入Blob类型的数据必须使用PreparedStatement,
	 * 因为blob类型无法用字符表示.
	 * 插入图片.
	 * 调用SetBlob(int index,InputStream)
	 */
	@Test
	public void tetInsetBlob(){

		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		try {
			connection = JDBCTool.getConnection() ;
			String sql = "insert into customers(name,email,birth,picture)" +
					"values(?,?,?,?)" ;
			preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS) ;
			preparedStatement.setString(1, "abcde");
			preparedStatement.setString(2, "abcde@guigu.com");
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			InputStream inputStream = new FileInputStream("1.jpg") ;
			preparedStatement.setBlob(4, inputStream); ;
			
			// 通过,getGeneratedKeys()方法获取新生成的ResultSet对象.
			preparedStatement.executeUpdate() ;
			ResultSet rs = preparedStatement.getGeneratedKeys() ;
			if (rs != null && rs.next()){
				System.out.println(rs.getObject(1));
			}
			
			// 在ResultSet中只有一列.列名:GENERATED_KEY,用于存放生成的主键值.
			ResultSetMetaData rsmd = rs.getMetaData() ;
			for (int i = 0 ; i < rsmd.getColumnCount() ; i++){
				System.out.println(rsmd.getColumnName(i + 1));
				System.out.println(rsmd.getColumnLabel(i + 1));
			}
			
		} catch(Exception e){
			e.printStackTrace(); 
		} finally {
			JDBCTool.releaseDB(null, preparedStatement, connection); ;
		}
	
	}
	
	/**
	 * 从数据库读出blob对象.
	 */
	@Test
	public void testReadBlob(){

		Connection conn = null ;
		Statement statement = null ;
		ResultSet rs = null ;
		OutputStream outputStream = null ;
		try {
			conn = JDBCTool.getConnection() ;
			statement = conn.createStatement() ;
			
			String sql = "Select * from customers where id = 11" ;
			rs = statement.executeQuery(sql) ;
			
			// 处理结果集
			while (rs.next()){
				int id = rs.getInt(1) ;
				String name = rs.getString("name") ;
				String email = rs.getString(3) ;
				Date birth = rs.getDate(4) ;
				
				java.sql.Blob pictrue = rs.getBlob(5) ;
				InputStream in = pictrue.getBinaryStream() ;
				outputStream = new FileOutputStream("flower.jpg") ;
				
				byte [] buffer = new byte[1024] ;
				int len = 0 ;
				while ((len = in.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
					
				}
				
				System.out.println(id) ;
				System.out.println(name) ;
				System.out.println(email) ;
				System.out.println(birth) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JDBCTool.releaseDB(rs,statement, conn);
		}
	}
	
	
	
	
	

}
