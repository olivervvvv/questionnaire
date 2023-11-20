package com.example.questionnaire.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

//import com.example.demo.entity.MealId;

@Entity
@Table(name = "question")
@IdClass(value = QuestionId.class)
public class Question {

	@Id
	@Column(name = "id")
	private int quId;
	
	@Id
	@Column(name = "qn_id")
	private int qnId;
	
	@Column(name = "q_title")
	private String qTitle;
	
	@Column(name = "options_type")
	private String optionsType;
	
	@Column(name = "is_necessary")
	private boolean necessary;
	
	@Column(name = "options")
	private String options;

	public Question() {
		super();
	}

	public Question(int quId, int qnId, String qTitle, String optionType, boolean necessary, String option) {
		super();
		this.quId = quId;
		this.qnId = qnId;
		this.qTitle = qTitle;
		this.optionsType = optionType;
		this.necessary = necessary;
		this.options = option;
	}


	public int getQuId() {
		return quId;
	}

	public void setQuId(int quId) {
		this.quId = quId;
	}

	public int getqnId() {
		return qnId;
	}

	public void setqnId(int qnId) {
		this.qnId = qnId;
	}

	public String getqTitle() {
		return qTitle;
	}

	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}

	public String getOptionsType() {
		return optionsType;
	}

	public void setOptionsType(String optionType) {
		this.optionsType = optionType;
	}

	public boolean isNecessary() {
		return necessary;
	}

	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String option) {
		this.options = option;
	}
	
	
}
