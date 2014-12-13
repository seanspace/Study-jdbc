package com.bin.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class BatchTest {
	/**
	 * 向Oracle插入10W条数据;
	 * 测试如何插入,用时更短;
	 * 1.使用statement
	 * 执行时间:3820ms
	 * 解释:发送10000条sql语句到数据库;
	 */
	@Test
	public void testBatchWithStatement() {
		Connection connection = null ;
		Statement statement = null ;
		String sql = null ;
		try {
			connection = JDBCTool.getConnection() ;
			JDBCTool.beginTansaction(connection); 
			statement = connection.createStatement() ;
			
			long begin = System.currentTimeMillis() ;
			for (int i = 0; i < 10000; i++) {
				sql = "insert into customers values(" + (i+1)
						+ ",'name_" + i + "','29-6月-13')" ;
				statement.executeUpdate(sql) ;
				// 积攒SQL语句;
				//statement.addBatch(sql);
			}
			long end = System.currentTimeMillis() ;
			System.out.println(end-begin);//3820MS,这是最后结果.
			JDBCTool.coommit(connection);
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
			JDBCTool.rollback(connection);
		} finally {
			JDBCTool.releaseDB(null, statement, connection);
		}
		
	}
	/**
	 * 测试PreparedStatement,插入1W数据;
	 * 执行时间:1450ms
	 * 解释:sql语句只发了一次,之后发了10000次参数给数据库.
	 */
	@Test
	public void testBatchWithPreparedStatement() {
		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		String sql = null ;
		try {
			connection = JDBCTool.getConnection() ;
			JDBCTool.beginTansaction(connection); 
			sql = "insert into customers values(?,?,?)" ;
			preparedStatement = connection.prepareStatement(sql);
			
			long begin = System.currentTimeMillis() ;
			for (int i = 0; i < 10000; i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setString(2, "name_" + 1);
				preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
				
				preparedStatement.executeUpdate() ;
				
			}
			long end = System.currentTimeMillis() ;
			System.out.println(end-begin);// 1450
			JDBCTool.coommit(connection);
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
			JDBCTool.rollback(connection);
		} finally {
			JDBCTool.releaseDB(null, preparedStatement, connection);
		}
		
	}
	
	/**
	 * 测试批处理,插入1W数据;
	 * 执行时间:230ms
	 * 解释:sql语句只发了一次,之后每300条参数,发送一次参数给数据库.
	 * 
	 * statement和preparedStatement都可以发批处理;
	 */
	@Test
	public void testBatch(){

		Connection connection = null ;
		PreparedStatement preparedStatement = null ;
		String sql = null ;
		try {
			connection = JDBCTool.getConnection() ;
			JDBCTool.beginTansaction(connection); 
			sql = "insert into customers values(?,?,?)" ;
			preparedStatement = connection.prepareStatement(sql);
			
			long begin = System.currentTimeMillis() ;
			for (int i = 0; i < 10000; i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setString(2, "name_" + 1);
				preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
				
				//"积攒"SQL参数到批处理;
				preparedStatement.addBatch();
				if ((i + 1) % 300 == 0) {
					// 积攒300条的时候,就统一执行一次;并前清空之前积攒的;
					preparedStatement.executeBatch() ;
					preparedStatement.clearBatch();
				}
			}
			// 若总条数不是批量数值的整数倍,则还需要额外再执行一次;
			if (10000 % 300 != 0) {
				preparedStatement.executeBatch() ;
				preparedStatement.clearBatch();
			}
			long end = System.currentTimeMillis() ;
			System.out.println(end-begin);// 1450
			JDBCTool.coommit(connection);
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
			JDBCTool.rollback(connection);
		} finally {
			JDBCTool.releaseDB(null, preparedStatement, connection);
		}
		
	
	}
	
	/*
	 * 总结后发现:批处理最快.
	 */
	

}
