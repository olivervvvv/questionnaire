package com.example.questionnaire.Service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.Service.ifs.QuizService;
import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
//import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;
	@Autowired
	private QuestionDao quDao;

	@Override
	@Transactional // 當2個save都能成功save的時候才會save，只能加在public 上面
	public QuizRes create(QuizReq req) {
		// 新增問卷
		// 使用檢查方法
		List<QuizVo> quizVoList = new ArrayList<>();
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		// 儲存後，把 QN 中最新一筆的ID拉出來，存到QU的qn_id中，
		int qnid = qnDao.save(req.getQuestionnaire()).getId();
		List<Question> quList = req.getQuestionList();
		// 可以只新增問卷，問卷內沒有題目
		if (quList.isEmpty()) {
			quizVoList.add(req);
			return new QuizRes(quizVoList,RtnCode.SUCCESSFUL);
//			return new QuizRes();
		}

//		int qnid = qnDao.findTopByOrderById().getId();
		for (Question qu : quList) {
			qu.setqnId(qnid);
		}
		quDao.saveAll(req.getQuestionList());
		return new QuizRes(quizVoList,RtnCode.SUCCESSFUL);
	}

	// 把檢查方法拉出來用
	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();

		if (!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null || qn.getStartDate().isAfter(qn.getEndDate())) {
			return new QuizRes(RtnCode.QNPARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() <= 0 || !StringUtils.hasText(qu.getqTitle()) || !StringUtils.hasText(qu.getOptionsType())
					|| !StringUtils.hasText(qu.getOptions())) {
				return new QuizRes(RtnCode.QUPARAM_ERROR);
			}
		}
		// 傳null代表成功，沒有錯
		return null;
	}

	@Transactional
	@Override
	public QuizRes update(QuizReq req) {
		// 能改的時機 ： 未發布+已發布尚未開始
		// 更新問卷
		// 使用檢查方法
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		// 抽出檢查ID的方法
		checkResult = checkQuid(req);
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.ID_NOTFOUNT);
		}
		// 能改的時機 ： 未發布+已發布尚未開始 ( 當前時間 < 開始時間 )
		if (!qnOp.get().isPublished()
				|| (qnOp.get().isPublished() && (LocalDate.now().isBefore(qnOp.get().getStartDate())))) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

	private QuizRes checkQuid(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.ID_ERROR);
		}
		// 判斷QuestionList裡的quid是否等於Questionnaire裡的id
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getqnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.ID_ERROR);
			}
		}

		return null;
	}

	@Override
	public QuizRes deleQuestionnaire(List<Integer> qnIdList) {
		// 刪多張問卷 只有 尚未發布 + 尚未開放(當前日期 < 開始日期)
		List<Questionnaire> resList = qnDao.findByIdIn(qnIdList);
		List<Integer> deleList = new ArrayList<>();
		for (Questionnaire qn : resList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
				deleList.add(qn.getId());
				// qnDao.deleteById(qn.getId());
			}
		}
		if (!deleList.isEmpty()) {
			qnDao.deleteAllById(deleList);
			quDao.deleteAllByQnIdIn(deleList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleQuestion(int qnid, List<Integer> quIdList) {
		List<Question> resList = quDao.findByQuIdInAndQnId(quIdList, qnid);
		List<Integer> idList = new ArrayList<>();
		Optional<Questionnaire> qnOp = qnDao.findById(qnid);
		if (!qnOp.get().isPublished()
				|| qnOp.get().isPublished() && LocalDate.now().isBefore(qnOp.get().getStartDate())) {

			for (Question qu : resList) {
				idList.add(qu.getQuId());
			}
			if (!idList.isEmpty() && qnid > 0) {
				quDao.deleteAllByQnIdInAndQuId(idList, qnid);
			}
		}

		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		
		title = StringUtils.hasText(title) ? title: "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		
		/* 寫成三元式 上面
		if(!StringUtils.hasText(title)) {
			title = "";
		}
		if(startDate == null) {
			startDate = LocalDate.of(1971, 1, 1);
		}
		if(endDate == null) {
			endDate = LocalDate.of(2099, 12, 31);
		}
		*/
		// 先找到第1、4、9張問卷，再找1、4、9張問卷裡的題目，並配對
		
		// 找到1 4 9 張問卷
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		// 取出 1 4 9 ID
		List<Integer> qnIdList = new ArrayList<>(); 
		for(Questionnaire qn:qnList) {
			qnIdList.add(qn.getId());
		}
		
		// 找到 1 4 9 張問卷裡的題目
		List<Question> quList = quDao.findAllByQnIdIn(qnIdList);
		
		// 配對問卷跟題目到 VO vo裡包一張問卷QN 和該問卷的多個題目list<QU>
		// for迴圈一張一張問卷，裡再for迴圈對應問卷的題目，用set放問卷題目
		// 找到的資料需要有個東西來裝，不管是問卷還是題目
		// 問卷是一題所以單個   題目是List 所以用list 去接
		List<QuizVo> quizVoList = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo(); // 接整張問卷和題目
			vo.setQuestionnaire(qn);  // 接問卷
			List<Question> quesList = new ArrayList<>();
			for(Question qu:quList) { 
				// ID相符，存入對應問卷的題目
				if(qu.getqnId() == qn.getId()) {					
					quesList.add(qu);
				}
			}
			vo.setQuestionList(quesList); // 接題目
			quizVoList.add(vo);  // 將題目和問卷存到VO
		}
		
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes create1(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		List<Question> quList = req.getQuestionList();
		int qnid = qnDao.save(qn).getId();
		for(Question qu:quList) {
			qu.setqnId(qnid);
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate, boolean isAll) {
		title = StringUtils.hasText(title) ? title: "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		if(isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(title, startDate, endDate);
		}
		else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		}
		return new QuestionnaireRes(qnList,RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if(qnId <= 0) {
			return new QuestionRes(null,RtnCode.QNPARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		
		return null;
	}


}
