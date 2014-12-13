package com.bin.jdbc;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.bin.jdbc.bean.Student;

/**
 * Apache的工具包;
 * 一般情况:字段名和属性名一致;
 * JavaEE中有getter和setter的成员域,叫做属性;
 * 其他的叫做字段;
 */
public class BeanUtilsTest {
	@Test
	public void testSetProperty() throws IllegalAccessException, InvocationTargetException{
		Object object = new Student() ;
		BeanUtils.setProperty(object, "idCard", "dafda");
		System.out.println(object);
		
	}
	
	@Test
	public void testGetProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		Object object = new Student() ;
		BeanUtils.setProperty(object, "idCard", "dafda");
		System.out.println(object);
		
		Object val = BeanUtils.getProperty(object, "idCard") ;
		System.out.println(val);
	}

}
