package com.bin.jdbc.bean;

public class Student {
	private String idCard ;

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Override
	public String toString() {
		return "Student [idCard=" + idCard + "]";
	}
	
	
}
