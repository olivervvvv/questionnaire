package com.example.questionnaire.Service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

public interface QuizService {
	
	public QuizRes create(QuizReq req);

	public QuizRes create1(QuizReq req);
	
	public QuizRes update(QuizReq req);
	
	public QuizRes deleQuestionnaire(List<Integer> qnIdList);

	public QuizRes deleQuestion(int qnid, List<Integer> quIdList);
	
	public QuizRes search(String title, LocalDate startDate,LocalDate endDate);

	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate,LocalDate endDate, boolean isAll);

	public QuestionRes searchQuestionList(int qnId);
	
}
