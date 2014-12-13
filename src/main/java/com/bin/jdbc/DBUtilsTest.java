package com.bin.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.bin.jdbc.bean.Customer;

/**
 * 测试DBUtils工具类;
 *
 */
public class DBUtilsTest {
	/**
	 * 他是线程安全的,所以可以放在这里.
	 */
	QueryRunner queryRunner = new QueryRunner() ;
	/**
	 * 内部类.实现类.
	 */
	class MyResultSetHandler implements ResultSetHandler<List<Customer>> {
		@Override
		public List<Customer> handle(ResultSet rs) throws SQLException {
			List<Customer> customers = new ArrayList<>() ;
			while (rs.next()){
				Integer id = rs.getInt(1) ;
				String name = rs.getString(2) ;
				String email = rs.getString(3) ;
				Date birth = rs.getDate(4) ;
				
				Customer customer = new Customer(id, name, email, birth) ;
				customers.add(customer) ;
			}
			return customers;
		}
		
	}
	/**
	 * 测试QueryRunner类的update方法;
	 */
	@Test
	public void testQueryRunnerUpdate(){
		// 1.创建QueryRunner的实现类;
		
		// 2.
		String sqlString = "Delete from customers where id in (?,?)" ;
		Connection connection = null ;
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			queryRunner.update(connection, sqlString , 10 , 11) ;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * 测试查询,自己写的handler类;
	 */
	@Test
	public void testQuery(){
		Connection connection = null ;
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select id,name,email,birth from customers" ;
			Object object = queryRunner.query(connection,sql, new MyResultSetHandler()) ;
			System.out.println(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * BeanHandler:重点掌握的handler之一;<br>
	 * bean类的属性必须和sql语句字段的别名一致;
	 */
	@Test
	public void testBeanHandler(){
		Connection connection = null ;
		
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select id,name,email,birth from customers where id = ?" ;
			
			Customer customer = queryRunner.query(connection, sql, new BeanHandler<Customer>(Customer.class), "5") ;
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	/**
	 * BeanListHandler:重点掌握的handler之一;<br>
	 * 把结果集转为一个List,该List不为Null,但可能为空集合.<br>
	 * 若sql语句确实可以查到记录,List中存放创建BeanListHandler.
	 */
	@Test
	public void testBeanListHandler(){
		Connection connection = null ;
		
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select id,name,email,birth from customers" ;
			
			List<Customer> customers = queryRunner.query(connection, sql, new BeanListHandler<Customer>(Customer.class)) ;
			System.out.println(customers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	/**
	 * MapHandler:重点掌握的handler之一;<br>
	 * 只返回第一条记录.键值对:label,value;
	 */
	@Test
	public void testMapHandler(){
		Connection connection = null ;
		
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select id,name,email,birth from customers" ;
			
			Map<String, Object> result = queryRunner.query(connection, sql, new MapHandler()) ;
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	/**
	 * MapListHandler:重点掌握的handler之一;<br/>
	 * 只返回一批记录,map的list.键值对:label:value;
	 */
	@Test
	public void testMapListHandler(){
		Connection connection = null ;
		
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select id,name,email,birth from customers" ;
			
			List<Map<String,Object>> result = queryRunner.query(connection, sql, new MapListHandler()) ;
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * 返回一个值的Handler;(可以返回任意基本类型)
	 * 如返回多行多列:返回第一行的第一列;
	 */
	@Test
	public void testScalarHandler(){
		Connection connection = null ;
		
		try {
			connection = JDBCTool.getConnectionFromPool() ;
			String sql = "select count(id) from customers" ;
			
			long result = queryRunner.query(connection, sql, new ScalarHandler<Long>()) ;
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	
	}
	
	/**
	 * QueryLoader:可以用来加载存放这SQL语句的资源文件.
	 * 使用该类可以把SQL语句外置到一个资源文件中.已以提供更好的解耦合
	 */
	@Test
	public void testQueryLoadeer() throws IOException {
		//  "/"代表类路径的根路径;
		Map<String, String> sqls = QueryLoader.instance().load("/sql.properties") ;
		String updateSql = sqls.get("update_custommer") ;
		System.out.println(updateSql);
	}

}
