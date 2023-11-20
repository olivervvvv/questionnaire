package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Questionnaire;

public class QuestionnaireRes {

	private List<Questionnaire> questionnaire;
	
	private RtnCode rtnCode;

	public QuestionnaireRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionnaireRes(List<Questionnaire> questionnaire, RtnCode rtnCode) {
		super();
		this.questionnaire = questionnaire;
		this.rtnCode = rtnCode;
	}

	public List<Questionnaire> getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(List<Questionnaire> questionnaire) {
		this.questionnaire = questionnaire;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	
	
}
