package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Questionnaire;

public class QuestionRes {

	private List<Questionnaire> question;

	private RtnCode rtnCode;

	public QuestionRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionRes(List<Questionnaire> question, RtnCode rtnCode) {
		super();
		this.question = question;
		this.rtnCode = rtnCode;
	}

	public List<Questionnaire> getQuestion() {
		return question;
	}

	public void setQuestion(List<Questionnaire> question) {
		this.question = question;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	

}
