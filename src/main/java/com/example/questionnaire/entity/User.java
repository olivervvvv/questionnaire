package com.example.questionnaire.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	@Column(name = "name")
	private String name;
	
	@Id
	@Column(name = "phone_number")
	private String phoneNumder;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "qn_id")
	private int qnId;
	
	@Column(name = "q_id")
	private int qid;

	@Column(name = "ans")
	private String ans;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String name, String phoneNumder, String email, int age, int qnId, int qid, String ans) {
		super();
		this.name = name;
		this.phoneNumder = phoneNumder;
		this.email = email;
		this.age = age;
		this.qnId = qnId;
		this.qid = qid;
		this.ans = ans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumder() {
		return phoneNumder;
	}

	public void setPhoneNumder(String phoneNumder) {
		this.phoneNumder = phoneNumder;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getQnId() {
		return qnId;
	}

	public void setQnId(int qnId) {
		this.qnId = qnId;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}
	
	
}
