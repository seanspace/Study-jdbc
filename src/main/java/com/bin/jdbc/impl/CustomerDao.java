package com.bin.jdbc.impl;

import com.bin.jdbc.bean.Customer;

/**
 * 这个类可以不用定义自己的方法;
 * 只是传了一个Customer给他就可以;就是给泛型传参数;
 *
 */
public class CustomerDao extends JDBCDaoImpl<Customer> {
	
}
