package com.example.questionnaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.questionnaire.Service.ifs.QuizService;
import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

@SpringBootTest
public class CreateServiceTest {

	@Autowired
	private QuizService quizService;
	
	@Autowired
	private QuestionDao quDao;

	@Autowired
	private QuestionnaireDao qnDao;

	@Test
	public void CreateTest1() {
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setTitle("BQuiz");
		questionnaire.setPublished(true);
		questionnaire.setDescription("AAAAAAAAAA");
		questionnaire.setStartDate(LocalDate.of(2023, 12, 1));
		questionnaire.setEndDate(LocalDate.of(2023, 12, 25));
		QuizReq req = new QuizReq();
//		req.setQuestionnaire(questionnaire);
		
		List<Question> questionList = new ArrayList<>();
		Question qu = new Question(1,0,"QuTitle","Text",true,"Option");
		questionList.add(qu);
		qu = new Question(1,0,"AQuTitle","Text",true,"Option");
		questionList.add(qu);
		qu = new Question(2,0,"BQuTitle","Text",true,"Option");
		questionList.add(qu);
		qu = new Question(3,0,"CQuTitle","Text",true,"Option");
		questionList.add(qu);
		qu = new Question(4,0,"DQuTitle","Text",true,"Option");
		questionList.add(qu);
		req.setQuestionnaire(questionnaire);
		req.setQuestionList(questionList);
		
		System.out.println(quizService.create1(req).getRtnCode());
	
		
	}
	@Test
	public void updateTest1() {
		// 準備測試數據
	    QuizReq quizReq = new QuizReq();
	    
	    // 設置問卷信息
	    Questionnaire questionnaire = new Questionnaire();
	    questionnaire.setTitle("Sample Quiz");
	    quizReq.setQuestionnaire(questionnaire);

	    // 設置問題信息
	    List<Question> questionList = new ArrayList<>();
	    Question question1 = new Question();
	    question1.setqTitle("What is your favorite color?");
	    question1.setOptionsType("Blue");
	    question1.setOptionsType("Red");
	    question1.setOptionsType("");
	    questionList.add(question1);

//	    Question question2 = new Question();
//	    question2.setOptionsType("How satisfied are you with our service?");
//	    question2.setOptions("Very Satisfied");
//	    questionList.add(question2);

	    quizReq.setQuestionList(questionList);

	    // 呼叫被測試的方法
	    QuizRes result = quizService.create(quizReq);

	    // 進行斷言 (Assertions) 來確認預期結果
	    assertNotNull(result);
//	    assertEquals(RtnCode.SUCCESSFUL, result.getRtnCode());
//	    assertEquals(RtnCode.UPDATE_ERROR, result.getRtnCode());


	}
	 @Test
	 @Transactional
	 public void testDeleQuestionnaire() {
	        // 準備測試數據
	        List<Integer> qnIdList = Arrays.asList(1, 2, 3);

	        // 呼叫被測試的方法
	        QuizRes result = quizService.deleQuestionnaire(qnIdList);

	        // 進行斷言 (Assertions) 來確認預期結果
	        assertNotNull(result);
	        assertEquals(RtnCode.SUCCESSFUL, result.getRtnCode());
	    }
}
