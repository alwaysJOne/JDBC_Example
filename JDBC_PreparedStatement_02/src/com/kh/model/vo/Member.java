package com.kh.model.vo;

import java.sql.Date;

/*
 * * VO(Value Object)
 * 	 한 명의 회원(DB테이블의 한 행의 데이터들)에 대한 데이터들을 기록할 수 있는 저장용 객체
 * */
public class Member {
	//필드
	//필드는 기본적으로 DB컬럼명과 유사하게 작업
	private int userNo;
	private String userID;   
	private String userPwd;   
	private String userName;   
	private String gender;   
	private int age;   
	private String email;   
	private String phone;   
	private String address;   
	private String hobby;
	private Date enrolldate; //java.sql.Date import해야함
	
	//생성자
	//기본생성자, 매개변수생성자
	public Member() {}

	public Member(int userNo, String userID, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby, Date enrolldate) {
		super();
		this.userNo = userNo;
		this.userID = userID;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
		this.enrolldate = enrolldate;
	} //알트 쉬프트 s o
	
	public Member(String userID, String userPwd, String userName, String gender, int age, String email, String phone,
			String address, String hobby) {
		super();
		this.userID = userID;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
	}

	//알트 쉬프트 s r
	//메소드
	//getter/setter메소드, toString메소드
	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Date getEnrolldate() {
		return enrolldate;
	}

	public void setEnrolldate(Date enrolldate) {
		this.enrolldate = enrolldate;
	}

	//알트 쉬프트 s s
	@Override
	public String toString() {
		return userNo + ", " + userID + ", " + userPwd + ", " + userName
				+ ", " + gender + ", " + age + ", " + email + ", " + phone + ", "
				+ address + ", " + hobby + ", " + enrolldate;
	}
	
}
