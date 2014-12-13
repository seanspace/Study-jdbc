package com.bin.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.junit.Test;

public class CallableStatmentTest {

	/**
	 * 使用JDBC调用存储过程.存储方法;
	 * 1.通过Connection对象的prepareCall()方法创建一个CallbleStatement对象.需要一个字符串;
	 *   {?= call add_fun(?,?)} 存储方法;{call add_fun(?,?)}存储过程;
	 * 2.通过callableStatement对象的registerOutParameter()方法注册out参数;返回值是属于out型的;
	 *   in型和in out型的参数通过setXXX()方法设置;null,可以是使用setNull()方法;
	 * 3.通过callableStatement对象的execute()方法执行存储过程.
	 * 4.如果调用的是带返回参数的存储过程或方法,通过getXXX()方法获取放回值.
	 */
	@Test
	public void testCallableStatment(){
		Connection connection = null ;
		CallableStatement callableStatement = null ;
		try {
			connection = JDBCTool.getConnection() ;
			String sql = "{?= call add_fun(?,?)}" ;
			callableStatement = connection.prepareCall(sql) ;
			callableStatement.registerOutParameter(1, Types.NUMERIC);// 不需要设值;
			callableStatement.setInt(2, 10);
			callableStatement.setInt(3, 3);
			callableStatement.execute() ;
			double sum = callableStatement.getInt(1) ;
			System.out.println(sum);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTool.releaseDB(null, callableStatement, connection);
		}
	}
}
