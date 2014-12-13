package com.bin.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class TransactionTest {
	/**
	 * tom给Jerry汇款500元.
	 * 
	 * 关于事务:
	 * 1.如果多个操作,每个操作使用的是自己的单独的连接,则无法保证事务;
	 * 2.具体步骤:
	 * 	1).事务操作前,开始事务:取消connection的默认提交行为;
	 *  2).如果事务的操作都成功,则提交事务.
	 *  3).回滚事务:若出现异常.则在catch块中回滚事务;
	 */
	@Test
	public void testTransaction(){
		/*
		 * 不是同一个connection
		JDBCTest dao = new JDBCTest() ;
		
		String sql = "update users Set balance = balance+500 where id = 1" ;
		dao.update(sql);
		String sql2 = "update users Set balance = balance-500 where id = 2" ;
		dao.update(sql2);
		*/
		
		Connection connection = null ;
		try {
			JDBCTest dao = new JDBCTest() ;
			connection = JDBCTool.getConnection() ;
			connection.setAutoCommit(false);
			String sql = "update users Set balance = balance+500 where id = 1" ;
			dao.update(connection,sql);
			String sql2 = "update users Set balance = balance-500 where id = 2" ;
			dao.update(connection,sql2);
			// 提交事务.
			connection.commit(); 
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务.
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * 测试事务的隔离级别.
	 * 通过connection的settransactionIsolation(int level)
	 * 来设置隔离级别.
	 */
	public void testTransactionIsolation(){
		Connection connection = null ;
		try {
			JDBCTest dao = new JDBCTest() ;
			connection = JDBCTool.getConnection() ;
			connection.setAutoCommit(false);
			String sql = "update users Set balance = balance+500 where id = 1" ;
			dao.update(connection,sql);
			String sql2 = "update users Set balance = balance-500 where id = 2" ;
			dao.update(connection,sql2);
			// 提交事务.
			connection.commit(); 
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务.
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			JDBCTool.releaseDB(null, null, connection);
		}
	}

}
