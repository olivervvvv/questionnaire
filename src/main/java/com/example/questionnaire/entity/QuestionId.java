package com.example.questionnaire.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuestionId implements Serializable{

	private int quId;
	
	private int qnId;

	public QuestionId() {
		super();
	}

	public QuestionId(int quId, int qId) {
		super();
		this.quId = quId;
		this.qnId = qId;
	}

	public int getQuId() {
		return quId;
	}

	public void setQuId(int quId) {
		this.quId = quId;
	}

	public int getqId() {
		return qnId;
	}

	public void setqId(int qId) {
		this.qnId = qId;
	}

	
}
